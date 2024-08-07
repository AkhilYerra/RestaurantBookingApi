package com.example.resy.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static Date addTwoHours(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        return calendar.getTime();
    }

    public static Date createDate(String dateString){
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }
}
