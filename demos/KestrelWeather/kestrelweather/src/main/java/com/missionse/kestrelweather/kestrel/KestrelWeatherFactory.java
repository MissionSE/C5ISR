package com.missionse.kestrelweather.kestrel;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.model.tables.KestrelWeather;

/**
 * Provides functionality to get Kestrel Weather data from Shared Preferences.
 */
public final class KestrelWeatherFactory {
	private KestrelWeatherFactory() {
	}

	/**
	 * Gets the saved KestrelWeather values from Shared Preferences.
	 * @param context The current context.
	 * @return The saved KestrelWeather data.
	 */
	public static KestrelWeather getSavedKestrelWeatherData(final Context context) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

		KestrelWeather kestrelWeather = new KestrelWeather();
		kestrelWeather.setTemperature(Float.valueOf(
				getPreference(context, sharedPreferences, R.string.key_simulated_temperature)));

		kestrelWeather.setHumidity(Integer.valueOf(
				getPreference(context, sharedPreferences, R.string.key_simulated_humidity)));

		kestrelWeather.setPressure(Float.valueOf(
				getPreference(context, sharedPreferences, R.string.key_simulated_pressure)));

		kestrelWeather.setPressureTrend(Integer.valueOf(
				getPreference(context, sharedPreferences, R.string.key_simulated_pressure_trend)));

		kestrelWeather.setHeatIndex(Float.valueOf(
				getPreference(context, sharedPreferences, R.string.key_simulated_heat_index)));

		kestrelWeather.setWindSpeed(Float.valueOf(
				getPreference(context, sharedPreferences, R.string.key_simulated_wind_speed)));

		kestrelWeather.setWindDirection(Integer.valueOf(
				getPreference(context, sharedPreferences, R.string.key_simulated_wind_dir)));

		kestrelWeather.setWindChill(Float.valueOf(
				getPreference(context, sharedPreferences, R.string.key_simulated_wind_chill)));

		kestrelWeather.setDewPoint(Float.valueOf(
				getPreference(context, sharedPreferences, R.string.key_simulated_dew_pt)));

		return kestrelWeather;
	}

	private static String getPreference(final Context context, final SharedPreferences sharedPreferences, final int key) {
		return sharedPreferences.getString(
				context.getString(key),
				Integer.toString(context.getResources().getInteger(R.integer.default_simulated_value)));
	}
}
