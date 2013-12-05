package com.missionse.mapdatabaseexample;

import com.missionse.mapdatabaseexample.model.MapLocation;

/**
 * Provides a callback when a location is added.
 */
public interface MapLocationListener {
	/**
	 * Adds a specified location to a map.
	 * @param location The location to add.
	 */
	void addLocation(final MapLocation location);
}
