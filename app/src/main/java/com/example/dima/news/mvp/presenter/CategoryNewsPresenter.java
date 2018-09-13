package com.example.dima.news.mvp.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.dima.news.Interface.NewsService;
import com.example.dima.news.common.Common;
import com.example.dima.news.mvp.model.news.Article;
import com.example.dima.news.mvp.model.news.News;
import com.example.dima.news.mvp.view.CategoryNewsView;
import com.example.dima.news.ui.adapter.ListNewsAdapter;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

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

            } else {
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
                    List<Article> articles = response.body().getArticles();
                    getViewState().onLoadResult(articles);
                    getViewState().setRefresh(false);
                } else {
                    //TODO:
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
        if (!isRefreshed) {
            getViewState().dialogShow();
            newsService.getHeadlines(source, Common.API_KEY)
                    .enqueue(new Callback<News>() {
                        @Override
                        public void onResponse(Call<News> call, Response<News> response) {
                            if (response.body() != null) {
                                List<Article> removeFirstArticle = response.body().getArticles();
                                removeFirstArticle.remove(0);
                                getViewState().onLoadResult(removeFirstArticle);
                                getViewState().dialogDismiss();
                            } else {
                                //todo
                            }
                        }

                        @Override
                        public void onFailure(Call<News> call, Throwable t) {

                        }
                    });
        }
        else
        {


        }
        getViewState().setRefresh(false);

    }
}
