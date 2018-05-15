package com.example.dima.news;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.dima.news.Interface.WeatherService;
import com.example.dima.news.adapter.ListWeatherForecastAdapter;
import com.example.dima.news.common.Common;
import com.example.dima.news.model.weather.ForecastWeather;
import com.example.dima.news.model.weather.List;
import com.github.aakira.expandablelayout.ExpandableWeightLayout;

import java.util.ArrayList;

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
    private Button expandleButton;

    private Button todayForecast;
    private Button tomorrowForecast;
    private Button afterTomorrowForecast;
    private Button afterThreeDaysForecast;
    private ImageView toggleButtonImage;
    private ExpandableWeightLayout mExpandLayout;

    private ArrayList<Integer> beginOfNewDay = new ArrayList<>();
    private java.util.List<List> forecastList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_forecast);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitle(R.string.weather_forecast);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        weatherService = Common.getWeatherService();

        recyclerView = findViewById(R.id.weather_recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        dialog = new SpotsDialog(this);
        dialog.show();
        mExpandLayout = (ExpandableWeightLayout) findViewById(R.id.expandableLayout);

        Button weatherForecastButton = (Button) findViewById(R.id.weather_forecast_on_four_days);
        weatherForecastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleReload(0,forecastList.size());

            }
        });


        todayForecast = findViewById(R.id.todayButton);
        todayForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               toggleReload(0, beginOfNewDay.get(0));
            }
        });

        tomorrowForecast = findViewById(R.id.tomorrow_button);
        tomorrowForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleReload(beginOfNewDay.get(0), beginOfNewDay.get(1));
            }
        });

        afterTomorrowForecast = findViewById(R.id.after_tomorrow_button);
        afterTomorrowForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleReload(beginOfNewDay.get(1),beginOfNewDay.get(2));
            }
        });

        afterThreeDaysForecast = findViewById(R.id.aafter_three_days_button);
        afterThreeDaysForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleReload(beginOfNewDay.get(2),beginOfNewDay.get(3));
            }
        });

        toggleButtonImage = findViewById(R.id.close_toggle_button);
        toggleButtonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandLayout.toggle();
            }
        });
        getWeatherForecast();
    }

    private void toggleReload(final int subListFromIndex, final int subListToIndex)
    {
        mExpandLayout.collapse();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mExpandLayout.toggle();
                addToAdapter(forecastList.subList(subListFromIndex, subListToIndex));
            }
        }, 1050);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void supportNavigateUpTo(@NonNull Intent upIntent) {
        super.supportNavigateUpTo(upIntent);
    }

    private void getWeatherForecast() {
        // swipeRefreshLayout.setRefreshing(true);
        String city = PreferenceManager.getDefaultSharedPreferences(this).getString("city", "");
        weatherService.getForecast(city, Common.units, Common.WEATHER_API_KEY).enqueue(new Callback<ForecastWeather>() {
            @Override
            public void onResponse(@NonNull Call<ForecastWeather> call, @NonNull Response<ForecastWeather> response) {
                if(response.body()!= null) {
                    forecastList = response.body().getList();
                    splitForecastOnDay(forecastList);
                    addToAdapter(forecastList);

                    tomorrowForecast.setText(forecastList.get(beginOfNewDay.get(0)).getDayOfWeek());
                    afterTomorrowForecast.setText(forecastList.get(beginOfNewDay.get(1)).getDayOfWeek());
                    afterThreeDaysForecast.setText(forecastList.get(beginOfNewDay.get(2)).getDayOfWeek());

                    dialog.dismiss();
                    mExpandLayout.toggle();
                }
                else
                {
                    ///TODO
                }
                // swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ForecastWeather> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }

    private void addToAdapter(java.util.List<List> forecastList) {
        adapter = new ListWeatherForecastAdapter(getBaseContext(), forecastList);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    private void splitForecastOnDay(java.util.List<List> forecastList) {
        for (int i = 0; i < forecastList.size(); i++) {
            String[] date = forecastList.get(i).getDtTxt().split(" "); //0 - date, 1 - time
            if (date[1].equals("00:00:00")) {
                beginOfNewDay.add(i + 1);
            }
        }
    }
}
