package com.missionse.uiextensions.touchlistener;

import android.widget.ListView;

/**
 * The callback interface used by {@link SwipeToDismissTouchListener} to inform its client about a successful dismissal
 * of one or more list item positions.
 */
public interface SwipeToDismissListener {
	/**
	 * Called to determine whether the given mPosition can be dismissed.
	 * @param position the item to determine state for
	 * @return whether or not this item can be dismissed
	 */
	boolean canDismiss(int position);

	/**
	 * Called when the user has indicated they she would like to dismiss one or more list item positions.
	 * 
	 * @param listView The originating {@link ListView}.
	 * @param positions An array of positions to dismiss, sorted in descending order for convenience.
	 */
	void onDismiss(ListView listView, int[] positions);
}
