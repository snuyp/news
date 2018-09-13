package com.example.dima.news.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.dima.news.R;
import com.example.dima.news.ui.adapter.ListNewsAdapter;
import com.example.dima.news.mvp.model.news.Article;
import com.example.dima.news.mvp.presenter.CategoryNewsPresenter;
import com.example.dima.news.mvp.view.CategoryNewsView;

import java.util.List;

public class SportsNewsFragment extends MvpAppCompatFragment implements CategoryNewsView {
    private static SportsNewsFragment sportsNewsFragment = null ;

    @InjectPresenter
    CategoryNewsPresenter categoryNewsPresenter;


    SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView lstNews;
    private ListNewsAdapter adapter;

    private final String country = "ru"; //todo: edit later
    private final String category = "sports";

    public static SportsNewsFragment getInstance() {
        if (sportsNewsFragment == null) {
            sportsNewsFragment = new SportsNewsFragment();
        }
        return sportsNewsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category,container,false);

        lstNews = v.findViewById(R.id.lst_news);
        lstNews.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        lstNews.setLayoutManager(layoutManager);

        swipeRefreshLayout = v.findViewById(R.id.swipe_refresh_news);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                categoryNewsPresenter.categoryNews(country,category,true);
            }
        });
        categoryNewsPresenter.categoryNews(country,category,true);
        return v;
    }

    @Override
    public void setRefresh(boolean isRefreshing) {
        swipeRefreshLayout.setRefreshing(isRefreshing);
    }

    @Override
    public void onLoadResult(List<Article> articles) {
        adapter = new ListNewsAdapter(articles);
        lstNews.setAdapter(adapter);
    }

    @Override
    public void dialogShow() {

    }

    @Override
    public void dialogDismiss() {

    }
}
