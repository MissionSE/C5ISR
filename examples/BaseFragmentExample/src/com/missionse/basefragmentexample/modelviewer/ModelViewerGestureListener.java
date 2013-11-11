package com.missionse.basefragmentexample.modelviewer;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.missionse.basefragmentexample.gesturedetector.PanGestureDetector;
import com.missionse.basefragmentexample.gesturedetector.RotationGestureDetector;

public class ModelViewerGestureListener implements
		GestureDetector.OnGestureListener,
		ScaleGestureDetector.OnScaleGestureListener,
		RotationGestureDetector.OnRotationGestureListener,
		PanGestureDetector.OnPanGestureListener {

	protected ModelController controller;

	public void setController(final ModelController modelController) {
		controller = modelController;
	}

	@Override
	public boolean onDown(final MotionEvent e) {
		return true;
	}

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
	public boolean onScaleBegin(final ScaleGestureDetector detector) {
		return true;
	}

	@Override
	public void onScaleEnd(final ScaleGestureDetector detector) {
	}

	@Override
	public boolean onFling(final MotionEvent e1, final MotionEvent e2, final float velocityX, final float velocityY) {
		return false;
	}

	@Override
	public void onLongPress(final MotionEvent e) {
	}

	@Override
	public void onShowPress(final MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(final MotionEvent e) {
		controller.getObjectAt(e.getX(), e.getY());
		return true;
	}

	@Override
	public boolean onRotate(final RotationGestureDetector detector, final float angle) {
		controller.rotate(0f, 0f, -detector.getAngle());
		return true;
	}

	@Override
	public boolean onRotateBegin(final RotationGestureDetector detector, final float angle) {
		return true;
	}

	@Override
	public void onRotateEnd() {
	}

	@Override
	public boolean onPan(final PanGestureDetector detector, final float distanceX, final float distanceY) {
		controller.rotate(-distanceX / 3f, -distanceY / 3f, 0);
		return true;
	}

	@Override
	public boolean onPanBegin(final PanGestureDetector detector, final float distanceX, final float distanceY) {
		return true;
	}

	@Override
	public void onPanEnd() {
	}
}
