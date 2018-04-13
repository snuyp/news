package com.example.dima.news;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dima.news.Interface.NewsService;
import com.example.dima.news.Interface.RatesService;
import com.example.dima.news.Interface.WeatherService;
import com.example.dima.news.adapter.ListNewsAdapter;
import com.example.dima.news.adapter.ListSourceAdapter;
import com.example.dima.news.common.Common;
import com.example.dima.news.common.IntervalDays;
import com.example.dima.news.model.news.News;
import com.example.dima.news.model.news.WebSite;
import com.example.dima.news.model.weather.CurrentWeather;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView listWebsite;
    private RecyclerView.LayoutManager layoutManager;

    private NewsService newsService;
    private WeatherService weatherService;
    private RatesService rateService;

    private ListSourceAdapter adapter;
    private ListNewsAdapter newsAdapter;
    private SpotsDialog dialog;
    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView cityTemp, sun, windSpeedHumidity, pressure;
    private ImageView weatherImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);

        newsService = Common.getNewsService();
        weatherService = Common.getWeatherService();
        rateService = Common.getRatesService();

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWebSite(true);
            }
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        listWebsite = findViewById(R.id.list_source);
        listWebsite.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listWebsite.setLayoutManager(layoutManager);

        dialog = new SpotsDialog(this);

        //Weather
        cityTemp = findViewById(R.id.weather_name_t);
        windSpeedHumidity = findViewById(R.id.weather_wind_speed);
        sun = findViewById(R.id.weather_sunrise);
        pressure = findViewById(R.id.weather_pressure);
        weatherImage = findViewById(R.id.weather_image);

        loadWebSite(false);
        loadWeather();
    }

    private void loadRates() {
        swipeRefreshLayout.setRefreshing(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setBackgroundColor(getResources().getColor(R.color.black_overlay));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                    loadSearchArticles(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                //Common.getPreviousDateOfEndDate(3).getToday();
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

//            case R.id.action_favorite:
//                // User chose the "Favorite" action, mark the current item
//                // as a favorite...
//                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    private void loadWeather() {
        swipeRefreshLayout.setRefreshing(true);
        dialog.show();
        String city = PreferenceManager.getDefaultSharedPreferences(this).getString("city", "");

        weatherService.getToday(city, Common.units, Common.WEATHER_API_KEY).enqueue(new Callback<CurrentWeather>() {
            @Override
            public void onResponse(Call<CurrentWeather> call, Response<CurrentWeather> response) {
                CurrentWeather currentWeather = response.body();
                String cityT = currentWeather.getName() + " " + currentWeather.getMain().getTemp().toString();
                cityTemp.setText(cityT);
                pressure.setText(
                        String.format("%s %s", getResources().getString(R.string.pressure),
                                currentWeather.getMain().getPressure()));

                windSpeedHumidity.setText(
                        String.format("%s %s\n%s %s", getResources().getString(R.string.wind_speed),
                                currentWeather.getWind().getSpeed(), getResources().getString(R.string.humidity),
                                currentWeather.getMain().getHumidity()));

                sun.setText(String.format("%s%s", currentWeather.getSys().getSunrise(), currentWeather.getSys().getSunset()));

//                temp_min_max.setText
//                        (String.format("%s\n%s", currentWeather.getMain().getTempMax(), currentWeather.getMain().getTempMin()));

                Picasso.with(getBaseContext())
                        .load(currentWeather.getWeather().get(0).getIcon())
                        .into(weatherImage);

                dialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<CurrentWeather> call, Throwable t) {

            }
        });

    }

    private void loadWebSite(boolean isRefreshed) {
        String languageSource = PreferenceManager.
                getDefaultSharedPreferences(this).getString("select_language", "");

        if (!isRefreshed) {
            String cache = Paper.book().read("cache");
            if (cache != null && !cache.isEmpty() && !cache.equals("null")) {
                WebSite webSite = new Gson().fromJson(cache, WebSite.class);
                adapter = new ListSourceAdapter(getBaseContext(), webSite);
                adapter.notifyDataSetChanged();
                listWebsite.setAdapter(adapter);
            } else {
                dialog.show();

                newsService.getSources(languageSource, Common.API_KEY).enqueue(new Callback<WebSite>() {
                    @Override
                    public void onResponse(@NonNull Call<WebSite> call, @NonNull Response<WebSite> response) {
                        adapter = new ListSourceAdapter(getBaseContext(), response.body());
                        adapter.notifyDataSetChanged();
                        listWebsite.setAdapter(adapter);

                        //Save to cache
                        Paper.book().write("cache", new Gson().toJson(response.body()));
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<WebSite> call, @NonNull Throwable t) {
                        Log.e("Failure", "Failure" + t.getMessage());
                    }
                });

            }
        } else {
            swipeRefreshLayout.setRefreshing(true);
            loadWeather();
                newsService.getSources(languageSource, Common.API_KEY).enqueue(new Callback<WebSite>() {
                    @Override
                    public void onResponse(@NonNull Call<WebSite> call, @NonNull Response<WebSite> response) {
                        adapter = new ListSourceAdapter(getBaseContext(), response.body());
                        adapter.notifyDataSetChanged();
                        listWebsite.setAdapter(adapter);

                        //Save to cache
                        Paper.book().write("cache", new Gson().toJson(response.body()));

                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(@NonNull Call<WebSite> call, @NonNull Throwable t) {
                        Log.e("Failure", "Failure" + t.getMessage());
                    }
                });

        }
    }
    public void loadSearchArticles(String search)
    {
        int agoDay = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("search_articles", ""));
        IntervalDays intervalDays = new IntervalDays(agoDay);
        String today = intervalDays.getToday();
        String daysAgo = intervalDays.getDaysAgo();

        newsService.getSearch(Common.getApiUrlSearch(search,daysAgo,today)).enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                newsAdapter = new ListNewsAdapter(response.body().getArticles(), getBaseContext());
                newsAdapter.notifyDataSetChanged();
                listWebsite.setAdapter(newsAdapter);
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {

            }
        });
    }
}
