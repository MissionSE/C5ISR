package com.missionse.gesturedetector.util;

public class Line {
	private Point point1, point2;

	public Line() {
		this(new Point(), new Point());
	}

	public Line(final float x1, final float y1, final float x2, final float y2) {
		this(new Point(x1, y1), new Point(x2, y2));
	}

	public Line(final Point firstPoint, final Point secondPoint) {
		setPoint1 (firstPoint);
		setPoint2 (secondPoint);
	}

	public Point getCenter() {
		return new Point((getX1() + getX2()) / 2, (getY1() + getY2()) / 2);
	}

	public Point getPoint1() {
		return point1;
	}

	public Point getPoint2() {
		return point2;
	}

	public void setPoint1(final Point firstPoint) {
		point1 = firstPoint;
	}

	public void setPoint2(final Point secondPoint) {
		point2 = secondPoint;
	}

	public float getX1() {
		return point1.getX();
	}

	public float getY1() {
		return point1.getY();
	}

	public float getX2() {
		return point2.getX();
	}

	public float getY2() {
		return point2.getY();
	}

	public void setX1(final float x1) {
		point1.setX(x1);
	}

	public void setY1(final float y1) {
		point1.setY(y1);
	}

	public void setX2(final float x2) {
		point2.setX(x2);
	}

	public void setY2(final float y2) {
		point2.setY(y2);
	}
}
