package com.example.resy.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

    public static LocalDateTime addTwoHours(LocalDateTime dateTime) {
        if (dateTime == null) {
            throw new IllegalArgumentException("LocalDateTime cannot be null");
        }
        return dateTime.plusHours(2);
    }

    public static String getFormattedDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            throw new IllegalArgumentException("LocalDateTime cannot be null");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy'T'HH:mm");
        return dateTime.format(formatter);
    }

}
