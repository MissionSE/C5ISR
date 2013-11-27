package com.missionse.uiextensions.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

/**
 * Extends the ViewPager to work with a Navigation Drawer and horizontal gestures.
 */
public class DrawerSafeViewPager extends ViewPager {

	private static final int DEFAULT_GUTTER_SIZE = 16; // dips
	private static final int GUTTER_RATIO = 10;

	private int mDefaultGutterSize;
	private int mGutterSize;

	/**
	 * Creates a new DrawerSafeViewPager.
	 * @param context the context in which this ViewPager belongs
	 */
	public DrawerSafeViewPager(final Context context) {
		super(context);
		init();
	}

	void init() {
		final float density = getContext().getResources().getDisplayMetrics().density;
		mDefaultGutterSize = (int) (DEFAULT_GUTTER_SIZE * density);
	}

	@Override
	protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
		final int measuredWidth = getMeasuredWidth();
		final int maxGutterSize = measuredWidth / GUTTER_RATIO;
		mGutterSize = Math.min(maxGutterSize, mDefaultGutterSize);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	public boolean onTouchEvent(final MotionEvent ev) {
		// Don't handle events within the left gutter. This fixes an issue with 
		// ViewPager briefly intercepting the bezel swipe gesture within a DrawerLayout 
		// and starting a drag movement, causing a visual stutter.
		if (ev.getAction() != MotionEvent.ACTION_UP && ev.getX() < mGutterSize) {
			return false;
		}
		return super.onTouchEvent(ev);
	}
}
