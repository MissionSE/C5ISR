package com.missionse.gesturedetector.util;

public class Point {
	private float x, y;

	public Point() {
		this(0.0f, 0.0f);
	}

	public Point(final float xPosition, final float yPosition) {
		setX (xPosition);
		setY (yPosition);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setX(final float xPosition) {
		x = xPosition;
	}

	public void setY(final float yPosition) {
		y = yPosition;
	}
}
