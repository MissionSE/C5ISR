package com.missionse.kestrelweather.reports.auxiliary;

/**
 * Provides an interface describing a list item representing auxiliary data.
 */
public interface AuxiliaryDataListItem {
	/**
	 * Get the resource id representing the icon of the auxiliary data.
	 * @return The resource id representing the icon of the auxiliary data.
	 */
	int getIcon();

	/**
	 * Get the number of items in this item of auxiliary data.
	 * @return The number of items in this item of auxiliary data.
	 */
	int getCount();

	/**
	 * Get the type of the auxiliary data.
	 * @return The type of the auxiliary data.
	 */
	String getType();

	/**
	 * Handles a callback when the item is clicked.
	 */
	void onClick();
}
