package com.example.dima.news.Interface;

import com.example.dima.news.model.weather.CurrentWeather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Dima on 06.04.2018.
 */

public interface WeatherService {
    @GET("weather?")
    Call<CurrentWeather> getToday(
            @Query("q") String city,
            @Query("units") String units,
            @Query("appid") String appid
    );

//    @GET("forecast")
//    Call<WeatherForecast> getForecast(
//            @Query("lat") Double lat,
//            @Query("lon") Double lon,
//            @Query("units") String units,
//            @Query("appid") String appid
//    );
}
