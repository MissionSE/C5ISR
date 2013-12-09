package com.missionse.uiextensions.navigationdrawer.configuration;

import java.util.HashMap;

public class DrawerConfigurationContainer {

	public enum DrawerType {
		LEFT, RIGHT
	}

	private final HashMap<DrawerType, DrawerConfiguration> mConfigurations = new HashMap<DrawerType, DrawerConfiguration>();
	private int mMainLayout;
	private int mDrawerLayout;

	public DrawerConfigurationContainer(final int mainLayout, final int drawerLayout) {
		mMainLayout = mainLayout;
		mDrawerLayout = drawerLayout;
	}

	public void setConfiguration(final DrawerType type, final DrawerConfiguration configuration) {

		mConfigurations.put(type, configuration);
	}

	public DrawerConfiguration getLeftConfiguration() {
		return mConfigurations.get(DrawerType.LEFT);
	}

	public DrawerConfiguration getRightConfiguration() {
		return mConfigurations.get(DrawerType.RIGHT);
	}

	public int getMainLayout() {
		return mMainLayout;
	}

	public int getDrawerLayout() {
		return mDrawerLayout;
	}

}
