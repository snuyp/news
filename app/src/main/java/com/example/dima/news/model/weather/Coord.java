package com.example.dima.news.model.weather;

/**
 * Created by Dima on 06.04.2018.
 */

public class Coord {
    private Double lon;
    private Double lat;

    public Coord() {
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }
}
