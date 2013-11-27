package com.missionse.gesturedetector;

import android.view.MotionEvent;

import com.missionse.gesturedetector.util.Line;
import com.missionse.gesturedetector.util.Point;

/**
 * Provides a gesture detector to detect panning.
 * Panning is considered to be a two finger swipe.
 */
public class PanGestureDetector {
	private static final int INVALID_POINTER_ID = -1;
	private Line mPreviousLine;
	private int mPointerId1, mPointerId2;
	private float mXDistance, mYDistance;
	private boolean mIsPanning;

	private OnPanGestureListener mGestureListener;

	/**
	 * Constructor.
	 * @param listener The gesture listener that will receive callbacks.
	 */
	public PanGestureDetector(final OnPanGestureListener listener) {
		mGestureListener = listener;
		mPointerId1 = INVALID_POINTER_ID;
		mPointerId2 = INVALID_POINTER_ID;
		mXDistance = 0f;
		mYDistance = 0f;
		mPreviousLine = new Line();
	}

	/**
	 * Gets the x distance of the pan.
	 * @return The x distance of the pan.
	 */
	public float getDistanceX() {
		return mXDistance;
	}

	/**
	 * Gets the y distance of the pan.
	 * @return The y distance of the pan.
	 */
	public float getDistanceY() {
		return mYDistance;
	}

	/**
	 * Determines whether a pan gesture is in progress.
	 * @return Whether a pan gesture is in progress.
	 */
	public boolean isPanning() {
		return mIsPanning;
	}

	/**
	 * Processes a touch motion event and calculates the panning gesture.
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
					mPointerId2 = event.getPointerId(event.getActionIndex());
				}

				if (mPointerId1 != INVALID_POINTER_ID && mPointerId2 != INVALID_POINTER_ID) {
					unpackLinePosition(event, mPreviousLine);
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (mPointerId1 != INVALID_POINTER_ID && mPointerId2 != INVALID_POINTER_ID) {
					Line currentLine = new Line();
					unpackLinePosition(event, currentLine);

					calculateDistanceBetweenLines(mPreviousLine, currentLine);

					if (!mIsPanning) {
						startPan();
					}
					if (mGestureListener != null) {
						mGestureListener.onPan(this, getDistanceX(), getDistanceY());
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
		line.setX2(event.getX(event.findPointerIndex(mPointerId2)));
		line.setY2(event.getY(event.findPointerIndex(mPointerId2)));
	}

	private void calculateDistanceBetweenLines(final Line line1, final Line line2) {
		Point center1 = line1.getCenter();
		Point center2 = line2.getCenter();

		mXDistance = center2.getX() - center1.getX();
		mYDistance = center2.getY() - center1.getY();
	}

	private void clearPointerIndex(final int pointerIndex) {
		if (mPointerId1 == pointerIndex) {
			mPointerId1 = INVALID_POINTER_ID;
			if (mIsPanning) {
				endPan();
			}
		} else if (mPointerId2 == pointerIndex) {
			mPointerId2 = INVALID_POINTER_ID;
			if (mIsPanning) {
				endPan();
			}
		}
	}

	private void startPan() {
		mIsPanning = true;
		if (mGestureListener != null) {
			mGestureListener.onPanBegin(this, getDistanceX(), getDistanceY());
		}
	}

	private void endPan() {
		mIsPanning = false;
		if (mGestureListener != null) {
			mGestureListener.onPanEnd();
		}
	}

	/**
	 * Provides callbacks to process pan gestures.
	 */
	public interface OnPanGestureListener {
		/**
		 * Called on a motion event when a pan is detected.
		 * @param detector The pan gesture detector.
		 * @param distanceX The x distance of the pan.
		 * @param distanceY The y distance of the pan.
		 * @return Whether the touch was consumed.
		 */
		boolean onPan(PanGestureDetector detector, float distanceX, float distanceY);

		/**
		 * Called on receipt of a motion event when a pan is detected to begin.
		 * @param detector The pan gesture detector.
		 * @param distanceX The x distance of the pan.
		 * @param distanceY The y distance of the pan.
		 * @return Whether the touch was consumed.
		 */
		boolean onPanBegin(PanGestureDetector detector, float distanceX, float distanceY);

		/**
		 * Called on receipt of a motion event when a pan is detected to end.
		 */
		void onPanEnd();
	}
}