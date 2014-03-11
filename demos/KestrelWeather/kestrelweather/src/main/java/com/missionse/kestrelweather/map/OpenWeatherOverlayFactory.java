package com.missionse.kestrelweather.map;

import android.content.Context;

import com.google.android.gms.maps.model.TileProvider;
import com.missionse.kestrelweather.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides functionality to build overlays from OpenWeather.
 */
public final class OpenWeatherOverlayFactory {
	private OpenWeatherOverlayFactory() {
	}

	/**
	 * Creates a map of tile providers used to retrieve overlays.
	 * @param context The current context.
	 * @return A map of tile providers.
	 */
	public static Map<String, TileProvider> getOpenWeatherTileProviders(final Context context) {
		Map<String, TileProvider> tileProviders = new HashMap<String, TileProvider>();
		tileProviders.put(context.getString(R.string.rain_overlay_name),
				TileProviderFactory.createUrlTileProvider(context.getString(R.string.rain_overlay_url)));
		tileProviders.put(context.getString(R.string.cloud_overlay_name),
				TileProviderFactory.createUrlTileProvider(context.getString(R.string.cloud_overlay_url)));
		tileProviders.put(context.getString(R.string.snow_overlay_name),
				TileProviderFactory.createUrlTileProvider(context.getString(R.string.snow_overlay_url)));
		tileProviders.put(context.getString(R.string.pressure_overlay_name),
				TileProviderFactory.createUrlTileProvider(context.getString(R.string.pressure_overlay_url)));
		tileProviders.put(context.getString(R.string.temp_overlay_name),
				TileProviderFactory.createUrlTileProvider(context.getString(R.string.temp_overlay_url)));
		tileProviders.put(context.getString(R.string.precipitation_overlay_name),
				TileProviderFactory.createUrlTileProvider(context.getString(R.string.precipitation_overlay_url)));
		tileProviders.put(context.getString(R.string.wind_overlay_name),
				TileProviderFactory.createUrlTileProvider(context.getString(R.string.wind_overlay_url)));

		return tileProviders;
	}

	/**
	 * Determines whether a menu id represents a valid OpenWeather overlay.
	 * @param id The menu item id of the overlay.
	 * @return Whether the id represents a valid overlay.
	 */
	public static boolean isValidOverlay(final int id) {
		boolean isValid = false;
		if (id == R.id.action_rain
				|| id == R.id.action_cloud
				|| id == R.id.action_snow
				|| id == R.id.action_pressure
				|| id == R.id.action_temp
				|| id == R.id.action_precipitation
				|| id == R.id.action_wind) {
			isValid = true;
		}

		return isValid;
	}
}
