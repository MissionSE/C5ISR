package com.missionse.uiextensions.navigationdrawer;

/**
 * Defines the types of widgets that can appear in the Navigation Drawer.
 */
public enum DrawerItemType {
	SIMPLE, SIMPLENUMBERED, COMPLEX, HEADER, DROPDOWN, PADDEDDIVIDER, DIVIDER, UNKNOWN;

	/**
	 * Returns the number of unique types of NavigationDrawerItems.
	 * @return the number of types
	 */
	public static int getNumberOfTypes() {
		return UNKNOWN.ordinal();
	}
}
