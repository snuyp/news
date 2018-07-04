package com.example.dima.news.Interface;

import com.example.dima.news.model.news.News;
import com.example.dima.news.model.news.WebSite;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Dima on 30.03.2018.
 */

public interface NewsService {


    @GET("sources?")
    Call<WebSite> getSources(
        @Query("language") String language,
        @Query("apiKey") String apiKey
    );

    @GET("top-headlines")
    Call<News> getHeadlines(
            @Query("sources") String sources,
            @Query("apiKey") String apiKey
    );

    @GET("everything")
    Call<News> getSearch(
            @Query("q") String query,
            @Query("from") String fromDate,
            @Query("to") String toDate,
            @Query("sortBy") String sortBy,
            @Query("apiKey") String apiKey
    );

}
