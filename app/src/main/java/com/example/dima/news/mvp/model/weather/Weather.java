package com.example.dima.news.mvp.model.weather;

/**
 * Created by Dima on 06.04.2018.
 */

public class Weather {
    private Integer id;
    private String main;
    private String description;
    private String icon;

    public Weather() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getIcon() {
        return "http://openweathermap.org/img/w/" + icon + ".png";
    }
}
