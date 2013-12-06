package com.missionse.logisticsexample.navdrawer;

import android.content.Context;

public class NavigationDrawerMenuBasic implements NavigationDrawerItem {

	public static final NavigationDrawerItemType TYPE = NavigationDrawerItemType.BASIC;
	private int id;
	private String label;
	private int icon;
	private boolean updateActionBarTitle;

	private NavigationDrawerMenuBasic() {
	}

	public static NavigationDrawerMenuBasic create(final int id, final String label, final String icon,
			final boolean updateActionBarTitle, final Context context) {
		NavigationDrawerMenuBasic item = new NavigationDrawerMenuBasic();
		item.setId(id);
		item.setLabel(label);
		item.setIcon(context.getResources().getIdentifier(icon, "drawable", context.getPackageName()));
		item.setUpdateActionBarTitle(updateActionBarTitle);
		return item;
	}

	@Override
	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	@Override
	public String getLabel() {
		return label;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

	@Override
	public NavigationDrawerItemType getType() {
		return TYPE;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean updateActionBarTitle() {
		return updateActionBarTitle;
	}

	public void setUpdateActionBarTitle(final boolean updateActionBarTitle) {
		this.updateActionBarTitle = updateActionBarTitle;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(final int icon) {
		this.icon = icon;
	}
}
