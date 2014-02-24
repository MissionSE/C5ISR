package com.missionse.mapsexample;

import android.content.Context;
import android.util.Log;


public class Preferences {

    private Preferences() {}

    public static String getTemperatureUnit(Context context) {
        //TODO
        String value = context.getResources().getStringArray(R.array.temperature_units)[0];
        Log.d("Preferences", "temperature_unit[0]=" + value);
        return value;
    }
}
