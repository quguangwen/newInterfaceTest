package com.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String convertDate(){
        Date date = new Date();
        DateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String dateString = sd.format(date);
        return dateString;
    }
}
