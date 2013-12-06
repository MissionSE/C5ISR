package com.missionse.logisticsexample.navdrawer;

import android.widget.BaseAdapter;

public class NavigationDrawerConfiguration {

	private int mainLayout;
	private int drawerShadow;
	private int drawerLayoutId;
	private int leftDrawerId;
	private int[] actionMenuItemsToHideWhenDrawerOpen;
	private NavigationDrawerItem[] navItems;
	private int drawerOpenDesc;
	private int drawerCloseDesc;
	private BaseAdapter baseAdapter;

	public int getMainLayout() {
		return mainLayout;
	}

	public void setMainLayout(final int mainLayout) {
		this.mainLayout = mainLayout;
	}

	public int getDrawerShadow() {
		return drawerShadow;
	}

	public void setDrawerShadow(final int drawerShadow) {
		this.drawerShadow = drawerShadow;
	}

	public int getDrawerLayoutId() {
		return drawerLayoutId;
	}

	public void setDrawerLayoutId(final int drawerLayoutId) {
		this.drawerLayoutId = drawerLayoutId;
	}

	public int getLeftDrawerId() {
		return leftDrawerId;
	}

	public void setLeftDrawerId(final int leftDrawerId) {
		this.leftDrawerId = leftDrawerId;
	}

	public int[] getActionMenuItemsToHideWhenDrawerOpen() {
		return actionMenuItemsToHideWhenDrawerOpen;
	}

	public void setActionMenuItemsToHideWhenDrawerOpen(final int[] actionMenuItemsToHideWhenDrawerOpen) {
		this.actionMenuItemsToHideWhenDrawerOpen = actionMenuItemsToHideWhenDrawerOpen;
	}

	public NavigationDrawerItem[] getNavItems() {
		return navItems;
	}

	public void setNavItems(final NavigationDrawerItem[] navItems) {
		this.navItems = navItems;
	}

	public int getDrawerOpenDesc() {
		return drawerOpenDesc;
	}

	public void setDrawerOpenDesc(final int drawerOpenDesc) {
		this.drawerOpenDesc = drawerOpenDesc;
	}

	public int getDrawerCloseDesc() {
		return drawerCloseDesc;
	}

	public void setDrawerCloseDesc(final int drawerCloseDesc) {
		this.drawerCloseDesc = drawerCloseDesc;
	}

	public BaseAdapter getBaseAdapter() {
		return baseAdapter;
	}

	public void setBaseAdapter(final BaseAdapter baseAdapter) {
		this.baseAdapter = baseAdapter;
	}
}
