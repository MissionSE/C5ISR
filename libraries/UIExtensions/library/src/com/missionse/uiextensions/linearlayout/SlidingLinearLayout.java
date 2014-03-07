package com.missionse.uiextensions.linearlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Extends LinearLayout to work with side-to-side (horizontal) animators.
 */
public class SlidingLinearLayout extends LinearLayout {
	private static final int OFFSCREEN_X_POSITION = -10000;
	/**
	 * Creates a new SlidingLinearLayout.
	 * @param context the context to which this layout belongs
	 */
	public SlidingLinearLayout(final Context context) {
		super(context);
	}

	/**
	 * Creates a new SlidingLinearLayout.
	 * @param context the context to which this layout belongs
	 * @param attrs the attributes for the layout
	 */
	public SlidingLinearLayout(final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Creates a new SlidingLinearLayout.
	 * @param context the context to which this layout belongs
	 * @param attrs the attributes for the layout
	 * @param defStyle the style
	 */
	public SlidingLinearLayout(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
	}


	/**
	 * Retrieves the current 'X' position of the layout.
	 * @return the fraction that the layout is in within the horizontal space
	 */
	public float getXFraction() {
		final int width = getWidth();
		float xFraction = 0.0f;
		if (width != 0) {
			xFraction = getX() / width;
		}

		return xFraction;
	}

	/**
	 * Sets the 'X' position of the layout.
	 * @param xFraction the fraction that the layout should be set to within the horizontal space
	 */
	public void setXFraction(final float xFraction) {
		final int width = getWidth();
		if (width != 0) {
			setX(xFraction * width);
		} else {
			setX(OFFSCREEN_X_POSITION);
		}
	}
}
