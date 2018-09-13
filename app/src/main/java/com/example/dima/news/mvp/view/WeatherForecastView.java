package com.example.dima.news.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.dima.news.mvp.presenter.WeatherForecastPresenter;

import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.Viewport;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface WeatherForecastView extends MvpView {
    void weatherView(ColumnChartData columnData);
    void chartTop(LineChartData lineData,Viewport v);
    void chartTopCancelDataAnimation();
    void chartStartDataAnimation(int duration);
    void error(String error);
    void dialogShow();
    void dialogDismiss();
}