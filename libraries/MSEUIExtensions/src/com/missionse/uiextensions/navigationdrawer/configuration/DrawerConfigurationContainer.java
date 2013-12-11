package com.missionse.uiextensions.navigationdrawer.configuration;

import java.util.HashMap;

/**
 * Contains configurations for all possible potential Navigation drawers.
 */
public class DrawerConfigurationContainer {

	/**
	 * Denotes which the side of the application on which the drawer will appear.
	 */
	public enum DrawerType {
		LEFT, RIGHT
	}

	private final HashMap<DrawerType, DrawerConfiguration> mConfigurations = new HashMap<DrawerType, DrawerConfiguration>();
	private int mMainLayout;
	private int mDrawerLayout;

	/**
	 * Creates a new DrawerConfigurationContainer, to hold DrawerConfigurations and overall drawer configuration.
	 * @param mainLayout the layout of the activity that contains the drawer layout
	 * @param drawerLayout the id of the drawer layout
	 */
	public DrawerConfigurationContainer(final int mainLayout, final int drawerLayout) {
		mMainLayout = mainLayout;
		mDrawerLayout = drawerLayout;
	}

	/**
	 * Saves the configuration for a given Drawer.
	 * @param configuration the configuration
	 */
	public void addConfiguration(final DrawerConfiguration configuration) {
		mConfigurations.put(configuration.getType(), configuration);
	}

	/**
	 * Retrieves the configuration for the left-hand drawer.
	 * @return the configuration
	 */
	public DrawerConfiguration getLeftConfiguration() {
		return mConfigurations.get(DrawerType.LEFT);
	}

	/**
	 * Retrieves the configuration for the right-hand drawer.
	 * @return the configuration
	 */
	public DrawerConfiguration getRightConfiguration() {
		return mConfigurations.get(DrawerType.RIGHT);
	}

	/**
	 * Retrieves the resource ID of the main layout.
	 * @return the resource ID
	 */
	public int getMainLayout() {
		return mMainLayout;
	}

	/**
	 * Retrieves the resource ID of the drawer layout.
	 * @return the resource ID
	 */
	public int getDrawerLayout() {
		return mDrawerLayout;
	}

}
