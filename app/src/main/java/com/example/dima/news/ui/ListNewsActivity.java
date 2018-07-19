package com.example.dima.news.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.dima.news.Interface.NewsService;
import com.example.dima.news.R;
import com.example.dima.news.ui.adapter.ListNewsAdapter;
import com.example.dima.news.common.Common;
import com.example.dima.news.mvp.model.news.Article;
import com.example.dima.news.mvp.model.news.News;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.florent37.diagonallayout.DiagonalLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListNewsActivity extends AppCompatActivity {


    private AlertDialog dialog;
    private TextView topAuthor;
    private TextView topTitle;

    private SwipeRefreshLayout swipeRefreshLayout;
    private KenBurnsView kenBurnsView;
    private DiagonalLayout diagonalLayout;

    private ListNewsAdapter adapter;
    private RecyclerView lstNews;
    private NewsService service;
    private RecyclerView.LayoutManager layoutManager;

    private String source = "";
    private String webHotURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_news);

        service = Common.getNewsService();

        dialog = new SpotsDialog(this);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_news);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNews(source, true);
            }
        });

        diagonalLayout = findViewById(R.id.diagonal_layout);
        diagonalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ListNewsActivity.class);
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
                loadNews(source, false);
            }
        }
    }

    private void loadNews(String source, boolean isRefreshed) {
        if (!isRefreshed) {
            dialog.show();
            service.getHeadlines(source, Common.API_KEY)
                    .enqueue(new Callback<News>() {
                        @Override
                        public void onResponse(Call<News> call, Response<News> response) {
                            dialog.dismiss();
                            Picasso.with(getBaseContext())
                                    .load(response.body().getArticles().get(0).getUrlToImage())
                                    .into(kenBurnsView);
                            topTitle.setText(response.body().getArticles().get(0).getTitle());
                            topAuthor.setText(response.body().getArticles().get(0).getAuthor());
                            webHotURL = response.body().getArticles().get(0).getUrl();

                            List<Article> removeFirstArticle = response.body().getArticles();
                            removeFirstArticle.remove(0);
                            adapter = new ListNewsAdapter(removeFirstArticle);
                            adapter.notifyDataSetChanged();
                            lstNews.setAdapter(adapter);
                        }

                        @Override
                        public void onFailure(Call<News> call, Throwable t) {

                        }
                    });
        }
        //        else{
        //            dialog.show();
        //            service.getHeadlines(Common.getApiUrl(source))
        //                    .enqueue(new Callback<News>() {
        //                        @Override
        //                        public void onResponse(Call<News> call, Response<News> response) {
        //
        //                        }
        //
        //                        @Override
        //                        public void onFailure(Call<News> call, Throwable t) {
        //
        //                        }
        //                    });
        //
        //        }
        swipeRefreshLayout.setRefreshing(false);
    }
}