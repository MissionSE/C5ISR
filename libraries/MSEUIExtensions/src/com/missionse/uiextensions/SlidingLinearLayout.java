package com.missionse.uiextensions;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class SlidingLinearLayout extends LinearLayout {

	private WindowManager windowManager;
	
	public SlidingLinearLayout(final Context context) {
		super(context);
		
		windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	}
	
	public SlidingLinearLayout(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		
		windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	}
	
	public SlidingLinearLayout(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
		
		windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	}

	private WindowManager getWindowManager() {
		return windowManager;
	}
	
	public float getXFraction() {
		Point dimensions = new Point();
        getWindowManager().getDefaultDisplay().getSize(dimensions);
        return (dimensions.y == 0) ? 0 : getX() / (float) dimensions.y;
    }

    public void setXFraction(float xFraction) {
    	Point dimensions = new Point();
        getWindowManager().getDefaultDisplay().getSize(dimensions);
        setX((dimensions.y > 0) ? (xFraction * dimensions.y) : 0);
    }
}
