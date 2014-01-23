package com.missionse.gesturedetector;

import android.view.MotionEvent;

import com.missionse.gesturedetector.util.Line;

/**
 * Provides a gesture detector to detect rotation.
 * Rotation is considered to be a two finger change.
 */
public class RotationGestureDetector {
	private static final int INVALID_POINTER_ID = -1;
	private static final float HALF_ROTATION = 180.0f;
	private static final float FULL_ROTATION = 360.0f;

	private Line mPreviousLine;
	private int mPointerId1, mPointterId2;
	private float mRotationAngle;
	private boolean mIsRotating;

	private OnRotationGestureListener mGestureListener;

	/**
	 * Constructor.
	 * @param listener The gesture listener that will receive callbacks.
	 */
	public RotationGestureDetector(final OnRotationGestureListener listener) {
		mGestureListener = listener;
		mPointerId1 = INVALID_POINTER_ID;
		mPointterId2 = INVALID_POINTER_ID;
		mPreviousLine = new Line();
	}

	/**
	 * Gets the angle of the rotation.
	 * @return The angle of the rotation.
	 */
	public float getAngle() {
		return mRotationAngle;
	}

	/**
	 * Determines whether a rotate gesture is in progress.
	 * @return Whether a rotate gesture is in progress.
	 */
	public boolean isRotating() {
		return mIsRotating;
	}

	/**
	 * Processes a touch motion event and calculates the rotation gesture.
	 * @param event The motion event that occurred.
	 * @return Whether the touch was consumed.
	 */
	public boolean onTouchEvent(final MotionEvent event) {
		switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				mPointerId1 = event.getPointerId(event.getActionIndex());
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				if (mPointerId1 == INVALID_POINTER_ID) {
					mPointerId1 = event.getPointerId(event.getActionIndex());
				} else {
					mPointterId2 = event.getPointerId(event.getActionIndex());
				}

				if (mPointerId1 != INVALID_POINTER_ID && mPointterId2 != INVALID_POINTER_ID) {
					unpackLinePosition(event, mPreviousLine);
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (mPointerId1 != INVALID_POINTER_ID && mPointterId2 != INVALID_POINTER_ID) {

					Line currentLine = new Line();
					unpackLinePosition(event, currentLine);

					mRotationAngle = angleBetweenLines(mPreviousLine, currentLine);

					if (!mIsRotating) {
						startRotation();
					}
					if (mGestureListener != null) {
						mGestureListener.onRotate(this, mRotationAngle);
					}

					mPreviousLine = currentLine;
				}
				break;
			case MotionEvent.ACTION_UP:
				clearPointerIndex(event.getPointerId(event.getActionIndex()));
				break;
			case MotionEvent.ACTION_POINTER_UP:
				clearPointerIndex(event.getPointerId(event.getActionIndex()));
				break;
			default:
				break;
		}
		return true;
	}

	private void unpackLinePosition(final MotionEvent event, final Line line) {
		line.setX1(event.getX(event.findPointerIndex(mPointerId1)));
		line.setY1(event.getY(event.findPointerIndex(mPointerId1)));
		line.setX2(event.getX(event.findPointerIndex(mPointterId2)));
		line.setY2(event.getY(event.findPointerIndex(mPointterId2)));
	}

	private float angleBetweenLines(final Line line1, final Line line2) {
		float angle1 = (float) Math.atan2((line1.getY2() - line1.getY1()), (line1.getX2() - line1.getX1()));
		float angle2 = (float) Math.atan2((line2.getY2() - line2.getY1()), (line2.getX2() - line2.getX1()));

		float angle = ((float) Math.toDegrees(angle1 - angle2)) % FULL_ROTATION;
		if (angle < -HALF_ROTATION) {
			angle += FULL_ROTATION;
		}
		if (angle > HALF_ROTATION) {
			angle -= FULL_ROTATION;
		}
		return angle;
	}

	private void clearPointerIndex(final int pointerIndex) {
		if (mPointerId1 == pointerIndex) {
			mPointerId1 = INVALID_POINTER_ID;
			if (mIsRotating) {
				endRotation();
			}
		} else if (mPointterId2 == pointerIndex) {
			mPointterId2 = INVALID_POINTER_ID;
			if (mIsRotating) {
				endRotation();
			}
		}
	}

	private void startRotation() {
		mIsRotating = true;
		if (mGestureListener != null) {
			mGestureListener.onRotateBegin(this, getAngle());
		}
	}

	private void endRotation() {
		mIsRotating = false;
		if (mGestureListener != null) {
			mGestureListener.onRotateEnd();
		}
	}

	/**
	 * Provides callbacks to process rotation gestures.
	 */
	public interface OnRotationGestureListener {
		/**
		 * Called on receipt of a motion event when a rotation is detected.
		 * @param detector The rotation gesture detector.
		 * @param angle The angle of the rotation in degrees.
		 * @return Whether the touch was consumed.
		 */
		boolean onRotate(RotationGestureDetector detector, float angle);

		/**
		 * Called on receipt of a motion event when a rotation gesture is detected to begin.
		 * @param detector The rotation gesture detector.
		 * @param angle The angle of the rotation in degrees.
		 * @return Whether the touch was consumed.
		 */
		boolean onRotateBegin(RotationGestureDetector detector, float angle);

		/**
		 * Called on receipt of a motion event when a rotation gesture is detected to end.
		 */
		void onRotateEnd();
	}
}