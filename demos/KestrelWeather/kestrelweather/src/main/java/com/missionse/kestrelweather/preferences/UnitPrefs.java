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
	public static String getPreferredTemperatureUnit(Context context) {
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
	 * @param kilopascal the measured pressure
	 * @return pressure in the preferred units
	 */
	public static float getPreferredPressure(Context context, float kilopascal) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String key = context.getString(R.string.key_units_pressure);
		String value = prefs.getString(key, context.getString(R.string.kilopascal));
		if (value.equals(context.getString(R.string.milibars))) {
			return UnitConverter.kilopascalToMilibar(kilopascal);
		} else if (value.equals(context.getString(R.string.inches_of_mercury))) {
			return UnitConverter.kilopascalToInchesOfMercury(kilopascal);
		} else {
			return kilopascal;
		}
	}

	/**
	 * Gets the preferred unit for pressure.
	 * @param context the current context
	 * @return the preferred unit for pressure
	 */
	public static String getPreferredPressureUnit(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String key = context.getString(R.string.key_units_pressure);
		return prefs.getString(key, context.getString(R.string.kilopascal));
	}

	/**
	 * Gets the preferred abbreviated unit for pressure.
	 * @param context the current context
	 * @return the preferred unit for pressure
	 */
	public static String getPreferredPressureUnitAbbr(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String key = context.getString(R.string.key_units_pressure);
		String value = prefs.getString(key, context.getString(R.string.kilopascal));
		if (value.equals(context.getString(R.string.milibars))) {
			return context.getString(R.string.milibarsAbbr);
		} else if (value.equals(context.getString(R.string.inches_of_mercury))) {
			return context.getString(R.string.inches_of_mercuryAbbr);
		} else {
			return context.getString(R.string.kilopascalAbbr);
		}
	}

	/**
	 * Get the measure in the preferred unit for wind speed.
	 * @param context the current context
	 * @param mps the measured wind speed
	 * @return wind speed in the preferred units
	 */
	public static float getPreferredWindSpeed(Context context, float mps) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String key = context.getString(R.string.key_units_wind_speed);
		String value = prefs.getString(key, context.getString(R.string.meters_per_second));
		if (value.equals(context.getString(R.string.kilometers_per_hour))) {
			return UnitConverter.mpsToKmph(mps);
		} else if (value.equals(context.getString(R.string.miles_per_hour))) {
			return UnitConverter.mpsToMph(mps);
		} else if (value.equals(context.getString(R.string.knots))) {
			return UnitConverter.mpsToKnots(mps);
		} else {
			return mps;
		}
	}

	/**
	 * Gets the preferred unit for wind speed.
	 * @param context the current context
	 * @return the preferred unit for wind speed
	 */
	public static String getPreferredWindSpeedUnit(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String key = context.getString(R.string.key_units_wind_speed);
		return prefs.getString(key, context.getString(R.string.meters_per_second));
	}

	/**
	 * Gets the preferred abbreviated unit for wind speed.
	 * @param context the current context
	 * @return the preferred unit for wind speed
	 */
	public static String getPreferredWindSpeedUnitAbbr(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String key = context.getString(R.string.key_units_wind_speed);
		String value = prefs.getString(key, context.getString(R.string.meters_per_second));
		if (value.equals(context.getString(R.string.kilometers_per_hour))) {
			return context.getString(R.string.kilometers_per_hourAbbr);
		} else if (value.equals(context.getString(R.string.miles_per_hour))) {
			return context.getString(R.string.miles_per_hourAbbr);
		} else if (value.equals(context.getString(R.string.knots))) {
			return context.getString(R.string.knotsAbbr);
		} else {
			return context.getString(R.string.meters_per_secondAbbr);
		}
	}

	/**
	 * Get the measure in the preferred unit for wind direction.
	 * @param context the current context
	 * @param cardinal the measured wind direction
	 * @return wind direction in the preferred units
	 */
	public static String getPreferredWindDirection(Context context, float cardinal) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String key = context.getString(R.string.key_units_wind_direction);
		String value = prefs.getString(key, context.getString(R.string.degrees));
		if (value.equals(context.getString(R.string.cardinal))) {
			return UnitConverter.degreeToCardinal(context, cardinal);
		} else {
			return Float.toString(cardinal);
		}
	}

	/**
	 * Gets the preferred unit for wind direction.
	 * @param context the current context
	 * @return the preferred unit for wind direction
	 */
	public static String getPreferredWindDirectionUnit(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String key = context.getString(R.string.key_units_wind_direction);
		return prefs.getString(key, context.getString(R.string.degrees));
	}

	/**
	 * Gets the preferred abbreviated unit for wind direction.
	 * @param context the current context
	 * @return the preferred unit for wind direction
	 */
	public static String getPreferredWindDirectionUnitAbbr(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String key = context.getString(R.string.key_units_wind_direction);
		String value = prefs.getString(key, context.getString(R.string.degrees));
		if (value.equals(context.getString(R.string.cardinal))) {
			return "";
		} else {
			return context.getString(R.string.degreesAbbr);
		}
	}

}
