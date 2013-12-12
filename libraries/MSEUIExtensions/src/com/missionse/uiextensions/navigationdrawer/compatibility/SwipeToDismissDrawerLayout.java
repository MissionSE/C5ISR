package com.missionse.uiextensions.navigationdrawer.compatibility;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Fixes DrawerLayout to catch any runtime exceptions and ignores them. This issue specifically comes up when using
 * swipe-to-dismiss gestures with a DrawerLayout, and is a bug in the support library itself.
 * 
 * See this link for more detail: https://github.com/chrisbanes/PhotoView/issues/72
 */
public class SwipeToDismissDrawerLayout extends DrawerLayout {

	/**
	 * Creates a new SwipeToDismissDrawerLayout.
	 * @param context overall application context
	 */
	public SwipeToDismissDrawerLayout(final Context context) {
		super(context);
	}

	/**
	 * Creates a new SwipeToDismissDrawerLayout.
	 * @param context overall application context
	 * @param attrs attributes to apply on creation
	 */
	public SwipeToDismissDrawerLayout(final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Creates a new SwipeToDismissDrawerLayout.
	 * @param context overall application context
	 * @param attrs attributes to apply on creation
	 * @param defStyle defined style?
	 */
	public SwipeToDismissDrawerLayout(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onInterceptTouchEvent(final MotionEvent ev) {
		try {
			return super.onInterceptTouchEvent(ev);
		} catch (Throwable t) {
			t.printStackTrace();
			return false;
		}
	}

}
