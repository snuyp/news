package com.example.dima.news.Interface;

import com.example.dima.news.mvp.model.weather.CurrentWeather;
import com.example.dima.news.mvp.model.weather.ForecastWeather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface WeatherService {
    @GET("weather?")
    Call<CurrentWeather> getToday(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("units") String units,
            @Query("appid") String appid
    );

    @GET("forecast?")
    Call<ForecastWeather> getForecast(
            @Query("lat") double q,
            @Query("lon") double lon,
            @Query("units") String units,
            @Query("appid") String appid
    );
}
