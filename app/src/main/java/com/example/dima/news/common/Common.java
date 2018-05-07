package com.example.dima.news.common;

import com.example.dima.news.Interface.NewsService;
import com.example.dima.news.Interface.RatesService;
import com.example.dima.news.Interface.WeatherService;
import com.example.dima.news.remote.NewsClient;
import com.example.dima.news.remote.RatesClient;
import com.example.dima.news.remote.WeatherClient;


public class Common {
    private static final String BASE_URL = "https://newsapi.org/v2/";
    private static final String BASE_WEATHER_URL = "http://api.openweathermap.org/data/2.5/";
    private static final String BASE_RATES_URL = "http://data.fixer.io/api/";

    public static final String API_KEY = "5e4c61bd768b476a9fc0e5c7e6b71eac";
    public static final String WEATHER_API_KEY = "6942f7ea25878b9d8540aa631b494d8c";
    public static final String RATE_KEY = "99626231aec43ac815d63ec6d1cf55a6";
    public static String units = "metric";


    public static NewsService getNewsService()
    {
        return  NewsClient.getClient(BASE_URL).create(NewsService.class);
    }


    public static String getApiUrl(String source)
    {
        StringBuilder apiUrl= new StringBuilder("https://newsapi.org/v2/top-headlines?sources=");
        return apiUrl.append(source)
                .append("&apiKey=")
                .append(API_KEY)
                .toString();
    }
    public static String getApiUrlSearch(String search,String fromDate,String toDate)
    {
        StringBuilder apiUrl= new StringBuilder("https://newsapi.org/v2/everything?q=");
        return apiUrl.append(search)
                .append("&from=")
                .append(fromDate)
                .append("&to=")
                .append(toDate)
                .append("&sortBy=popularity")
                .append("&apiKey=")
                .append(API_KEY)
                .toString();
//      for example
//      https://newsapi.org/v2/everything?q=apple&from=2018-04-09&to=2018-04-09&sortBy=popularity&apiKey=5e4c
    }

    public static WeatherService getWeatherService()
    {
        return WeatherClient.getClient(BASE_WEATHER_URL).create(WeatherService.class);
    }
    public static RatesService getRatesService()
    {
        return RatesClient.getClient(BASE_RATES_URL).create(RatesService.class);
    }
}
