package com.example.dima.news.mvp.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.dima.news.Interface.WeatherService;
import com.example.dima.news.common.Common;
import com.example.dima.news.mvp.model.weather.CurrentWeather;
import com.example.dima.news.mvp.view.WeatherView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@InjectViewState
public class WeatherPresenter  extends MvpPresenter<WeatherView> {
    WeatherService weatherService = Common.getWeatherService();
    public void loadWeather(String city) {
        getViewState().dialogShow();

        weatherService.getToday(city, Common.units, Common.WEATHER_API_KEY).enqueue(new Callback<CurrentWeather>() {
            @Override
            public void onResponse(@NonNull Call<CurrentWeather> call, @NonNull Response<CurrentWeather> response) {
                CurrentWeather currentWeather = null;
                if (response.body() != null) {
                    getViewState().weatherView(response.body());
                    getViewState().dialogDismiss();
                }
                else
                {
                    getViewState().error();
                    Log.e("ERROR","Response error");
                }
                getViewState().dialogDismiss();
            }

            @Override
            public void onFailure(Call<CurrentWeather> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });

    }
}
