package com.missionse.mapdatabaseexample;

import com.missionse.mapdatabaseexample.model.MapLocation;

/**
 * Provides functionality to add a location.
 */
public interface MapLocationAdder {
	/**
	 * Adds a specified location to a map.
	 * @param location The location to add.
	 */
	void addLocation(final MapLocation location);
}
