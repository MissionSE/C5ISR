package com.missionse.mapsexample.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class DateHelper {

    public static String RSS_DATE_FORMAT = "EEE, d MMM yyyy HH:mm:ss Z";

    public static Date optDate(String source, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(source);
        } catch (ParseException ex) {
            return null;
        }
    }

    public static Date optUTCDate(String source, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return sdf.parse(source);
        } catch (ParseException ex) {
            return null;
        }
    }

    public static String getDate(String source, String inputFormat, String outputFormat) throws ParseException {
        return new SimpleDateFormat(outputFormat).format(new SimpleDateFormat(inputFormat, Locale.US).parse(source));
    }

    public static String optDate(String source, String inputFormat, String outputFormat, String defaultValue) {
        try {
            return getDate(source, inputFormat, outputFormat);
        } catch (ParseException ex) {
            return defaultValue;
        }
    }
    public static String optDate(String source, String inputFormat, String outputFormat) {
        return optDate(source, inputFormat, outputFormat, null);
    }
}