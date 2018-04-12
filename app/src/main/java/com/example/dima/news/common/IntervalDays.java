package com.example.dima.news.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Dima on 11.04.2018.
 */

public class IntervalDays {
    private String today;
    private String daysAgo;
    private int interval;

    public IntervalDays(int daysAgo) {
        this.interval = daysAgo;
        int ago = 0;
        if(daysAgo > 0)
        {
            ago =  daysAgo * (-1);
        }

        Calendar instance = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd",new Locale("en","US"));

        Date date = new Date();
        instance.setTime(date); //устанавливаем дату, с которой будет производить операции
        instance.add(Calendar.DAY_OF_MONTH, ago);//отнимаем от тукещуй даты дни
        Date newDate = instance.getTime();

        this.today = dateFormat.format(date);
        this.daysAgo = dateFormat.format(newDate);
    }

    public String getToday() {
        return today;
    }

    public String getDaysAgo() {
        return daysAgo;
    }
}
