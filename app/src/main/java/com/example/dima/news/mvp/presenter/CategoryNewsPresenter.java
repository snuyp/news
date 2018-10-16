package com.example.dima.news.mvp.presenter;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.dima.news.Interface.NewsService;
import com.example.dima.news.common.Common;
import com.example.dima.news.common.IntervalDays;
import com.example.dima.news.mvp.model.news.Article;
import com.example.dima.news.mvp.model.news.News;
import com.example.dima.news.mvp.view.CategoryNewsView;
import com.google.gson.Gson;

import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@InjectViewState
public class CategoryNewsPresenter extends MvpPresenter<CategoryNewsView> {
    private NewsService newsService = Common.getNewsService();

    public void categoryNews(String country, String category, boolean isRefreshed) {
        if (!isRefreshed) {
            String cache = Paper.book().read(category);
            if (cache != null && !cache.isEmpty() && !cache.equals("null")) {
                News news = new Gson().fromJson(cache, News.class);
                getViewState().onLoadResult(news.getArticles());

            } else  {
                loadCategory(country, category);
            }
        } else {
            loadCategory(country, category);
        }
    }

    private void loadCategory(String country, String category) {
        getViewState().setRefresh(true);
        newsService.getCategory(country, category, Common.API_KEY).enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.body() != null) {
                    News news = response.body();
                    //Save to cache
                    Paper.book().write(category, new Gson().toJson(response.body()));
                    getViewState().onLoadResult(news.getArticles());
                    getViewState().setRefresh(false);
                } else {
                    getViewState().error("Failure");
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.e("Failure", "Failure" + t.getMessage());
                getViewState().setRefresh(false);
            }
        });
    }

    public void loadNewsOfSource(String source, boolean isRefreshed) {
        if (!isRefreshed ) {
            getViewState().dialogShow();
            newsService.getHeadlines(source, Common.API_KEY)
                    .enqueue(new Callback<News>() {
                        @Override
                        public void onResponse(Call<News> call, Response<News> response) {
                            if (response.body() != null) {
                                List<Article>removeFirstArticle = response.body().getArticles();
                                getViewState().onLoadResult(removeFirstArticle);
                                getViewState().dialogDismiss();
                                getViewState().setRefresh(false);
                            } else {
                                getViewState().error("Error");
                            }
                        }

                        @Override
                        public void onFailure(Call<News> call, Throwable t) {
                            getViewState().error(t.getMessage());
                        }
                    });
        }
        getViewState().setRefresh(false);
    }

    public void loadSearchArticles(Context context, String search) {
        int agoDay = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("search_articles", "3"));
        IntervalDays intervalDays = new IntervalDays(agoDay);
        String today = intervalDays.getToday();
        String daysAgo = intervalDays.getDaysAgo();
        newsService.getSearch(search, daysAgo, today, "popularity", Common.API_KEY).enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.body() != null) {
                    getViewState().onLoadResult(response.body().getArticles());
                } else {
                    getViewState().error("Error");
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                getViewState().error(t.getMessage());
            }
        });
    }
}
