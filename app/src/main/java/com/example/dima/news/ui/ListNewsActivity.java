package com.example.dima.news.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bumptech.glide.Glide;
import com.example.dima.news.R;
import com.example.dima.news.mvp.model.news.Article;
import com.example.dima.news.mvp.presenter.CategoryNewsPresenter;
import com.example.dima.news.mvp.view.CategoryNewsView;
import com.example.dima.news.ui.adapter.ListNewsAdapter;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.florent37.diagonallayout.DiagonalLayout;

import java.util.List;

import dmax.dialog.SpotsDialog;

public class ListNewsActivity extends MvpAppCompatActivity implements CategoryNewsView{
    @InjectPresenter
    CategoryNewsPresenter categoryNewsPresenter;

    private AlertDialog dialog;
    private TextView topAuthor;
    private TextView topTitle;

    private SwipeRefreshLayout swipeRefreshLayout;
    private KenBurnsView kenBurnsView;
    private DiagonalLayout diagonalLayout;

    private ListNewsAdapter adapter;
    private RecyclerView lstNews;
    private RecyclerView.LayoutManager layoutManager;

    private String source = "";
    private String webHotURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_news);

        dialog = new SpotsDialog(this);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_news);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                categoryNewsPresenter.loadNewsOfSource(source, true);
            }
        });

        diagonalLayout = findViewById(R.id.diagonal_layout);
        diagonalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), DetailArticle.class);
                intent.putExtra("webURL", webHotURL);
                startActivity(intent);
            }
        });

        kenBurnsView = findViewById(R.id.top_image);
        topAuthor = findViewById(R.id.top_author);
        topTitle = findViewById(R.id.top_title);

        lstNews = findViewById(R.id.lst_news);
        lstNews.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        lstNews.setLayoutManager(layoutManager);

        if (getIntent() != null) {
            source = getIntent().getStringExtra("source");
            if (!source.isEmpty()) {
                categoryNewsPresenter.loadNewsOfSource(source, false);
            }
        }
    }

    @Override
    public void setRefresh(boolean isRefreshing) {
        swipeRefreshLayout.setRefreshing(isRefreshing);
    }

    @Override
    public void onLoadResult(List<Article> articles) {
        Glide.with(getBaseContext())
                .load(articles.get(0).getUrlToImage())
                .into(kenBurnsView);

        if(String.valueOf(articles.get(0).getTitle().charAt(0)).equals("�")) {
            topTitle.setText(getString(R.string.title_not_found));
        }else {
            topTitle.setText(articles.get(0).getTitle());
        }

        topAuthor.setText(articles.get(0).getAuthor());
        webHotURL = articles.get(0).getUrl();
        articles.remove(0);
        adapter = new ListNewsAdapter(articles);
        lstNews.setAdapter(adapter);
    }

    @Override
    public void error(String error) {

    }

    @Override
    public void dialogShow() {
        dialog.show();
    }

    @Override
    public void dialogDismiss() {
        dialog.dismiss();
    }

}