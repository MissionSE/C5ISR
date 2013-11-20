package com.missionse.uiextensions.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class DisableableViewPager extends ViewPager {

	private boolean pagingEnabled = true;

	public DisableableViewPager(final Context context) {
		super(context);
	}

	public DisableableViewPager(final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	public void enablePaging() {
		pagingEnabled = true;
	}

	public void disablePaging() {
		pagingEnabled = false;
	}

	@Override
	public boolean onInterceptTouchEvent(final MotionEvent event) {
		if (pagingEnabled) {
			return super.onInterceptTouchEvent(event);
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		if (pagingEnabled) {
			return super.onTouchEvent(event);
		}
		return false;
	}
}
