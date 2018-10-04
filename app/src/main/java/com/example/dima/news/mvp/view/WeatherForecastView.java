package com.example.dima.news.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.dima.news.mvp.model.news.SourceNews;
import com.example.dima.news.mvp.model.weather.ForecastList;
import com.example.dima.news.mvp.model.weather.ForecastWeather;
import com.example.dima.news.mvp.presenter.WeatherForecastPresenter;

import java.util.List;

import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.Viewport;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface WeatherForecastView extends MvpView {
    void chartBottomView(ColumnChartData columnData);
    void chartTop(LineChartData lineData,Viewport v);
    void chartTopCancelDataAnimation();
    void chartStartDataAnimation(int duration);
    void onLoadResult(List<ForecastList> forecast);
    void error(String error);
    void dialogShow();
    void dialogDismiss();
}