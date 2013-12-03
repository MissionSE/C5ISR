package com.missionse.mapsexample;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

public class SlidingFrameLayout extends FrameLayout {
	
	private static final String TAG = SlidingFrameLayout.class.getSimpleName();
	
	public SlidingFrameLayout(Context context) {
		super(context);
		Log.d(TAG, "SlidingFrameLayout(Context context)");
	}
	
	public SlidingFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.d(TAG, "SlidingFrameLayout(Context context, AttributeSet attrs)");
	}
	
	public SlidingFrameLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Log.d(TAG, "SlidingFrameLayout(Context context, AttributeSet attrs, int defStyle)");
	}

	public float getXFraction() {
        return getX() / getWidth(); // TODO: guard divide-by-zero
    }

    public void setXFraction(float xFraction) {
    	Log.d(TAG, "setXFraction called.");
        // TODO: cache width
        final int width = getWidth();
        setX((width > 0) ? (xFraction * width) : -9999);
    }

}
