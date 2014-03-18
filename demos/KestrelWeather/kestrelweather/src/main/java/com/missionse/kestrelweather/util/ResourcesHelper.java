package com.missionse.kestrelweather.util;

import android.content.Context;
import android.content.res.TypedArray;

import com.missionse.kestrelweather.R;

/**
 * Provides utility functions to handle temperature resources.
 */
public final class ResourcesHelper {
	private static final int MAX_COLOR_INDEX = 24;
	private static final int TEMPERATURE_PADDING = 20;
	private static final float RANGE_PER_TEMPERATURE_COLOR = 2.4f;

	private ResourcesHelper() {
	}

	/**
	 * Gets the color of a temperature based on the celsius temperature value.
	 * @param context The current context.
	 * @param temperature The temperature in celsius.
	 * @return The color of the temperature.
	 */
	public static int getTemperatureColor(final Context context, final double temperature) {
		return getTemperatureColorByIndex(context, getTemperatureIndex(getTemperatureIndex(temperature)));
	}

	/**
	 * Gets the color of a temperature based on the index in the color array.
	 * @param context The current context.
	 * @param index The index into the temperature color array.
	 * @return The color of the temperature.
	 */
	public static int getTemperatureColorByIndex(final Context context, final int index) {
		int color = 0;
		TypedArray temperatureArray = context.getResources().obtainTypedArray(R.array.temperature_colors);
		if (temperatureArray != null) {
			color = temperatureArray.getColor(index, 0);
		}

		return color;
	}

	/**
	 * Gets the index of a temperature color based on the temperature in celsius.
	 * @param temperature The temperature in celsius.
	 * @return The index of the temperature color.
	 */
	public static int getTemperatureIndex(final double temperature) {
		return Math.min(MAX_COLOR_INDEX,
				Math.max((int) Math.floor((temperature + TEMPERATURE_PADDING) / RANGE_PER_TEMPERATURE_COLOR),
						0)
		);
	}
}
