package com.example.datospersonalistaalmacen.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String getDateString(Date d){
        String pattern = "dd MMM yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(d);
    }

    public static Date fromDateString(String dateString){
        String pattern = "dd MMM yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }
}
