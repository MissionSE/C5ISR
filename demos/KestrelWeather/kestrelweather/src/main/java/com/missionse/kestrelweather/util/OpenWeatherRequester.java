package com.missionse.kestrelweather.util;

import android.content.Context;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.missionse.kestrelweather.R;

/**
 * A utility class that provides a connection to the Open Weather API.
 */
public final class OpenWeatherRequester {

	private OpenWeatherRequester() {
	}

	/**
	 * Get the OpenWeather data for a given location.
	 * @param context The current context.
	 * @param latitude The latitude of the location.
	 * @param longitude The longitude of the location.
	 * @param callback The callback function to call upon completion.
	 */
	public static void queryOpenWeather(final Context context, final String latitude, final String longitude,
			final FutureCallback<JsonObject> callback) {
		String openWeatherApiUrl = String.format(
				context.getString(R.string.open_weather_api, latitude, longitude));

		Ion.with(context, openWeatherApiUrl)
				.asJsonObject()
				.setCallback(callback);
	}
}
