package com.example.dima.news.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dima on 30.03.2018.
 */

public class NewsClient {
    private static Retrofit retrofit = null;
    public static Retrofit getClient(String baseUrl)
    {
        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
