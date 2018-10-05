package com.example.dima.news.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.dima.news.mvp.model.news.SourceNews;
import java.util.List;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface SourceView extends MvpView {
    void setRefresh(boolean isRefreshing);
    void error(String error);
    void dialogShow();
    void dialogDismiss();
    void onLoadResult(List<SourceNews> sources);
}
