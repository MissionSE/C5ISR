package com.missionse.uiextensions.graph;

import android.graphics.Path;
import android.graphics.Region;

/**
 * Describes a point on a line.
 */
public class LinePoint {

	private float mX;
	private float mY;
	private Path mPath;
	private Region mRegion;
	private boolean mIsSelected;
	private String mTitle;

	/**
	 * Constructor.
	 */
	public LinePoint() {
		mX = 0;
		mY = 0;
		mPath = new Path();
		mRegion = new Region();
		mIsSelected = false;
	}

	/**
	 * Constructor.
	 * @param x the x value
	 * @param y the y value
	 */
	public LinePoint(final float x, final float y) {
		mX = x;
		mY = y;
	}

	/**
	 * Retrieves the y value.
	 * @return the x value
	 */
	public float getX() {
		return mX;
	}

	/**
	 * Sets the x value.
	 * @param x the x value
	 */
	public void setX(final float x) {
		mX = x;
	}

	/**
	 * Gets the y value.
	 * @return the y value
	 */
	public float getY() {
		return mY;
	}

	/**
	 * Sets the y value.
	 * @param y the y value
	 */
	public void setY(final float y) {
		mY = y;
	}

	/**
	 * Retrieves the region of this point.
	 * @return the region
	 */
	public Region getRegion() {
		return mRegion;
	}

	/**
	 * Sets the region of this point.
	 * @param region the region
	 */
	public void setRegion(final Region region) {
		mRegion = region;
	}

	/**
	 * Retrieves the path of this point.
	 * @return the path
	 */
	public Path getPath() {
		return mPath;
	}

	/**
	 * Sets the path of this point.
	 * @param path the path
	 */
	public void setPath(final Path path) {
		mPath = path;
	}

	/**
	 * Retrieves the selected status of this point.
	 * @return whether or not this point is selected
	 */
	public boolean isSelected() {
		return mIsSelected;
	}

	/**
	 * Sets the selected status of this point.
	 * @param isSelected whether or not this point is selected
	 */
	public void setSelected(final boolean isSelected) {
		mIsSelected = isSelected;
	}


	public void setTitle(final String title) {
		mTitle = title;
	}

	@Override
	public String toString() {
		if (mTitle == null) {
			return String.valueOf(mY);
		}
		return mTitle;
	}
}

