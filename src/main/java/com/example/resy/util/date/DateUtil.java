package com.example.resy.util.date;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static Date addTwoHours(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        return calendar.getTime();
    }
}
