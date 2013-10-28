package com.missionse.gesturedetector;

import android.view.MotionEvent;

import com.missionse.gesturedetector.util.Line;
import com.missionse.gesturedetector.util.Point;

public class PanGestureDetector {
	private static final int INVALID_POINTER_ID = -1;
	private Line previousLine;
	private int ptrID1, ptrID2;
	private float xDistance, yDistance;
	private boolean isPanning;

	private OnPanGestureListener gestureListener;

	public PanGestureDetector(final OnPanGestureListener listener) {
		gestureListener = listener;
		ptrID1 = INVALID_POINTER_ID;
		ptrID2 = INVALID_POINTER_ID;
		xDistance = 0f;
		yDistance = 0f;
		previousLine = new Line();
	}

	public float getDistanceX() {
		return xDistance;
	}

	public float getDistanceY() {
		return yDistance;
	}

	public boolean isPanning() {
		return isPanning;
	}

	public boolean onTouchEvent(final MotionEvent event) {
		switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				ptrID1 = event.getPointerId(event.getActionIndex());
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				if (ptrID1 == INVALID_POINTER_ID)
					ptrID1 = event.getPointerId(event.getActionIndex());
				else
					ptrID2 = event.getPointerId(event.getActionIndex());

				if (ptrID1 != INVALID_POINTER_ID && ptrID2 != INVALID_POINTER_ID) {
					unpackLinePosition(event, previousLine);
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if(ptrID1 != INVALID_POINTER_ID && ptrID2 != INVALID_POINTER_ID) {

					Line currentLine = new Line();
					unpackLinePosition(event, currentLine);

					calculateDistanceBetweenLines(previousLine, currentLine);

					if (!isPanning) {
						startPan();
					}
					if (gestureListener != null) {
						gestureListener.onPan(this, getDistanceX(), getDistanceY());
					}

					previousLine = currentLine;
				}
				break;
			case MotionEvent.ACTION_UP:
				clearPointerIndex(event.getPointerId(event.getActionIndex()));
				break;
			case MotionEvent.ACTION_POINTER_UP:
				clearPointerIndex(event.getPointerId(event.getActionIndex()));
				break;
		}
		return true;
	}

	private void unpackLinePosition(final MotionEvent event, final Line line) {
		line.setX1(event.getX(event.findPointerIndex(ptrID1)));
		line.setY1(event.getY(event.findPointerIndex(ptrID1)));
		line.setX2(event.getX(event.findPointerIndex(ptrID2)));
		line.setY2(event.getY(event.findPointerIndex(ptrID2)));
	}

	private void calculateDistanceBetweenLines (final Line line1, final Line line2) {
		Point center1 = line1.getCenter();
		Point center2 = line2.getCenter();

		xDistance = center2.getX() - center1.getX();
		yDistance = center2.getY() - center1.getY();
	}

	private void clearPointerIndex(final int pointerIndex) {
		if (ptrID1 == pointerIndex) {
			ptrID1 = INVALID_POINTER_ID;
			if (isPanning) {
				endPan();
			}
		}
		else if (ptrID2 == pointerIndex) {
			ptrID2 = INVALID_POINTER_ID;
			if (isPanning) {
				endPan();
			}
		}
	}

	private void startPan() {
		isPanning = true;
		if (gestureListener != null) {
			gestureListener.onPanBegin(this, getDistanceX(), getDistanceY());
		}
	}

	private void endPan() {
		isPanning = false;
		if (gestureListener != null) {
			gestureListener.onPanEnd();
		}
	}

	public static interface OnPanGestureListener {
		public boolean onPan(PanGestureDetector detector, float distanceX, float distanceY);
		public boolean onPanBegin(PanGestureDetector detector, float distanceX, float distanceY);
		public void onPanEnd();
	}
}