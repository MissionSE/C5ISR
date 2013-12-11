package com.missionse.uiextensions.navigationdrawer.configuration;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.BaseAdapter;

import com.missionse.uiextensions.R;
import com.missionse.uiextensions.navigationdrawer.DrawerAdapter;
import com.missionse.uiextensions.navigationdrawer.configuration.DrawerConfigurationContainer.DrawerType;

/**
 * A container class that holds all configuration options of the Navigation Drawer, used by the DrawerActivity when
 * instantiating the NavigationDrawer.
 */
public class DrawerConfiguration {

	private DrawerType mType;
	private int mDrawerShadow;
	private int mDrawer;
	private int[] mActionMenuItemsToHideWhenDrawerOpen;
	private int mDrawerOpenAccessibilityDescription;
	private int mDrawerCloseAccessibilityDescription;
	private BaseAdapter mBaseAdapter;
	private Drawable mDivider;

	/**
	 * Constructs a new DrawerConfiguration with default settings.
	 * @param type the type of drawer (LEFT/RIGHT)
	 * @param context the context of the parent activity
	 */
	public DrawerConfiguration(final DrawerType type, final Context context) {
		mType = type;
		setDefaults(context);
	}

	private void setDefaults(final Context context) {
		if (mType == DrawerType.LEFT) {
			setDrawerShadow(R.drawable.drawer_shadow_left);
		} else {
			setDrawerShadow(R.drawable.drawer_shadow_right);
		}

		setDrawerOpenDesc(R.string.drawer_open);
		setDrawerCloseDesc(R.string.drawer_close);
		setBaseAdapter(new DrawerAdapter(context));
	}

	/**
	 * Retrieves the type of this DrawerConfiguration.
	 * @return the type (LEFT/RIGHT)
	 */
	public DrawerType getType() {
		return mType;
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
	 * Gets the resource ID of the ListView that comprises the specific drawer within the DrawerLayout.
	 * @return the resource ID
	 */
	public int getDrawer() {
		return mDrawer;
	}

	/**
	 * Sets the resource ID of the ListView that will be the drawer within the DrawerLayout.
	 * @param drawer the resource ID
	 */
	public void setDrawer(final int drawer) {
		mDrawer = drawer;
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

	/**
	 * Retrieves the divider to be displayed between each list item.
	 * @return the divider drawable
	 */
	public Drawable getDivider() {
		return mDivider;
	}

	/**
	 * Sets the divider drawable to be used between each list item.
	 * @param divider the drawable to use
	 */
	public void setDivider(final Drawable divider) {
		mDivider = divider;
	}
}
