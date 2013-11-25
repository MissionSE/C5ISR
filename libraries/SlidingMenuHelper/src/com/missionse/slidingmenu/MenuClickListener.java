package com.missionse.slidingmenu;

public interface MenuClickListener {

	/**
	 * Called when an item in the SlidingMenu is clicked.
	 * @param index of the selected item
	 */
	public void onMenuClick(int position);
}
