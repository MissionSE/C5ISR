package com.missionse.kestrelweather.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.util.UnitConverter;

/**
 * Utility class for getting weather measurements in the preferred units.
 */
public final class UnitPrefs {

	private UnitPrefs() {
	}

	/**
	 * Get the measurement in the preferred unit for temperature.
	 * @param context the current context
	 * @param temperature the measured temperature in celsius
	 * @return temperature in the preferred units
	 */
	public static float getPreferredTemperature(Context context, float temperature) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String key = context.getString(R.string.key_units_temperature);
		String value = prefs.getString(key, context.getString(R.string.celsius));
		if (value.equals(context.getString(R.string.fahrenheit))) {
			return UnitConverter.celsiusToFahrenheit(temperature);
		} else {
			return temperature;
		}
	}

	/**
	 * Gets the preferred unit for temperature.
	 * @param context the current context
	 * @return the preferred unit for temperature
	 */
	public static String getPreferrredTemperatureUnit(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String key = context.getString(R.string.key_units_temperature);
		return prefs.getString(key, context.getString(R.string.celsius));
	}

	/**
	 * Gets the preferred abbreviated unit for temperature.
	 * @param context the current context
	 * @return the preferred unit for temperature
	 */
	public static String getPreferredTemperatureUnitAbbr(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String key = context.getString(R.string.key_units_temperature);
		String value = prefs.getString(key, context.getString(R.string.celsius));
		if (value.equals(context.getString(R.string.fahrenheit))) {
			return context.getString(R.string.fahrenheitAbbr);
		} else {
			return context.getString(R.string.celsiusAbbr);
		}
	}

	/**
	 * Get the measure in the preferred unit for pressure.
	 * @param context the current context
	 * @param pressure the measured pressure
	 * @return pressure in the preferred units
	 */
	public static float getPreferredPressure(Context context, float pressure) {
		return pressure;
	}

	/**
	 * Gets the preferred unit for pressure.
	 * @param context the current context
	 * @return the preferred unit for pressure
	 */
	public static String getPreferredPressureUnit(Context context) {
		return "Milibars (mb)";
	}

	/**
	 * Gets the preferred abbreviated unit for pressure.
	 * @param context the current context
	 * @return the preferred unit for pressure
	 */
	public static String getPreferredPressureUnitAbbr(Context context) {
		return "mb";
	}

	/**
	 * Get the measure in the preferred unit for wind speed.
	 * @param context the current context
	 * @param windSpeed the measured wind speed
	 * @return wind speed in the preferred units
	 */
	public static float getPreferredWindSpeed(Context context, float windSpeed) {
		return windSpeed;
	}

	/**
	 * Gets the preferred unit for wind speed.
	 * @param context the current context
	 * @return the preferred unit for wind speed
	 */
	public static String getPreferredWindSpeedUnit(Context context) {
		return "Meters per second (mps)";
	}

	/**
	 * Gets the preferred abbreviated unit for wind speed.
	 * @param context the current context
	 * @return the preferred unit for wind speed
	 */
	public static String getPreferredWindSpeedUnitAbbr(Context context) {
		return "mps";
	}

	/**
	 * Get the measure in the preferred unit for wind direction.
	 * @param context the current context
	 * @param windDirection the measured wind direction
	 * @return wind direction in the preferred units
	 */
	public static float getPreferredWindDirection(Context context, float windDirection) {
		return windDirection;
	}

	/**
	 * Gets the preferred unit for wind direction.
	 * @param context the current context
	 * @return the preferred unit for wind direction
	 */
	public static String getPreferredWindDirectionUnit(Context context) {
		return "Degrees";
	}

	/**
	 * Gets the preferred abbreviated unit for wind direction.
	 * @param context the current context
	 * @return the preferred unit for wind direction
	 */
	public static String getPreferredWindDirectionUnitAbbr(Context context) {
		return "°";
	}

}
