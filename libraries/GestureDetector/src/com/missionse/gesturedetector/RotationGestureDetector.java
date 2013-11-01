package com.missionse.gesturedetector;

import android.view.MotionEvent;

import com.missionse.gesturedetector.util.Line;

public class RotationGestureDetector {
	private static final int INVALID_POINTER_ID = -1;
	private Line previousLine;
	private int ptrID1, ptrID2;
	private float rotationAngle;
	private boolean isRotating;

	private OnRotationGestureListener gestureListener;

	public RotationGestureDetector(final OnRotationGestureListener listener) {
		gestureListener = listener;
		ptrID1 = INVALID_POINTER_ID;
		ptrID2 = INVALID_POINTER_ID;
		previousLine = new Line();
	}

	public float getAngle() {
		return rotationAngle;
	}

	public boolean isRotating() {
		return isRotating;
	}

	public boolean onTouchEvent(final MotionEvent event) {
		switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				ptrID1 = event.getPointerId(event.getActionIndex());
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				if (ptrID1 == INVALID_POINTER_ID) {
					ptrID1 = event.getPointerId(event.getActionIndex());
				} else {
					ptrID2 = event.getPointerId(event.getActionIndex());
				}

				if (ptrID1 != INVALID_POINTER_ID && ptrID2 != INVALID_POINTER_ID) {
					unpackLinePosition(event, previousLine);
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if(ptrID1 != INVALID_POINTER_ID && ptrID2 != INVALID_POINTER_ID) {

					Line currentLine = new Line();
					unpackLinePosition(event, currentLine);

					rotationAngle = angleBetweenLines(previousLine, currentLine);

					if (!isRotating) {
						startRotation();
					}
					if (gestureListener != null) {
						gestureListener.onRotate(this, rotationAngle);
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

	private float angleBetweenLines (final Line line1, final Line line2) {
		float angle1 = (float) Math.atan2((line1.getY2() - line1.getY1()), (line1.getX2() - line1.getX1()));
		float angle2 = (float) Math.atan2((line2.getY2() - line2.getY1()), (line2.getX2() - line2.getX1()));

		float angle = ((float)Math.toDegrees(angle1 - angle2)) % 360;
		if (angle < -180.f) angle += 360.0f;
		if (angle > 180.f) angle -= 360.0f;
		return angle;
	}

	private void clearPointerIndex(final int pointerIndex) {
		if (ptrID1 == pointerIndex) {
			ptrID1 = INVALID_POINTER_ID;
			if (isRotating) {
				endRotation();
			}
		}
		else if (ptrID2 == pointerIndex) {
			ptrID2 = INVALID_POINTER_ID;
			if (isRotating) {
				endRotation();
			}
		}
	}

	private void startRotation() {
		isRotating = true;
		if (gestureListener != null) {
			gestureListener.onRotateBegin(this, getAngle());
		}
	}

	private void endRotation() {
		isRotating = false;
		if (gestureListener != null) {
			gestureListener.onRotateEnd();
		}
	}

	public static interface OnRotationGestureListener {
		public boolean onRotate(RotationGestureDetector detector, float angle);
		public boolean onRotateBegin(RotationGestureDetector detector, float angle);
		public void onRotateEnd();
	}
}