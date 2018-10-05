package com.example.dima.news.mvp.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.dima.news.Interface.WeatherService;
import com.example.dima.news.common.Common;
import com.example.dima.news.mvp.model.weather.ForecastList;
import com.example.dima.news.mvp.model.weather.ForecastWeather;
import com.example.dima.news.mvp.view.WeatherForecastView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@InjectViewState
public class WeatherForecastPresenter extends MvpPresenter<WeatherForecastView> {
    private WeatherService weatherService = Common.getWeatherService();

    private ForecastWeather forecastWeather;
    private LineChartData lineData;
    private LinkedHashMap<String, List<ForecastList>> mapForecastOnTheDays;
    public String[] time = new String[]{"03:00", "06:00", "09:00", "12:00", "15:00", "18:00","21:00","24:00"};


    public void getWeatherForecast(String city) {
        if(forecastWeather == null) {
            getViewState().dialogShow();
            weatherService.getForecast(city, Common.units, Common.WEATHER_API_KEY).enqueue(new Callback<ForecastWeather>() {
                @Override
                public void onResponse(@NonNull Call<ForecastWeather> call, @NonNull Response<ForecastWeather> response) {
                    if (response.body() != null) {
                        forecastWeather = response.body();
                        mapForecastOnTheDays = forecastWeather.forecastOnTheDays();
                        generateColumnData();
                        getViewState().dialogDismiss();
                    } else {
                        getViewState().error("Failure");
                    }
                }

                @Override
                public void onFailure(Call<ForecastWeather> call, Throwable t) {
                    Log.e("Error", t.getMessage());
                    getViewState().error(t.getMessage());
                }
            });
        }
    }

    private void generateColumnData() {

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        int i = 0;
        for (String day : mapForecastOnTheDays.keySet()) {

            values = new ArrayList<>();

            List<Float> temp = forecastWeather.tempOfDays(day);
            float tMax = Collections.max(temp);
            float tMin = Collections.min(temp);
            int color = ChartUtils.nextColor();
            values.add(new SubcolumnValue(tMin, color));
            values.add(new SubcolumnValue(tMax, color).setValue(tMax));
            axisValues.add(new AxisValue(i).setLabel(day));

            columns.add(new Column(values).setHasLabels(true));

            i++;
        }

        ColumnChartData columnData = new ColumnChartData(columns);

        columnData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        columnData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(2));
        getViewState().chartBottomView(columnData);

        generateInitialLineData(time);
    }


    private void generateInitialLineData(String[] days) {
        int numValues = days.length;

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<PointValue> values = new ArrayList<PointValue>();
        for (int i = 0; i < numValues; ++i) {
            values.add(new PointValue(i, 0));
            axisValues.add(new AxisValue(i).setLabel(days[i]));
        }

        Line line = new Line(values);
        line.setColor(ChartUtils.COLOR_GREEN).setCubic(true);

        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        lineData = new LineChartData(lines);
        lineData.setAxisXBottom(new Axis(axisValues).setHasLines(true).setName("Hours"));
        lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));

        Viewport viewPort = new Viewport(0,30,7,-30);
        getViewState().chartTop(lineData,viewPort);
    }

    public void generateLineData(int columnIndex, int color, float range) {
        // Cancel last animation if not finished.
        getViewState().chartTopCancelDataAnimation();
        // Modify data targets
        Line line = lineData.getLines().get(0);
        line.setColor(color);
        line.setHasLabels(true);

        getViewState().onLoadResult(forecastWeather.forecastOnTheDays(columnIndex));
        List<Float> tempOfDays = forecastWeather.tempOfDays(columnIndex);
        int i = 0;
        int difference = line.getValues().size() - tempOfDays.size();

        for (PointValue value : line.getValues()) {
            // Change target only for Y value.
            if(difference !=0 )
            {
                difference--;
                value.setTarget(value.getX(),tempOfDays.get(0));
                value.setLabel("-");
            }
            else {
                value.setTarget(value.getX(), tempOfDays.get(i));
                value.setLabel(String.valueOf(tempOfDays.get(i).intValue()));
                i++;
            }
        }

        getViewState().chartTop(lineData,
                new Viewport(0,Collections.max(tempOfDays)+2,7,Collections.min(tempOfDays)-2));

        // Start new data animation with 300ms duration;
        getViewState().chartStartDataAnimation(300);
    }

}
