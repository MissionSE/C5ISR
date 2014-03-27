package com.missionse.uiextensions.graph;

import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Region;

/**
 * Represents the a "slice" in a Pie Chart.
 */
public class PieSlice {

	private int mColor;
	private float mValue;
	private String mTitle;
	private Path mPath;
	private Region mRegion;
	private boolean mIsSelected;

	public PieSlice() {
		mColor = Color.BLACK;
		mValue = 0;
		mTitle = "";
		mPath = new Path();
		mRegion = new Region();
		mIsSelected = false;
	}

	/**
	 * Retrieves the color of this slice.
	 * @return the color
	 */
	public int getColor() {
		return mColor;
	}

	/**
	 * Sets the color of this slice.
	 * @param color the color
	 */
	public void setColor(final int color) {
		mColor = color;
	}

	/**
	 * Gets the value of this slice.
	 * @return the value
	 */
	public float getValue() {
		return mValue;
	}

	/**
	 * Sets the value of this slice.
	 * @param value the value
	 */
	public void setValue(float value) {
		mValue = value;
	}

	/**
	 * Gets the title of this slice.
	 * @return the title
	 */
	public String getTitle() {
		return mTitle;
	}

	/**
	 * Sets the title of this slice.
	 * @param title the title
	 */
	public void setTitle(final String title) {
		mTitle = title;
	}

	/**
	 * Get the path of this slice.
	 * @return the path
	 */
	public Path getPath() {
		return mPath;
	}

	/**
	 * Set the path of this slice.
	 * @param path the path
	 */
	public void setPath(final Path path) {
		mPath = path;
	}

	/**
	 * Get the region of this slice.
	 * @return the region
	 */
	public Region getRegion() {
		return mRegion;
	}

	/**
	 * Sets the region of this slice.
	 * @param region the region
	 */
	public void setRegion(final Region region) {
		mRegion = region;
	}

	/**
	 * Sets the selected flag of this slice to true.
	 */
	public void select() {
		mIsSelected = true;
	}

	/**
	 * Sets the selected flag of this slice to false.
	 */
	public void unselect() {
		mIsSelected = false;
	}

	/**
	 * Retrieves the selected status of this slice.
	 * @return whether or not this slice is selected
	 */
	public boolean isSelected() {
		return mIsSelected;
	}
}

