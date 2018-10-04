package com.example.dima.news.mvp.model.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by Dima on 27.04.2018.
 */

public class ForecastWeather {
    @SerializedName("cod")
    @Expose
    private String cod;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("cnt")
    @Expose
    private Integer cnt;
    @SerializedName("list")
    @Expose
    private java.util.List<ForecastList> forecastLists = null;
    @SerializedName("city")
    @Expose
    private City city;

    private LinkedHashMap<String, List<ForecastList>> mapForecastOnTheDays = null;


    public ForecastWeather() {

    }

    public LinkedHashMap<String, List<ForecastList>> forecastOnTheDays() {
        if (mapForecastOnTheDays == null) {
            splitForecastOnDay();
        }
        return mapForecastOnTheDays;
    }

    public List<Float> tempOfDays(String day) {
        List<Float> t = new ArrayList<>();
        for (ForecastList forecastList : mapForecastOnTheDays.get(day)) {
            t.add(forecastList.getMain().getTemp().floatValue());
        }
        return t;
    }

    public List<Float> tempOfDays(int index) {
        List<ForecastList> s = mapForecastOnTheDays.get(mapForecastOnTheDays.keySet().toArray()[index]);
        return tempOfDays(s.get(0).getDayOfWeek());
    }
    public List<ForecastList> forecastOnTheDays (int index)
    {
        return  mapForecastOnTheDays.get(mapForecastOnTheDays.keySet().toArray()[index]);
    }

    private void splitForecastOnDay() {
        ArrayList<Integer> beginOfNewDay = beginOfTheNewDay();
        mapForecastOnTheDays = new LinkedHashMap<>();
        List<ForecastList> day = forecastLists.subList(0, beginOfNewDay.get(0)); //split forecast on the days
        String dayOfWeek = day.get(0).getDayOfWeek();
        mapForecastOnTheDays.put(dayOfWeek, day);

        day = forecastLists.subList(beginOfNewDay.get(0), beginOfNewDay.get(1));
        dayOfWeek = day.get(beginOfNewDay.get(0)).getDayOfWeek();
        mapForecastOnTheDays.put(dayOfWeek, day);

        day = forecastLists.subList(beginOfNewDay.get(1), beginOfNewDay.get(2));
        dayOfWeek = day.get(beginOfNewDay.get(0)).getDayOfWeek();
        mapForecastOnTheDays.put(dayOfWeek, day);

        day = forecastLists.subList(beginOfNewDay.get(2), beginOfNewDay.get(3));
        dayOfWeek = day.get(beginOfNewDay.get(0)).getDayOfWeek();
        mapForecastOnTheDays.put(dayOfWeek, day);

    }

    private ArrayList<Integer> beginOfTheNewDay() {
        ArrayList<Integer> beginOfNewDay = new ArrayList<>();
        for (int i = 0; i < forecastLists.size(); i++) {
            String[] date = forecastLists.get(i).getDtTxt().split(" "); //0 - date, 1 - time
            if (date[1].equals("00:00:00")) {
                beginOfNewDay.add(i + 1);
            }
        }
        return beginOfNewDay;
    }

    public List<ForecastList> getForecastLists() {
        return forecastLists;
    }

    public HashMap<String, List<ForecastList>> getMapForecastOnTheDays() {
        return mapForecastOnTheDays;
    }

    public void setForecastLists(List<ForecastList> forecastLists) {
        this.forecastLists = forecastLists;
    }


    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}