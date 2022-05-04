package com.lubuteam.fakemessenger.fakeconversation.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static final String DATE_FORMAT_1 = "yyyy.MM.dd HH:mm";
    public static final String DATE_FORMAT_2 = "yyyy.MM.dd";
    public static final String DATE_FORMAT_3 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_4 = "yyyy/MM/dd";
    public static final String DATE_FORMAT_5 = "MM/dd/yyyy HH:mm:ss";
    public static final String DATE_FORMAT_6 = "MMM dd";
    public static final String DATE_FORMAT_7 = "MMM dd, yyyy";

    public static final String TIME_FORMAT_1 = "hh:mm a";
    public static final String TIME_FORMAT_2 = "HH:mm";

    private static final long ONE_DAY = 60 * 60 * 24 * 1000L;
    private static final int ONE_HOUR = 60 * 60 * 1000;
    private static final int ONE_MINUTE = 60 * 1000;

    public static String getStampByDate(Date date, String pattern) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
            return dateFormat.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    public static String longToDateString(long time, String fomat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(fomat);
            return sdf.format(time);
        } catch (Exception e) {
            return "";
        }
    }

}
