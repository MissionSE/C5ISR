package com.missionse.kestrelweather.util;

import android.content.Context;

import com.missionse.kestrelweather.R;

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
		return Math.min(24, Math.max((int) Math.floor((temperature + 20) / 2.4), 0));
	}
}
