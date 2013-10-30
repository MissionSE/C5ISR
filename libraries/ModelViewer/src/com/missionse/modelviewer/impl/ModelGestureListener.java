package com.missionse.modelviewer.impl;

import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.missionse.gesturedetector.PanGestureDetector;
import com.missionse.gesturedetector.RotationGestureDetector;
import com.missionse.modelviewer.ModelViewerGestureListener;

public class ModelGestureListener extends ModelViewerGestureListener {
	@Override
	public boolean onScroll(final MotionEvent e1, final MotionEvent e2, final float distanceX, final float distanceY) {
		if (e2.getPointerCount() == 1) {
			controller.translate(-distanceX / 100.0f, distanceY / 100.0f, 0);
		}
		return true;
	}

	@Override
	public boolean onScale(final ScaleGestureDetector detector) {
		controller.scale(detector.getScaleFactor());
		return true;
	}

	@Override
	public boolean onRotate(final RotationGestureDetector detector, final float angle) {
		controller.rotate(0f, 0f, -detector.getAngle());
		return true;
	}

	@Override
	public boolean onPan(final PanGestureDetector detector, final float distanceX, final float distanceY) {
		controller.rotate(-distanceX / 3f, -distanceY / 3f, 0);
		return true;
	}

	@Override
	public boolean onSingleTapUp(final MotionEvent e) {
		controller.getObjectAt(e.getX(), e.getY());
		return true;
	}
}
