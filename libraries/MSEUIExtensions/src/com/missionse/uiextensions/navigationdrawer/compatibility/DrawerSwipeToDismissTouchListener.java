package com.missionse.uiextensions.navigationdrawer.compatibility;

import android.support.v4.widget.DrawerLayout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.missionse.uiextensions.touchlistener.SwipeToDismissListener;
import com.missionse.uiextensions.touchlistener.SwipeToDismissTouchListener;

/**
 * Extensions of the SwipeToDismissTouchListener that specifically disabled DrawerLayout touch event interception, to
 * give precedence to the swipe-to-dismiss gesture when this child needs it.
 */
public class DrawerSwipeToDismissTouchListener extends SwipeToDismissTouchListener {

	private DrawerLayout mDrawerLayout;

	/**
	 * Constructs a new DrawerSwipeToDismissTouchListener.
	 * @param drawerLayout the parent drawer layout
	 * @param listView the listView using this touch listener
	 * @param listener the listener to callback on swipe-to-dismiss events
	 */
	public DrawerSwipeToDismissTouchListener(final DrawerLayout drawerLayout, final ListView listView,
			final SwipeToDismissListener listener) {
		super(listView, listener);
		mDrawerLayout = drawerLayout;
	}

	@Override
	public boolean onTouch(final View view, final MotionEvent motionEvent) {
		mDrawerLayout.requestDisallowInterceptTouchEvent(true);
		super.onTouch(view, motionEvent);
		// Returning false, or the ScrollListener will not receive events.
		return false;
	}
}
