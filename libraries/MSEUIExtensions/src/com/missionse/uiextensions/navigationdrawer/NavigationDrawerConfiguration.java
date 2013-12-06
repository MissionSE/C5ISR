package com.missionse.uiextensions.navigationdrawer;

import android.widget.BaseAdapter;

/**
 * A container class that holds all configuration options of the Navigation Drawer, used by the NavigationDrawerActivity
 * when instantiating the NavigationDrawer.
 */
public class NavigationDrawerConfiguration {

	private int mMainLayout;
	private int mDrawerShadow;
	private int mDrawerLayoutId;
	private int mLeftDrawerId;
	private int[] mActionMenuItemsToHideWhenDrawerOpen;
	private NavigationDrawerItem[] mNavigationDrawerItems;
	private int mDrawerOpenAccessibilityDescription;
	private int mDrawerCloseAccessibilityDescription;
	private BaseAdapter mBaseAdapter;

	/**
	 * Returns the ID of the layout file that includes the DrawerLayout.
	 * @return the ID of the layout file
	 */
	public int getMainLayout() {
		return mMainLayout;
	}

	/**
	 * Sets the ID of the layout file that contains the DrawerLayout.
	 * @param mainLayout the ID of the layout file
	 */
	public void setMainLayout(final int mainLayout) {
		mMainLayout = mainLayout;
	}

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
	 * Returns the resource ID of the DrawerLayout as defined in the specified layout file.
	 * @return the resource ID
	 */
	public int getDrawerLayoutId() {
		return mDrawerLayoutId;
	}

	/**
	 * Sets the resource ID of the DrawerLayout container as specified in the layout file.
	 * @param drawerLayoutId the resource ID
	 */
	public void setDrawerLayoutId(final int drawerLayoutId) {
		mDrawerLayoutId = drawerLayoutId;
	}

	/**
	 * Gets the resource ID of the ListView that comprises the specific NavigationDrawer within the DrawerLayout.
	 * @return the resource ID
	 */
	public int getLeftDrawerId() {
		return mLeftDrawerId;
	}

	/**
	 * Sets the resource ID of the ListView that will be the NavigationDrawer within the DrawerLayout.
	 * @param leftDrawerId the resource ID
	 */
	public void setLeftDrawerId(final int leftDrawerId) {
		mLeftDrawerId = leftDrawerId;
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
	public NavigationDrawerItem[] getNavItems() {
		return mNavigationDrawerItems;
	}

	/**
	 * Sets the NavigationDrawerItems to be displayed in the NavigationDrawer.
	 * @param navItems an array of the items
	 */
	public void setNavItems(final NavigationDrawerItem[] navItems) {
		mNavigationDrawerItems = navItems;
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
	 * Gets the adapter to use to translate NavigationDrawerItem structures to Android widgets.
	 * @return the adapter
	 */
	public BaseAdapter getBaseAdapter() {
		return mBaseAdapter;
	}

	/**
	 * Sets the adapter to use to translate NavigationDrawerItem structures to Android widgets.
	 * @param baseAdapter the adapter
	 */
	public void setBaseAdapter(final BaseAdapter baseAdapter) {
		mBaseAdapter = baseAdapter;
	}
}
