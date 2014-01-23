package com.missionse.logisticsexample.map;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;

import com.google.android.gms.maps.model.Marker;

/**
 * Provides a container for map locations and their associated markers.
 */
@SuppressLint("UseSparseArrays")
public class MapLocationManager {

	private final Map<Integer, MapLocation> mLocations;
	private final Map<Integer, Marker> mMarkers;

	/**
	 * Constructor.
	 */
	public MapLocationManager() {
		mLocations = new HashMap<Integer, MapLocation>();
		mMarkers = new HashMap<Integer, Marker>();
	}

	/**
	 * Determines whether a location exists given its id.
	 * @param id The unique identifier for the location.
	 * @return Whether the location exists.
	 */
	public boolean containsLocation(final int id) {
		return mLocations.containsKey(id);
	}

	/**
	 * Determines whether a marker exists given its id.
	 * @param id The unique identifier for the marker.
	 * @return Whether the marker exists.
	 */
	public boolean containsMarker(final int id) {
		return mMarkers.containsKey(id);
	}

	/**
	 * Gets a location with a specified id.
	 * @param id The unique identifier for the location.
	 * @return The location with the specified id.
	 */
	public MapLocation getLocation(final int id) {
		return mLocations.get(id);
	}

	/**
	 * Gets a location associated with a specified marker.
	 * @param marker The marker associated with the location.
	 * @return The location associated with the specified marker.
	 */
	public MapLocation getLocation(final Marker marker) {
		MapLocation location = null;
		for (int locationId : mMarkers.keySet()) {
			if (mMarkers.get(locationId).equals(marker)) {
				location = mLocations.get(locationId);
				break;
			}
		}
		return location;
	}

	/**
	 * Gets a marker with a specified id.
	 * @param id The unique identifier for the marker.
	 * @return The marker with the specified id.
	 */
	public Marker getMarker(final int id) {
		return mMarkers.get(id);
	}

	/**
	 * Gets a map of all locations mapped to their specified ids.
	 * @return A map of locations and ids.
	 */
	public Map<Integer, MapLocation> getLocations() {
		return mLocations;
	}

	/**
	 * Gets a map of all markers mapped to their specified ids.
	 * @return A map of all markers and ids.
	 */
	public Map<Integer, Marker> getMarkers() {
		return mMarkers;
	}

	/**
	 * Puts a location in the collection with the specified id.
	 * @param id The unique id for the location.
	 * @param location The location to be added to the collection.
	 */
	public void put(final int id, final MapLocation location) {
		mLocations.put(id, location);
	}

	/**
	 * Puts a marker in the collection with the specified id.
	 * @param id The unique id for the marker.
	 * @param marker The marker to be added to the collection.
	 */
	public void put(final int id, final Marker marker) {
		mMarkers.put(id, marker);
	}

	/**
	 * Removes a location with a specified id from the collection.
	 * @param id The id of the location (and associated marker) to be removed.
	 */
	public void remove(final int id) {
		mLocations.remove(id);
		mMarkers.remove(id);
	}
}
