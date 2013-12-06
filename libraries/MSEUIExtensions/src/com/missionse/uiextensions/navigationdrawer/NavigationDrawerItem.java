package com.missionse.uiextensions.navigationdrawer;

/**
 * A base Navigation Drawer item.
 */
public abstract class NavigationDrawerItem {

	private int mId;

	/**
	 * Retrieves the id of this item.
	 * @return the id
	 */
	public int getId() {
		return mId;
	}

	/**
	 * Sets the id of this item.
	 * @param id the id to set
	 */
	public void setId(final int id) {
		mId = id;
	}

	/**
	 * Returns the type of this item.
	 * @return the type
	 */
	public abstract NavigationDrawerItemType getType();

	/**
	 * Returns whether or not this item is able to be selected.
	 * @return whether or not this item is available to receive touch actions
	 */
	public abstract boolean isSelectable();

	/**
	 * Returns whether or not this item will change the title when selected.
	 * @return whether or not this item will change the title
	 */
	public abstract boolean willChangeActionBarTitle();

	/**
	 * Returns the Title to be displayed if this item will change the title in the ActionBar.
	 * @return the title
	 */
	public abstract String getActionBarTitle();
}
