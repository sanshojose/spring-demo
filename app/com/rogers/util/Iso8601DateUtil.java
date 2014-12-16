package com.rogers.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Iso8601DateUtil {


    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");

    static{
        TimeZone tz = TimeZone.getTimeZone("UTC");
        df.setTimeZone(tz);
    }

    public static String dateToIso8601(Date date){
        String dateAsISO = df.format(date);
        return dateAsISO ;
    }

    public static String currentDateToIso8601(){
        String nowAsISO = df.format(new Date());
        return nowAsISO;
    }

    public static Date currentDatePlusMillis(Long millisecondsToExpiry) {
        return new Date(new Date().getTime() + millisecondsToExpiry);
    }

    public static String currentDatePlusMillisIso8601(Long millisecondsToExpiry) {
        Date date = new Date(new Date().getTime() + millisecondsToExpiry);
        return dateToIso8601(date);
    }


}
