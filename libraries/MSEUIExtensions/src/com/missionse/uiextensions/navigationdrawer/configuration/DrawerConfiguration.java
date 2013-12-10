package com.missionse.uiextensions.navigationdrawer.configuration;

import java.util.List;

import android.widget.BaseAdapter;

import com.missionse.uiextensions.navigationdrawer.DrawerItem;

/**
 * A container class that holds all configuration options of the Navigation Drawer, used by the DrawerActivity when
 * instantiating the NavigationDrawer.
 */
public class DrawerConfiguration {

	private int mDrawerShadow;
	private int mDrawer;
	private int[] mActionMenuItemsToHideWhenDrawerOpen;
	private List<DrawerItem> mNavigationDrawerItems;
	private int mDrawerOpenAccessibilityDescription;
	private int mDrawerCloseAccessibilityDescription;
	private BaseAdapter mBaseAdapter;

	/**
	 * Gets the resource ID used to draw the Drawer shadow, if any.
	 * @return the resource ID of the shadow
	 */
	public int getDrawerShadow() {
		return mDrawerShadow;
	}

	/**
	 * Sets the resource ID representing the resource to use to draw a shadow, if necessary.
	 * @param drawerShadow the resource ID of the drawer shadow
	 */
	public void setDrawerShadow(final int drawerShadow) {
		mDrawerShadow = drawerShadow;
	}

	/**
	 * Gets the resource ID of the ListView that comprises the specific NavigationDrawer within the DrawerLayout.
	 * @return the resource ID
	 */
	public int getDrawer() {
		return mDrawer;
	}

	/**
	 * Sets the resource ID of the ListView that will be the NavigationDrawer within the DrawerLayout.
	 * @param leftDrawer the resource ID
	 */
	public void setDrawer(final int leftDrawer) {
		mDrawer = leftDrawer;
	}

	/**
	 * Gets the items to be hidden when the drawer is open.
	 * @return an array of the items
	 */
	public int[] getActionMenuItemsToHideWhenDrawerOpen() {
		return mActionMenuItemsToHideWhenDrawerOpen;
	}

	/**
	 * Sets the items to be hidden when the drawer is open.
	 * @param actionMenuItemsToHideWhenDrawerOpen an array of the items to hide
	 */
	public void setActionMenuItemsToHideWhenDrawerOpen(final int[] actionMenuItemsToHideWhenDrawerOpen) {
		this.mActionMenuItemsToHideWhenDrawerOpen = actionMenuItemsToHideWhenDrawerOpen;
	}

	/**
	 * Gets the NavigationDrawerItems to be displayed in the NavigationDrawer.
	 * @return an array of the items
	 */
	public List<DrawerItem> getNavigationItems() {
		return mNavigationDrawerItems;
	}

	/**
	 * Sets the NavigationDrawerItems to be displayed in the NavigationDrawer.
	 * @param navigationItems an array of the items
	 */
	public void setNavigationItems(final List<DrawerItem> navigationItems) {
		mNavigationDrawerItems = navigationItems;
	}

	/**
	 * Gets the resource ID of the accessibility string describing the drawer open event.
	 * @return the resource ID
	 */
	public int getDrawerOpenDesc() {
		return mDrawerOpenAccessibilityDescription;
	}

	/**
	 * Sets the resource ID of the accessibility string describing the drawer open event.
	 * @param drawerOpenDesc the resource ID
	 */
	public void setDrawerOpenDesc(final int drawerOpenDesc) {
		mDrawerOpenAccessibilityDescription = drawerOpenDesc;
	}

	/**
	 * Gets the resource ID of the accessibility string describing the drawer close event.
	 * @return the resource ID
	 */
	public int getDrawerCloseDesc() {
		return mDrawerCloseAccessibilityDescription;
	}

	/**
	 * Sets the resource ID of the accessibility string describing the drawer close event.
	 * @param drawerCloseDesc the resource ID
	 */
	public void setDrawerCloseDesc(final int drawerCloseDesc) {
		mDrawerCloseAccessibilityDescription = drawerCloseDesc;
	}

	/**
	 * Gets the adapter to use to translate DrawerItem structures to Android widgets.
	 * @return the adapter
	 */
	public BaseAdapter getBaseAdapter() {
		return mBaseAdapter;
	}

	/**
	 * Sets the adapter to use to translate DrawerItem structures to Android widgets.
	 * @param baseAdapter the adapter
	 */
	public void setBaseAdapter(final BaseAdapter baseAdapter) {
		mBaseAdapter = baseAdapter;
	}
}
