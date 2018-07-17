package com.example.dima.news;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.dima.news.Interface.WeatherService;
import com.example.dima.news.adapter.NewsFragmentAdapter;
import com.example.dima.news.mvp.model.weather.CurrentWeather;
import com.example.dima.news.mvp.presenter.WeatherPresenter;
import com.example.dima.news.mvp.view.WeatherView;
import com.squareup.picasso.Picasso;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class MainActivity extends MvpAppCompatActivity implements WeatherView {
    @InjectPresenter
    WeatherPresenter weatherPresenter;

    private WeatherService weatherService;

    private SpotsDialog dialog;
    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView cityTemp, sun, windSpeedHumidity, pressure;
    private ImageView weatherImage;
    private LinearLayout weatherForecast;

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Paper.init(this);
        final String city = PreferenceManager.getDefaultSharedPreferences(this).getString("city", "");


        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        mViewPager = findViewById(R.id.viewPager);
        NewsFragmentAdapter adapter = new NewsFragmentAdapter(getSupportFragmentManager(),this);
        mViewPager.setAdapter(adapter);
        mTabLayout = findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);

        //Weather
        cityTemp = findViewById(R.id.weather_name_t);
        windSpeedHumidity = findViewById(R.id.weather_wind_speed);
        sun = findViewById(R.id.weather_sunrise);
        pressure = findViewById(R.id.weather_pressure);
        weatherImage = findViewById(R.id.weather_image);

        dialog = new SpotsDialog(this);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        weatherForecast = findViewById(R.id.open_weather_forecast);
        weatherForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherForecastActivity.class);
                startActivity(intent);
            }
        });

        //loadWebSite(false);
        weatherPresenter.loadWeather(city);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
               // loadSearchArticles(query);
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


    @Override
    public void weatherView(CurrentWeather currentWeather) {
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

        Picasso.with(getBaseContext())
                .load(currentWeather.getWeather().get(0).getIcon())
                .into(weatherImage);
    }

    @Override
    public void error() {
        Toast.makeText(this, R.string.openWeatherMap_not_working, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void dialogShow() {
        dialog.show();
    }

    @Override
    public void dialogDismiss() {
        dialog.dismiss();
    }

//    public void loadSearchArticles(String search) {
//        int agoDay = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("search_articles", ""));
//        IntervalDays intervalDays = new IntervalDays(agoDay);
//        String today = intervalDays.getToday();
//        String daysAgo = intervalDays.getDaysAgo();
//        newsService.getSearch(search,daysAgo,today,"popularity",Common.API_KEY).enqueue(new Callback<News>() {
//            @Override
//            public void onResponse(Call<News> call, Response<News> response) {
//                newsAdapter = new ListNewsAdapter(response.body().getArticles(), getBaseContext());
//                newsAdapter.notifyDataSetChanged();
//                listWebsite.setAdapter(newsAdapter);
//            }
//
//            @Override
//            public void onFailure(Call<News> call, Throwable t) {
//
//            }
//        });
//    }
}
