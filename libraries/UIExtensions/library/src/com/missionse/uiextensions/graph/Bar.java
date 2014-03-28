package com.missionse.uiextensions.graph;

import android.graphics.Path;
import android.graphics.Region;

/**
 * Describes a bar on a bar graph.
 */
public class Bar {
	private int mColor;
	private String mName = null;
	private float mValue;
	private String mValueString = null;
	private Path mPath = null;
	private Region mRegion = null;
	private boolean mIsSelected = false;

	/**
	 * Retrieves the color of this bar.
	 * @return the color
	 */
	public int getColor() {
		return mColor;
	}

	/**
	 * Sets the color of this bar.
	 * @param color the color
	 */
	public void setColor(final int color) {
		mColor = color;
	}

	/**
	 * Retrieves the name of this bar.
	 * @return the name
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Sets the name of this bar.
	 * @param name the name
	 */
	public void setName(final String name) {
		mName = name;
	}

	/**
	 * Gets the value of this bar.
	 * @return the value
	 */
	public float getValue() {
		return mValue;
	}

	/**
	 * Sets the value of this bar.
	 * @param value the value
	 */
	public void setValue(final float value) {
		mValue = value;
	}

	/**
	 * Gets the string to be printed to represent the value. Will default to the value if not explicitly set.
	 * @return the string to be printed to represent the value of this bar
	 */
	public String getValueString() {
		if (mValueString != null) {
			return mValueString;
		} else {
			return String.valueOf(mValue);
		}
	}

	/**
	 * Sets the string to be printed to represent the value.
	 * @param valueString the string to be printed to represent the value of this bar
	 */
	public void setValueString(final String valueString) {
		mValueString = valueString;
	}

	/**
	 * Retrieves the path of this bar.
	 * @return the path
	 */
	public Path getPath() {
		return mPath;
	}

	/**
	 * Sets the path of this bar.
	 * @param path the path
	 */
	public void setPath(final Path path) {
		mPath = path;
	}

	/**
	 * Retrieves the region of this bar.
	 * @return the region
	 */
	public Region getRegion() {
		return mRegion;
	}

	/**
	 * Sets the region of this bar.
	 * @param region the region
	 */
	public void setRegion(final Region region) {
		mRegion = region;
	}

	/**
	 * Retrieves the selected status of this bar.
	 * @return whether or not this bar is selected
	 */
	public boolean isSelected() {
		return mIsSelected;
	}

	/**
	 * Sets the selected status of this bar.
	 * @param isSelected whether or not this bar is selected
	 */
	public void setSelected(final boolean isSelected) {
		mIsSelected = isSelected;
	}
}

