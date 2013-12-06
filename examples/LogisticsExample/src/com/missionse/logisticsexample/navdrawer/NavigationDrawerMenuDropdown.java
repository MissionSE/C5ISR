package com.missionse.logisticsexample.navdrawer;

import android.widget.ArrayAdapter;

public class NavigationDrawerMenuDropdown implements NavigationDrawerItem {

	public static final NavigationDrawerItemType TYPE = NavigationDrawerItemType.DROPDOWN;
	private int id;
	private String label;
	private ArrayAdapter<String> adapter;

	private NavigationDrawerMenuDropdown() {
	}

	public static NavigationDrawerMenuDropdown create(final int id, final String label,
			final ArrayAdapter<String> adapter) {
		NavigationDrawerMenuDropdown dropdown = new NavigationDrawerMenuDropdown();
		dropdown.setId(id);
		dropdown.setLabel(label);
		dropdown.setAdapter(adapter);
		return dropdown;
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

	public ArrayAdapter<String> getAdapter() {
		return adapter;
	}

	public void setAdapter(final ArrayAdapter<String> adapter) {
		this.adapter = adapter;
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
		return false;
	}
}
