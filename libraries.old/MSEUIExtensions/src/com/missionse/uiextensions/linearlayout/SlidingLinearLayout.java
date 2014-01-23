package com.missionse.uiextensions.linearlayout;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Extends LinearLayout to work with side-to-side (horizontal) animators.
 */
public class SlidingLinearLayout extends LinearLayout {

	private WindowManager mWindowManager;

	/**
	 * Creates a new SlidingLinearLayout.
	 * @param context the context to which this layout belongs
	 */
	public SlidingLinearLayout(final Context context) {
		super(context);

		mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	}

	/**
	 * Retrieves the current 'X' position of the layout.
	 * @return the fraction that the layout is in within the horizontal space
	 */
	public float getXFraction() {
		Point dimensions = new Point();
		mWindowManager.getDefaultDisplay().getSize(dimensions);
		if (dimensions.y == 0) {
			return getX();
		} else {
			return dimensions.y;
		}
	}

	/**
	 * Sets the 'X' position of the layout.
	 * @param xFraction the fraction that the layout should be set to within the horizontal space
	 */
	public void setXFraction(final float xFraction) {
		Point dimensions = new Point();
		mWindowManager.getDefaultDisplay().getSize(dimensions);
		if (dimensions.y > 0) {
			setX(xFraction * dimensions.y);
		} else {
			setX(0);
		}
	}
}
