package com.missionse.logisticsexample.map;

import com.google.android.gms.maps.model.LatLng;

/**
 * Provides the model of a location on the map.
 */
public class MapLocation {
	private int mId;
	private String mName;
	private double mLatitude;
	private double mLongitude;

	/**
	 * Constructor.
	 * @param id The unique id for the location.
	 * @param name The name of the location.
	 * @param latitude The latitude of the location, in degrees.
	 * @param longitude The longitude of the location, in degrees.
	 */
	public MapLocation(final int id, final String name, final double latitude, final double longitude) {
		mId = id;
		mName = name;
		mLatitude = latitude;
		mLongitude = longitude;
	}

	/**
	 * Get the unique id of the location.
	 * @return The id of the location.
	 */
	public int getId() {
		return mId;
	}

	/**
	 * Gets the name of the location.
	 * @return The name of the location.
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Gets the latitude of the location, in degrees.
	 * @return The latitude of the location, in degrees.
	 */
	public double getLatitude() {
		return mLatitude;
	}

	/**
	 * Gets the longitude of the location, in degrees.
	 * @return The longitude of the location, in degrees.
	 */
	public double getLongitude() {
		return mLongitude;
	}

	/**
	 * Gets the latitude and longitude of the location.
	 * @return The latitude and longitude of the location.
	 */
	public LatLng getLatLng() {
		return new LatLng(getLatitude(), getLongitude());
	}

	@Override
	public String toString() {
		return "[Location] " + mName + " (" + mLatitude + ", " + mLongitude + ")";
	}
}
