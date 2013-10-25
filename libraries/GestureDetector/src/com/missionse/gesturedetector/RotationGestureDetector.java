package com.missionse.gesturedetector;

import android.view.MotionEvent;

public class RotationGestureDetector {
	private static final int INVALID_POINTER_ID = -1;
	private float lastX1, lastY1, lastX2, lastY2;
	private int ptrID1, ptrID2;
	private float rotationAngle;
	private boolean isRotating;

	private OnRotationGestureListener gestureListener;

	public float getAngle() {
		return rotationAngle;
	}

	public boolean isRotating() {
		return isRotating;
	}

	public RotationGestureDetector(final OnRotationGestureListener listener) {
		gestureListener = listener;
		ptrID1 = INVALID_POINTER_ID;
		ptrID2 = INVALID_POINTER_ID;
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
					lastX1 = event.getX(event.findPointerIndex(ptrID1));
					lastY1 = event.getY(event.findPointerIndex(ptrID1));
					lastX2 = event.getX(event.findPointerIndex(ptrID2));
					lastY2 = event.getY(event.findPointerIndex(ptrID2));
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if(ptrID1 != INVALID_POINTER_ID && ptrID2 != INVALID_POINTER_ID) {
					float currentX1, currentY1, currentX2, currentY2;

					currentX1 = event.getX(event.findPointerIndex(ptrID1));
					currentY1 = event.getY(event.findPointerIndex(ptrID1));
					currentX2 = event.getX(event.findPointerIndex(ptrID2));
					currentY2 = event.getY(event.findPointerIndex(ptrID2));

					rotationAngle = angleBetweenLines(lastX2, lastY2, lastX1, lastY1, currentX2, currentY2, currentX1, currentY1);

					if (!isRotating) {
						startRotation();
					}
					if (gestureListener != null) {
						gestureListener.onRotate(this);
					}

					lastX1 = currentX1;
					lastY1 = currentY1;
					lastX2 = currentX2;
					lastY2 = currentY2;
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

	private float angleBetweenLines (final float fX, final float fY, final float sX, final float sY, final float nfX, final float nfY, final float nsX, final float nsY) {
		float angle1 = (float) Math.atan2( (fY - sY), (fX - sX) );
		float angle2 = (float) Math.atan2( (nfY - nsY), (nfX - nsX) );

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
			gestureListener.onRotateStart(this);
		}
	}

	private void endRotation() {
		isRotating = false;
		if (gestureListener != null) {
			gestureListener.onRotateEnd();
		}
	}

	public static interface OnRotationGestureListener {
		public boolean onRotate(RotationGestureDetector detector);
		public boolean onRotateStart(RotationGestureDetector detector);
		public void onRotateEnd();
	}
}