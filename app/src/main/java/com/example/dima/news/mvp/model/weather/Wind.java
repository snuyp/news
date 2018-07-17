package com.example.dima.news.mvp.model.weather;

/**
 * Created by Dima on 06.04.2018.
 */

public class Wind {
    private Double speed;
    private Double deg;

    public String getSpeed() {
        return String.valueOf(speed) + " m/s";
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getDeg() {
        return deg;
    }

    public void setDeg(Double deg) {
        this.deg = deg;
    }
}
