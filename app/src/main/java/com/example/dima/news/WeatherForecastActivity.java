package com.example.dima.news;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dima.news.Interface.WeatherService;
import com.example.dima.news.adapter.ListSourceAdapter;
import com.example.dima.news.adapter.ListWeatherForecastAdapter;
import com.example.dima.news.common.Common;
import com.example.dima.news.model.weather.ForecastWeather;
import com.example.dima.news.model.weather.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dima on 02.05.2018.
 */

public class WeatherForecastActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private ListWeatherForecastAdapter adapter;

    private SpotsDialog dialog;
    private WeatherService weatherService;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_forecast);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Weather forecast");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        weatherService = Common.getWeatherService();

        recyclerView = findViewById(R.id.weather_recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        dialog = new SpotsDialog(this);
        dialog.show();

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_weather);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getWeatherForecast();
            }
        });
        getWeatherForecast();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void supportNavigateUpTo(@NonNull Intent upIntent) {
        super.supportNavigateUpTo(upIntent);
    }

    public void getWeatherForecast() {
        swipeRefreshLayout.setRefreshing(true);
        String city = PreferenceManager.getDefaultSharedPreferences(this).getString("city", "");
        weatherService.getForecast(city, Common.units, Common.WEATHER_API_KEY).enqueue(new Callback<ForecastWeather>() {
            @Override
            public void onResponse(Call<ForecastWeather> call, Response<ForecastWeather> response) {
                adapter = new ListWeatherForecastAdapter(getBaseContext(),response.body().getList());
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
                dialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ForecastWeather> call, Throwable t) {

            }
        });
    }
}
