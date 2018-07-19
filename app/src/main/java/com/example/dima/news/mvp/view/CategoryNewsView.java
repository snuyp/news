package com.example.dima.news.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.dima.news.mvp.model.news.Article;

import java.util.List;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface CategoryNewsView extends MvpView{
    void setRefresh(boolean isRefreshing);
    void onLoadResult(List<Article> articles);
}
