package com.missionse.uiextensions.graph;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Represents a line on a line graph.
 */
public class Line {

	/**
	 * A comparator that compares two points in terms of their x values.
	 */
	public class LinePointComparator implements Comparator<LinePoint> {
		@Override
		public int compare(final LinePoint linePoint, final LinePoint linePoint2) {
			if (linePoint.getX() < linePoint2.getX()) {
				return -1;
			} else if (linePoint.getX() > linePoint2.getX()) {
				return 1;
			}
			return 0;
		}
	}

	private static final int DEFAULT_STROKE_WIDTH = 6;

	private ArrayList<LinePoint> mPoints;

	private int mColor;
	private int mStrokeWidth;

	private boolean mFill;
	private int mFillColor;
	private LineGraphFillType mFillType;
	private int mFillOffset;

	private float mLowerBoundX, mUpperBoundX, mLowerBoundY, mUpperBoundY;

	public Line() {
		mPoints = new ArrayList<LinePoint>();

		mColor = Color.BLACK;
		mStrokeWidth = DEFAULT_STROKE_WIDTH;

		mFill = false;
		mFillColor = Color.BLACK;
	}

	/**
	 * Retrieves all the points.
	 * @return the points
	 */
	public ArrayList<LinePoint> getPoints() {
		return mPoints;
	}

	/**
	 * Sets the points to be used on this line. Overwrites any existing points.
	 * @param points the points to use
	 */
	public void setPoints(final ArrayList<LinePoint> points) {
		mPoints.clear();
		for (final LinePoint point : points) {
			addPoint(point);
		}
	}

	/**
	 * Adds a point to the series of points with which to draw this line.
	 * @param point a point to add
	 */
	public void addPoint(final LinePoint point) {
		mPoints.add(point);
		Collections.sort(mPoints, new LinePointComparator());
		determineUpperAndLowerBounds();
	}

	private void determineUpperAndLowerBounds() {
		if (mPoints.size() > 0) {
			mLowerBoundX = mPoints.get(0).getX();
			mUpperBoundX = mPoints.get(0).getX();
			mLowerBoundY = mPoints.get(0).getY();
			mUpperBoundY = mPoints.get(0).getY();
		}

		for (final LinePoint point : mPoints) {
			if (point.getX() < mLowerBoundX) {
				mLowerBoundX = point.getX();
			} else if (point.getX() > mUpperBoundX) {
				mUpperBoundX = point.getX();
			}

			if (point.getY() < mLowerBoundY) {
				mLowerBoundY = point.getY();
			} else if (point.getY() > mUpperBoundY) {
				mUpperBoundY = point.getY();
			}
		}
	}

	/**
	 * Removes a specific point from the series.
	 * @param point the point to remove
	 */
	public void removePoint(final LinePoint point) {
		mPoints.remove(point);
		determineUpperAndLowerBounds();
	}

	/**
	 * Gets the color of the line.
	 * @return the color
	 */
	public int getColor() {
		return mColor;
	}

	/**
	 * Sets the color of the line.
	 * @param color the color
	 */
	public void setColor(final int color) {
		mColor = color;
		mFillColor = color;
	}

	public int getFillColor() {
		return mFillColor;
	}

	public void setFillType(LineGraphFillType fillType) {
		mFillType = fillType;
	}

	public LineGraphFillType getFillType() {
		return mFillType;
	}

	public void setFillOffset(int offset) {
		mFillOffset = offset;
	}

	public int getFillOffset() {
		return mFillOffset;
	}

	/**
	 * Retrieves the stroke width of the line.
	 * @return the stroke width.
	 */
	public int getStrokeWidth() {
		return mStrokeWidth;
	}

	/**
	 * Sets the stroke width to a given value.
	 * @param strokeWidth the stroke width
	 */
	public void setStrokeWidth(final int strokeWidth) {
		if (strokeWidth < 0) {
			throw new IllegalArgumentException("Stroke width must not be less than zero!");
		}
		mStrokeWidth = strokeWidth;
	}

	/**
	 * Retrieves whether or not this line should fill when drawn.
	 * @return whether or not this line should fill when drawn
	 */
	public boolean isFilled() {
		return mFill;
	}

	/**
	 * Sets whether or not this line should fill when drawn.
	 * @param shouldFill whether or not this line should fill when drawn
	 */
	public void setFill(final boolean shouldFill) {
		mFill = shouldFill;
	}

	public float getLowerBoundX() {
		return mLowerBoundX;
	}

	public float getUpperBoundX() {
		return mUpperBoundX;
	}

	public float getLowerBoundY() {
		return mLowerBoundY;
	}

	public float getUpperBoundY() {
		return mUpperBoundY;
	}
}
