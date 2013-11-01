package com.missionse.commandablemodel;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class GutterViewPager extends ViewPager {

	private static final int DEFAULT_GUTTER_SIZE = 16; // dips
	
	private int mDefaultGutterSize;
	private int mGutterSize;
	
	private final List<Integer> gutterEnabledPages = new ArrayList<Integer>();
	
	public GutterViewPager(Context context) {
		super(context);
		init();
	}
 
	public GutterViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
 
	private void init() {
		final float density = getContext().getResources().getDisplayMetrics().density;
		mDefaultGutterSize = (int) (DEFAULT_GUTTER_SIZE * density);
	}
	
	public void enableGutterForPage(final int pageNumber) {
		gutterEnabledPages.add(Integer.valueOf(pageNumber));
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int measuredWidth = getMeasuredWidth();
    	final int maxGutterSize = measuredWidth / 10;
    	mGutterSize = Math.min(maxGutterSize, mDefaultGutterSize);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
 
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// Only handle events within the left gutter.
		if (gutterEnabledPages.contains(Integer.valueOf(getCurrentItem()))) {
			if (ev.getAction() != MotionEvent.ACTION_UP && ev.getX() < mGutterSize) {
				return super.onTouchEvent(ev);
			}
			else {
				return false;
			}
		}
		return super.onTouchEvent(ev);
	}
}
