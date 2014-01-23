package com.missionse.slidingmenu;

/**
 * Provides callbacks for notification menu specific actions.
 */
public interface OnNotificationActionListener {

	/**
	 * Called when a notification item is selected.
	 * @param position the position of the item selected
	 */
	void onNotificationSelected(int position);

	/**
	 * Called when a notification item is dismissed.
	 * @param position the position of the item dismissed
	 */
	void onNotificationDismissed(int position);
}
