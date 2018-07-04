package com.example.dima.news.common;

import com.example.dima.news.Interface.NewsService;
import com.example.dima.news.Interface.WeatherService;
import com.example.dima.news.remote.NewsClient;
import com.example.dima.news.remote.WeatherClient;


public class Common {
    private static final String BASE_URL = "https://newsapi.org/v2/";
    private static final String BASE_WEATHER_URL = "http://api.openweathermap.org/data/2.5/";

    public static final String API_KEY = "5e4c61bd768b476a9fc0e5c7e6b71eac";
    public static final String WEATHER_API_KEY = "6942f7ea25878b9d8540aa631b494d8c";
    public static String units = "metric";


    public static NewsService getNewsService()
    {
        return  NewsClient.getClient(BASE_URL).create(NewsService.class);
    }

    public static WeatherService getWeatherService()
    {
        return WeatherClient.getClient(BASE_WEATHER_URL).create(WeatherService.class);
    }

}
