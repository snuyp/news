package com.example.dima.news.model.weather;

/**
 * Created by Dima on 06.04.2018.
 */

public class Main {
    private Double temp;
    private Double pressure;
    private Integer humidity;
    private Double tempMin;
    private Double tempMax;

    public Main() {
    }

    public String getTemp() {
        return String.valueOf(temp.intValue()) + "\u00B0";
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }


    public String getPressure() {
        return String.valueOf(pressure) + " hPa";
    }

    public String getHumidity() {
        return String.valueOf(humidity) + " %";
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public String getTempMin() {
        return  String.valueOf(tempMin.intValue()) + "\u00B0";
    }

    public void setTempMin(Double tempMin) {
        this.tempMin = tempMin;
    }

    public String getTempMax() {
        return  String.valueOf(tempMax.intValue()) + "\u00B0";
    }

    public void setTempMax(Double tempMax) {
        this.tempMax = tempMax;
    }
}
