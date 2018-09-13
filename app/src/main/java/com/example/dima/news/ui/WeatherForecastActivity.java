package com.example.dima.news.ui;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.dima.news.R;
import com.example.dima.news.mvp.model.weather.ForecastList;
import com.example.dima.news.mvp.presenter.WeatherForecastPresenter;
import com.example.dima.news.mvp.view.WeatherForecastView;
import com.example.dima.news.ui.adapter.ListWeatherForecastAdapter;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Dima on 02.05.2018.
 */

public class WeatherForecastActivity extends MvpAppCompatActivity implements WeatherForecastView{

    @InjectPresenter
    WeatherForecastPresenter weatherForecastPresenter;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private ListWeatherForecastAdapter adapter;
    private LineChartView chartTop;
    private ColumnChartView chartBottom;
    private SpotsDialog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_forecast);
        String city = PreferenceManager.getDefaultSharedPreferences(this).getString("city", "");

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitle(R.string.weather_forecast);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        chartTop = findViewById(R.id.chart_top);
        weatherForecastPresenter.getWeatherForecast(city);
        //weatherForecastPresenter.generateInitialLineData(getResources().getStringArray(R.array.days));
        chartBottom = findViewById(R.id.chart_bottom);
        //weatherForecastPresenter.generateColumnData(getResources().getStringArray(R.array.days),city);
        dialog = new SpotsDialog(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void supportNavigateUpTo(@NonNull Intent upIntent) {
        super.supportNavigateUpTo(upIntent);
    }

    public void weatherView(java.util.List<ForecastList> forecastList) {
        adapter = new ListWeatherForecastAdapter(getBaseContext(), forecastList);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void weatherView(ColumnChartData columnData) {
        chartBottom.setColumnChartData(columnData);
        // Set value touch listener that will trigger changes for chartTop.
        chartBottom.setOnValueTouchListener(new ColumnChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
                weatherForecastPresenter.generateLineData(columnIndex, value.getColor(), 100);
            }

            @Override
            public void onValueDeselected() {
                weatherForecastPresenter.generateLineData(0, ChartUtils.COLOR_GREEN, 100);
            }
        });


        // Set selection mode to keep selected month column highlighted.
        chartBottom.setValueSelectionEnabled(true);
        chartBottom.setZoomEnabled(false);
        chartBottom.setZoomType(ZoomType.HORIZONTAL);
    }


    @Override
    public void chartTop(LineChartData lineData,Viewport v) {

        chartTop.setLineChartData(lineData);

        // For build-up animation you have to disable viewport recalculation.
        chartTop.setViewportCalculationEnabled(false);

        // And set initial max viewport and current viewport- remember to set viewports after data.
        chartTop.setMaximumViewport(v);
        chartTop.setCurrentViewport(v);

        chartTop.setZoomType(ZoomType.HORIZONTAL);

    }

    @Override
    public void chartTopCancelDataAnimation() {
        chartTop.cancelDataAnimation();
    }

    @Override
    public void chartStartDataAnimation(int duration) {
        chartTop.startDataAnimation(duration);
    }

    @Override
    public void error(String error) {
        Toasty.error(getApplicationContext(),error).show();
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
