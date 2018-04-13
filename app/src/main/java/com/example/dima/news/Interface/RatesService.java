package com.example.dima.news.Interface;

import com.example.dima.news.model.rate.RateMain;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Dima on 12.04.2018.
 */

public interface RatesService {
    @GET("latest?")
    Call<RateMain> getRates(
            @Query("appid") String appid,
            @Query("base") String baseRates,
            @Query("symbols") String returnCurrency
    );

}
