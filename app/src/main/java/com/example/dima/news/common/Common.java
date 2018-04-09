package com.example.dima.news.common;

import android.support.annotation.NonNull;

import com.example.dima.news.Interface.IconBetterIdeaService;
import com.example.dima.news.Interface.NewsService;
import com.example.dima.news.Interface.WeatherService;
import com.example.dima.news.model.news.News;
import com.example.dima.news.remote.IconBetterIdeaClient;
import com.example.dima.news.remote.NewsClient;
import com.example.dima.news.remote.WeatherClient;


public class Common {
    private static final String BASE_URL = "https://newsapi.org/v2/";
    private static final String BASE_WEATHER_URL = "http://openweathermap.org/data/2.5/";

    public static final String API_KEY = "5e4c61bd768b476a9fc0e5c7e6b71eac";
    public static final String WEATHER_API_KEY = "b6907d289e10d714a6e88b30761fae22";
    public static String language = "ru";
    public static String city = "Minsk";
    public static String units = "metric";


    public static NewsService getNewsService()
    {
        return  NewsClient.getClient(BASE_URL).create(NewsService.class);
    }

    public static IconBetterIdeaService getIconService()
    {
        return  IconBetterIdeaClient.getClient().create(IconBetterIdeaService.class);
    }

    public static String getApiUrl(String source)
    {
        StringBuilder apiUrl= new StringBuilder("https://newsapi.org/v2/top-headlines?sources=");
        return apiUrl.append(source)
                .append("&apiKey=")
                .append(API_KEY)
                .toString();
    }

    public static WeatherService getWeatherService()
    {
        return WeatherClient.getClient(BASE_WEATHER_URL).create(WeatherService.class);
    }

}
