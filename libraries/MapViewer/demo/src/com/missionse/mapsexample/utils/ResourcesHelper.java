package com.missionse.mapsexample.utils;

import android.content.Context;

import com.missionse.mapsexample.R;

import java.util.Arrays;
import java.util.List;

public class ResourcesHelper {

    public static int getTemperatureColor(Context context, double temperature) {
        return getTemperatureColorByIndex(context, getTemperatureIndex(getTemperatureIndex(temperature)));
    }

    public static int getTemperatureColorByIndex(Context context, int index) {
        return context.getResources().obtainTypedArray(R.array.temperature_colors).getColor(index, 0);
    }

    public static int getTemperatureIndex(double temperature) {
        return Math.min(32, Math.max((int) Math.floor((temperature - 273.15 + 43) / 3), 0));
    }

//    public static String getWeatherCode(Context paramContext, int paramInt) {
//        return paramContext.getResources().getStringArray(R.array.weather_codes)[paramInt];
//    }
//
//    public static int getWeatherIndex(Context paramContext, String paramString) {
//        return Arrays.asList(paramContext.getResources().getStringArray(R.array.weather_codes)).indexOf(paramString);
//    }
//
//    public static int getWeatherOverlayColor(Context paramContext, int paramInt) {
//        return paramContext.getResources().obtainTypedArray(R.array.weather_overlay_colors).getColor(paramInt, 0);
//    }
//
//    public static String getWeatherTitle(Context paramContext, int paramInt) {
//        return paramContext.getResources().getStringArray(R.array.weather_titles)[paramInt];
//    }
//
//    public static List<String> getWeathersList(Context paramContext) {
//        return Arrays.asList(paramContext.getResources().getStringArray(R.array.weather_codes));
//    }
}
