package com.missionse.kestrelweather.util;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.model.tables.OpenWeather;

/**
 * A utility class that provides a connection to the Open Weather API.
 */
public final class OpenWeatherRequester {

	private OpenWeatherRequester() {
	}

	/**
	 * Gets the OpenWeather data for a given location.
	 * @param context The current context.
	 * @param latitude The latitude of the location.
	 * @param longitude The longitude of the location.
	 * @param callback The callback function to call upon completion.
	 */
	public static void queryOpenWeather(final Context context, final String latitude, final String longitude,
			final FutureCallback<JsonObject> callback) {
		String openWeatherApiUrl = context.getString(R.string.open_weather_api, latitude, longitude);

		Ion.with(context, openWeatherApiUrl)
				.asJsonObject()
				.setCallback(callback);
	}

	/**
	 * Parses the Open Weather response.
	 * @param openWeatherJson The response from OpenWeather in JSON.
	 * @return An object containing the open weather data.
	 */
	public static OpenWeather parseOpenWeatherResponse(final JsonObject openWeatherJson) {
		OpenWeather openWeather = new OpenWeather();

		JsonElement name = openWeatherJson.get("name");
		if (name != null) {
			openWeather.setName(name.getAsString());
		}

		JsonObject sysData = openWeatherJson.getAsJsonObject("sys");
		if (sysData != null) {
			JsonElement country = sysData.get("country");
			if (country != null) {
				openWeather.setCountry(country.getAsString());
			}
		}

		JsonArray weatherArray = openWeatherJson.getAsJsonArray("weather");
		if (weatherArray != null && weatherArray.size() > 0) {
			JsonObject weatherData = weatherArray.get(0).getAsJsonObject();
			if (weatherData != null) {
				JsonElement conditionCode = weatherData.get("id");
				if (conditionCode != null) {
					openWeather.setConditionCode(conditionCode.getAsInt());
				}

				JsonElement description = weatherData.get("description");
				if (description != null) {
					openWeather.setDescription(description.getAsString());
				}
			}
		}

		return openWeather;
	}
}
