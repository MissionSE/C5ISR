package com.missionse.kestrelweather.reports.readings;

/**
 * Provides an interface describing a list item representing a reading.
 */
public interface ReadingsListItem {
	/**
	 * Get the resource id representing the icon of the reading.
	 * @return The resource id representing the icon of the reading.
	 */
	int getIcon();

	/**
	 * Get the reading value.
	 * @return A string representation of the reading.
	 */
	String getReading();

	/**
	 * Get the type of the reading.
	 * @return The type of the reading.
	 */
	String getType();

	/**
	 * Get the units in which the readings are measured.
	 * @return The units of the reading.
	 */
	String getUnits();
}
