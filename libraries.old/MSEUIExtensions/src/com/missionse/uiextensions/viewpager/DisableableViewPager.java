package com.missionse.uiextensions.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

/**
 * Extends the ViewPager to allow it to be disable/enabled on demand.
 */
public class DisableableViewPager extends ViewPager {

	private boolean mPagingEnabled = true;

	/**
	 * Creates a new DisableableViewPager.
	 * @param context the context to which this DisableableViewPager belongs
	 */
	public DisableableViewPager(final Context context) {
		super(context);
	}

	/**
	 * Enables paging.
	 */
	public void enablePaging() {
		mPagingEnabled = true;
	}

	/**
	 * Disables paging.
	 */
	public void disablePaging() {
		mPagingEnabled = false;
	}

	@Override
	public boolean onInterceptTouchEvent(final MotionEvent event) {
		if (mPagingEnabled) {
			return super.onInterceptTouchEvent(event);
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		if (mPagingEnabled) {
			return super.onTouchEvent(event);
		}
		return false;
	}
}
