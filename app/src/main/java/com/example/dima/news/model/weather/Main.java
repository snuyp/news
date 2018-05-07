package com.example.dima.news.model.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dima on 06.04.2018.
 */

public class Main {
    @SerializedName("temp")
    @Expose
    public Double temp;
    @SerializedName("temp_min")
    @Expose
    public Double tempMin;
    @SerializedName("temp_max")
    @Expose
    public Double tempMax;
    @SerializedName("pressure")
    @Expose
    public Double pressure;
    @SerializedName("sea_level")
    @Expose
    public Double seaLevel;
    @SerializedName("grnd_level")
    @Expose
    public Double grndLevel;
    @SerializedName("humidity")
    @Expose
    public Integer humidity;

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

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public Double getSeaLevel() {
        return seaLevel;
    }

    public void setSeaLevel(Double seaLevel) {
        this.seaLevel = seaLevel;
    }

    public Double getGrndLevel() {
        return grndLevel;
    }

    public void setGrndLevel(Double grndLevel) {
        this.grndLevel = grndLevel;
    }

}
