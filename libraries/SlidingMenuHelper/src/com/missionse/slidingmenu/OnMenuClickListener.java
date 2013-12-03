package com.missionse.slidingmenu;

/**
 * Provides callbacks for when a menu item is clicked.
 */
public interface OnMenuClickListener {

	/**
	 * Called when an item in the SlidingMenu is clicked.
	 * @param position of the selected item
	 */
	void onMenuClick(int position);
}
