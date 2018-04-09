package com.example.dima.news.model.weather;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Dima on 06.04.2018.
 */

public class Sys {
    private Integer type;
    private Integer id;
    private Double message;
    private String country;
    private Integer sunrise;
    private Integer sunset;

    public Sys() {
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getMessage() {
        return message;
    }

    public void setMessage(Double message) {
        this.message = message;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSunset()
    {
        return "↓ "+ dateText(sunset);
    }
    public String getSunrise()
    {
        return "↑ " + dateText(sunrise);
    }
    public void setSunrise(Integer sunrise) {
        this.sunrise = sunrise;
    }
    public void setSunset(Integer sunset) {
        this.sunset = sunset;
    }
    private String dateText(long dateText)
    {
        Date date=new Date(dateText * 1000);
        SimpleDateFormat df2 = new SimpleDateFormat("HH:mm ",new Locale("en","US"));
        return  df2.format(date);
    }
}
