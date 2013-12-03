package com.missionse.modelviewer.impl;

import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.missionse.gesturedetector.PanGestureDetector;
import com.missionse.gesturedetector.RotationGestureDetector;
import com.missionse.modelviewer.ModelViewerGestureListener;

/**
 * Provides a base gesture listener that implements all of the types of listeners.
 */
public class ModelGestureListener extends ModelViewerGestureListener {
	private static final double TRANSLATION_SCALE = 100.0;
	private static final double ROTATION_SCALE = 2 * Math.PI;

	@Override
	public boolean onScroll(final MotionEvent e1, final MotionEvent e2, final float distanceX, final float distanceY) {
		if (e2.getPointerCount() == 1) {
			getController().translate(-distanceX / TRANSLATION_SCALE, distanceY / TRANSLATION_SCALE, 0);
		}
		return true;
	}

	@Override
	public boolean onScale(final ScaleGestureDetector detector) {
		getController().scale(detector.getScaleFactor());
		return true;
	}

	@Override
	public boolean onRotate(final RotationGestureDetector detector, final float angle) {
		getController().rotateAround(0f, 0f, -detector.getAngle());
		return true;
	}

	@Override
	public boolean onPan(final PanGestureDetector detector, final float distanceX, final float distanceY) {
		getController().rotateAround(-distanceY / ROTATION_SCALE, -distanceX / ROTATION_SCALE, 0f);
		return true;
	}

	@Override
	public boolean onSingleTapUp(final MotionEvent e) {
		getController().getObjectAt(e.getX(), e.getY());
		return true;
	}
}
