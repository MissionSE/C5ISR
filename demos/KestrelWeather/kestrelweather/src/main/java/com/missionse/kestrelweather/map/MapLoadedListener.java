package com.missionse.kestrelweather.map;

import com.google.android.gms.maps.GoogleMap;

/**
 * Provides a callback the listener when a map is loaded.
 */
public interface MapLoadedListener {

	/**
	 * Notifies the listener when a map is loaded.
	 * @param map The map that has been loaded.
	 */
	void mapLoaded(final GoogleMap map);
}
