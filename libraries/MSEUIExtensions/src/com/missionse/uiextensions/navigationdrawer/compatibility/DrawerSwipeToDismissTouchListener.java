package com.missionse.uiextensions.navigationdrawer.compatibility;

import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.missionse.uiextensions.touchlistener.SwipeToDismissListener;
import com.missionse.uiextensions.touchlistener.SwipeToDismissTouchListener;

/**
 * Disallows touch interception of the DrawerLayout at key points during the swipe-to-dismiss gesture.
 */
public class DrawerSwipeToDismissTouchListener extends SwipeToDismissTouchListener {

	private DrawerLayout mDrawerLayout;

	/**
	 * Creates a new DrawerSwipeToDismissTouchListener.
	 * @param drawerLayout the DrawerLayout that normally intercepts the touch events
	 * @param listView the ListView comprising the items that can be dismissed
	 * @param listener listener to notify of dismiss events
	 */
	public DrawerSwipeToDismissTouchListener(final DrawerLayout drawerLayout, final ListView listView,
			final SwipeToDismissListener listener) {
		super(listView, listener);
		mDrawerLayout = drawerLayout;
	}

	@Override
	public boolean onTouch(final View view, final MotionEvent motionEvent) {
		if (!mDrawerLayout.isDrawerOpen(Gravity.END)) {
			mDrawerLayout.requestDisallowInterceptTouchEvent(false);
			return false;
		} else {
			boolean touchConsumed = super.onTouch(view, motionEvent);
			mDrawerLayout.requestDisallowInterceptTouchEvent(touchConsumed);
			return touchConsumed;
		}
	}
}
