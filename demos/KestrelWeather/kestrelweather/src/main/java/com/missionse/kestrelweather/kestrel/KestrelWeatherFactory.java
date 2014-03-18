package com.missionse.kestrelweather.kestrel;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.model.tables.KestrelWeather;
import com.missionse.kestrelweather.util.UnitConverter;

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

	/**
	 * Parses the current open weather data response into a KestrelWeather object.
	 * @param openWeatherJson The response from OpenWeather with the current conditions.
	 * @return The weather data with the current conditions.
	 */
	public static KestrelWeather getCurrentWeatherData(final JsonObject openWeatherJson) {
		KestrelWeather kestrelWeather = new KestrelWeather();

		JsonObject mainData = openWeatherJson.getAsJsonObject("main");
		if (mainData != null) {
			JsonElement temperature = mainData.get("temp");
			if (temperature != null) {
				kestrelWeather.setTemperature(UnitConverter.kelvinToCelsius(temperature.getAsFloat()));
			}

			JsonElement humidity = mainData.get("humidity");
			if (humidity != null) {
				kestrelWeather.setHumidity(humidity.getAsInt());
			}

			JsonElement pressure = mainData.get("pressure");
			if (pressure != null) {
				kestrelWeather.setPressure(UnitConverter.pascalToKilopascal(pressure.getAsFloat()));
			}
		}

		JsonObject windData = openWeatherJson.getAsJsonObject("wind");
		if (windData != null) {
			JsonElement windSpeed = windData.get("speed");
			if (windSpeed != null) {
				kestrelWeather.setWindSpeed(windSpeed.getAsFloat());
			}

			JsonElement windDirection = windData.get("deg");
			if (windDirection != null) {
				kestrelWeather.setWindDirection(windDirection.getAsFloat());
			}
		}

		float temperatureInCelsius = kestrelWeather.getTemperature();
		float windSpeedInKmph = UnitConverter.mpsToKmph(kestrelWeather.getWindSpeed());

		//Disable Checks: MagicNumber
		double calculatedWindChill = 13.12 + (0.6215 * temperatureInCelsius)
				- (11.37 * Math.pow(windSpeedInKmph, 0.16))
				+ (0.3965 * temperatureInCelsius * Math.pow(windSpeedInKmph, 0.16));

		kestrelWeather.setWindChill((float) calculatedWindChill);
		kestrelWeather.setPressureTrend((int) Math.floor((Math.random() * 2)));
		kestrelWeather.setHeatIndex(temperatureInCelsius + (float) (Math.random() * 10));
		kestrelWeather.setDewPoint(temperatureInCelsius - (float) (Math.random() * 10));
		//Enable Checks: MagicNumber

		return kestrelWeather;
	}
}
