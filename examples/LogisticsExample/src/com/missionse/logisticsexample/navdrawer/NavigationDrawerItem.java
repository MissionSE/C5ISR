package com.missionse.logisticsexample.navdrawer;

public interface NavigationDrawerItem {
	public int getId();

	public String getLabel();

	public NavigationDrawerItemType getType();

	public boolean isEnabled();

	public boolean updateActionBarTitle();
}
