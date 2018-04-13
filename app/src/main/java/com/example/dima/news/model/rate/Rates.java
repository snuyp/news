package com.example.dima.news.model.rate;

/**
 * Created by Dima on 12.04.2018.
 */

public class Rates {
    private String BYR;
    private String RUB;
    private String UAH; //Hryvnia
    private String USD;
    private String EUR;
    private String CNY; //Yuan Renminbi
    private String KZT; //Tenge

    public Rates() {
    }

    public Rates(String BYR, String RUB, String UAH, String USD, String EUR, String CNY, String KZT) {
        this.BYR = BYR;
        this.RUB = RUB;
        this.UAH = UAH;
        this.USD = USD;
        this.EUR = EUR;
        this.CNY = CNY;
        this.KZT = KZT;
    }

    public String getBYR() {
        return BYR;
    }

    public void setBYR(String BYR) {
        this.BYR = BYR;
    }

    public String getRUB() {
        return RUB;
    }

    public void setRUB(String RUB) {
        this.RUB = RUB;
    }

    public String getUAH() {
        return UAH;
    }

    public void setUAH(String UAH) {
        this.UAH = UAH;
    }

    public String getUSD() {
        return USD;
    }

    public void setUSD(String USD) {
        this.USD = USD;
    }

    public String getEUR() {
        return EUR;
    }

    public void setEUR(String EUR) {
        this.EUR = EUR;
    }

    public String getCNY() {
        return CNY;
    }

    public void setCNY(String CNY) {
        this.CNY = CNY;
    }

    public String getKZT() {
        return KZT;
    }

    public void setKZT(String KZT) {
        this.KZT = KZT;
    }
}
