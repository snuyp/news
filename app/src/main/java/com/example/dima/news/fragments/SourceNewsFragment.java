package com.example.dima.news.fragments;

import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.example.dima.news.adapter.ListSourceAdapter;
import com.example.dima.news.mvp.model.news.SourceNews;
import com.example.dima.news.mvp.presenter.SourcePresenter;
import com.example.dima.news.mvp.view.SourceView;

import java.util.List;

import dmax.dialog.SpotsDialog;

public class SourceNewsFragment extends MvpAppCompatFragment implements SourceView {
    private static SourceNewsFragment sourceNewsFragment = null;

    @InjectPresenter
    SourcePresenter sourcePresenter;

    SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView listWebsite;
    private RecyclerView.LayoutManager layoutManager;
    private ListSourceAdapter adapter;


    private SpotsDialog dialog;


    public static SourceNewsFragment getInstance() {
        if (sourceNewsFragment == null) {
            sourceNewsFragment = new SourceNewsFragment();
        }
        return sourceNewsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_source, container, false);

        final String languageSource = PreferenceManager.
                getDefaultSharedPreferences(getContext()).getString("select_language", "");

        listWebsite = v.findViewById(R.id.list_source);
        listWebsite.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        listWebsite.setLayoutManager(layoutManager);

        swipeRefreshLayout = v.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sourcePresenter.loadSources(languageSource,true);
            }
        });
        dialog = new SpotsDialog(getContext());
        sourcePresenter.loadSources(languageSource,true);
        return v;
    }

    @Override
    public void setRefresh(boolean isRefreshing) {
        swipeRefreshLayout.setRefreshing(isRefreshing);
    }

    @Override
    public void dialogShow() {
        dialog.show();
    }

    @Override
    public void dialogDismiss() {
        dialog.dismiss();
    }

    @Override
    public void onLoadResult(List<SourceNews> sources) {
        adapter = new ListSourceAdapter(getContext(), sources);
        listWebsite.setAdapter(adapter);
    }
}
