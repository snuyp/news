package com.example.dima.news.mvp.presenter;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.dima.news.Interface.NewsService;
import com.example.dima.news.common.Common;
import com.example.dima.news.common.IntervalDays;
import com.example.dima.news.mvp.model.news.News;
import com.example.dima.news.mvp.model.news.WebSite;
import com.example.dima.news.mvp.view.SourceView;
import com.google.gson.Gson;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@InjectViewState
public class SourcePresenter extends MvpPresenter<SourceView> {
    private NewsService newsService = Common.getNewsService();

    public void loadSources(String languageSource, boolean isRefreshed) {

        if (!isRefreshed) {
            String cache = Paper.book().read("cache");
            if (cache != null && !cache.isEmpty() && !cache.equals("null")) {
                WebSite webSite = new Gson().fromJson(cache, WebSite.class);
                getViewState().onLoadResult(webSite.getSources());

            } else {
                getViewState().dialogShow();
                newsService.getSources(languageSource, Common.API_KEY).enqueue(new Callback<WebSite>() {
                    @Override
                    public void onResponse(@NonNull Call<WebSite> call, @NonNull Response<WebSite> response) {
                        if(response.body()!=null) {
                            getViewState().onLoadResult(response.body().getSources());
                            //Save to cache
                            Paper.book().write("cache", new Gson().toJson(response.body()));
                            getViewState().dialogDismiss();
                        }
                        else
                        {
                            Log.e("Failure", "Failure");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WebSite> call, @NonNull Throwable t) {
                        Log.e("Failure", "Failure" + t.getMessage());
                    }
                });

            }
        } else {
            getViewState().setRefresh(true);

            newsService.getSources(languageSource, Common.API_KEY).enqueue(new Callback<WebSite>() {
                @Override
                public void onResponse(@NonNull Call<WebSite> call, @NonNull Response<WebSite> response) {
                    getViewState().onLoadResult(response.body().getSources());

                    //Save to cache
                    Paper.book().write("cache", new Gson().toJson(response.body()));

                    getViewState().setRefresh(false);
                }

                @Override
                public void onFailure(@NonNull Call<WebSite> call, @NonNull Throwable t) {
                    Log.e("Failure", "Failure" + t.getMessage());
                }
            });

        }
    }

    public void loadSearchArticles(Context context, String search) {
        int agoDay = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("search_articles", ""));
        IntervalDays intervalDays = new IntervalDays(agoDay);
        String today = intervalDays.getToday();
        String daysAgo = intervalDays.getDaysAgo();
        newsService.getSearch(search, daysAgo, today, "popularity", Common.API_KEY).enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
//                newsAdapter = new ListNewsAdapter(response.body().getArticles(), getBaseContext());
//                newsAdapter.notifyDataSetChanged();
//                listWebsite.setAdapter(newsAdapter);
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {

            }
        });
    }
}
