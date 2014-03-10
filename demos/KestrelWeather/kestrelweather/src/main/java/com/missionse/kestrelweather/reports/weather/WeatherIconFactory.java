package com.missionse.kestrelweather.reports.weather;

import com.missionse.kestrelweather.R;

import java.util.TreeMap;

/**
 * Provides the functionality to quickly get the weather icon for a condition.
 */
public final class WeatherIconFactory {
	private static TreeMap<Integer, Integer> mWeatherIcons;

	static {
		mWeatherIcons = new TreeMap<Integer, Integer>();
		mWeatherIcons.put(0, R.drawable.weather_pict_storm);
		mWeatherIcons.put(300, R.drawable.weather_pict_light_rain);
		mWeatherIcons.put(502, R.drawable.weather_pict_heavy_rain);
		mWeatherIcons.put(511, R.drawable.weather_pict_rain_snow);
		mWeatherIcons.put(520, R.drawable.weather_pict_showers);
		mWeatherIcons.put(600, R.drawable.weather_pict_light_snow);
		mWeatherIcons.put(602, R.drawable.weather_pict_heavy_snow);
		mWeatherIcons.put(615, R.drawable.weather_pict_rain_snow);
		mWeatherIcons.put(620, R.drawable.weather_pict_light_snow);
		mWeatherIcons.put(621, R.drawable.weather_pict_heavy_snow);
		mWeatherIcons.put(741, R.drawable.weather_pict_fog);
		mWeatherIcons.put(800, R.drawable.weather_pict_hot);
		mWeatherIcons.put(801, R.drawable.weather_pict_fair);
		mWeatherIcons.put(802, R.drawable.weather_pict_cloudy);
		mWeatherIcons.put(804, R.drawable.weather_pict_overcast);
		mWeatherIcons.put(904, R.drawable.weather_pict_hot);
	}

	private WeatherIconFactory() {
	}

	/**
	 * Gets the drawable weather icon for a specified condition code.
	 * @param conditionCode The code representing the weather condition.
	 * @return The drawable resource id for an icon representing the weather condition.
	 */
	public static int getWeatherIcon(final int conditionCode) {
		return mWeatherIcons.get(mWeatherIcons.floorKey(conditionCode));
	}
}
