package com.missionse.mapdatabaseexample;

import com.missionse.mapdatabaseexample.model.MapLocation;

/**
 * Provides a callback when a location is added.
 */
public interface MapLocationListener {
	/**
	 * Notifies a listener that a location is to be processed.
	 * @param location The location to be handled.
	 */
	void locationReceived(final MapLocation location);
}
