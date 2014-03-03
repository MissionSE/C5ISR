package com.missionse.kestrelweather.database.util;

import android.content.Context;

import com.missionse.kestrelweather.R;

/**
 * Created by Kyle on 2/28/14.
 */
public class ResourcesHelper {

	private ResourcesHelper() {
	}

	public static int getTemperatureColor(Context context, double temperature) {
		return getTemperatureColorByIndex(context, getTemperatureIndex(getTemperatureIndex(temperature)));
	}

	public static int getTemperatureColorByIndex(Context context, int index) {
		return context.getResources().obtainTypedArray(R.array.temperature_colors).getColor(index, 0);
	}

	public static int getTemperatureIndex(double temperature) {
		return Math.min(32, Math.max((int) Math.floor((temperature - 273.15 + 43) / 3), 0));
	}
}
