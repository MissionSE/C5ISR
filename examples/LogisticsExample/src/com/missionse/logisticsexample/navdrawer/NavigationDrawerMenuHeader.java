package com.missionse.logisticsexample.navdrawer;

public class NavigationDrawerMenuHeader implements NavigationDrawerItem {

	public static final NavigationDrawerItemType TYPE = NavigationDrawerItemType.HEADER;
	private int id;
	private String label;

	private NavigationDrawerMenuHeader() {
	}

	public static NavigationDrawerMenuHeader create(final int id, final String label) {
		NavigationDrawerMenuHeader header = new NavigationDrawerMenuHeader();
		header.setId(id);
		header.setLabel(label);
		return header;
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
		return false;
	}

	@Override
	public boolean updateActionBarTitle() {
		return false;
	}

}
