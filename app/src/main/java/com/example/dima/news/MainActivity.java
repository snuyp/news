package com.example.dima.news;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dima.news.Interface.NewsService;
import com.example.dima.news.Interface.WeatherService;
import com.example.dima.news.adapter.ListSourceAdapter;
import com.example.dima.news.common.Common;
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

    private ListSourceAdapter adapter;
    private SpotsDialog dialog;
    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView cityTemp, sun, windSpeedHumidity, pressure, temp_min_max;
    private ImageView weatherImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);

        newsService = Common.getNewsService();
        weatherService = Common.getWeatherService();

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWebSiteSource(true);
            }
        });

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

        loadWebSiteSource(false);
        loadWeather(false);

    }

    private void loadWeather(boolean isRefreshed) {
        dialog.show();
        weatherService.getToday(Common.city,Common.units,Common.WEATHER_API_KEY).enqueue(new Callback<CurrentWeather>() {
            @Override
            public void onResponse(Call<CurrentWeather> call, Response<CurrentWeather> response) {
                CurrentWeather currentWeather = response.body();
                String cityT = currentWeather.getName()+" "+currentWeather.getMain().getTemp().toString();
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
            }

            @Override
            public void onFailure(Call<CurrentWeather> call, Throwable t) {

            }
        });

    }

    private void loadWebSiteSource(boolean isRefreshed) {
        if(!isRefreshed)
        {
            String cache = Paper.book().read("cache");
            if(cache != null && !cache.isEmpty() && !cache.equals("null"))
            {
                WebSite webSite = new Gson().fromJson(cache,WebSite.class);
                adapter = new ListSourceAdapter(getBaseContext(),webSite);
                adapter.notifyDataSetChanged();
                listWebsite.setAdapter(adapter);
            }
            else
            {
                dialog.show();

                    newsService.getSources(Common.language,Common.API_KEY).enqueue(new Callback<WebSite>() {
                    @Override
                    public void onResponse(@NonNull Call<WebSite> call, @NonNull Response<WebSite> response) {
                        adapter = new ListSourceAdapter(getBaseContext(),response.body());
                        adapter.notifyDataSetChanged();
                        listWebsite.setAdapter(adapter);

                        //Save to cache
                        Paper.book().write("cache",new Gson().toJson(response.body()));
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<WebSite> call, @NonNull Throwable t) {
                        Log.e("Failure","Failure"+t.getMessage());
                    }
                });

            }
        }
        else
        {
            swipeRefreshLayout.setRefreshing(true);
            newsService.getSources(Common.language,Common.API_KEY).enqueue(new Callback<WebSite>() {
                @Override
                public void onResponse(@NonNull Call<WebSite> call, @NonNull Response<WebSite> response) {
                    adapter = new ListSourceAdapter(getBaseContext(),response.body());
                    adapter.notifyDataSetChanged();
                    listWebsite.setAdapter(adapter);

                    //Save to cache
                    Paper.book().write("cache",new Gson().toJson(response.body()));

                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(@NonNull Call<WebSite> call, @NonNull Throwable t) {
                    Log.e("Failure","Failure"+t.getMessage());
                }
            });
        }
    }
}
