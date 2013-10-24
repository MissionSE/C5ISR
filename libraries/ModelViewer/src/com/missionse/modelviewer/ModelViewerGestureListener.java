package com.missionse.modelviewer;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.missionse.modelviewer.ModelViewerFragment.ModelViewerRenderer;
import com.missionse.rotationgesturedetector.RotationGestureDetector;

public class ModelViewerGestureListener implements
		GestureDetector.OnGestureListener,
		ScaleGestureDetector.OnScaleGestureListener,
		RotationGestureDetector.OnRotationGestureListener {

	private ModelViewerRenderer renderer;
	private float lastRotation;
	private boolean isRotating;

	public ModelViewerGestureListener(final ModelViewerRenderer modelRenderer) {
		renderer = modelRenderer;
		lastRotation = 0.0f;
		isRotating = false;
	}

	@Override
	public boolean onDown(final MotionEvent e) {
		isRotating = false;
		lastRotation = 0.0f;
		return true;
	}

	@Override
	public boolean onScroll(final MotionEvent e1, final MotionEvent e2, final float distanceX, final float distanceY) {
		renderer.rotate(distanceX / 6.0f, distanceY / 6.0f, 0);
		return true;
	}

	@Override
	public boolean onScale(final ScaleGestureDetector detector) {
		renderer.scale(detector.getScaleFactor());
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
	public boolean onFling(final MotionEvent e1, final MotionEvent e2, final float velocityX,
			final float velocityY) {
		return false;
	}

	@Override
	public void onLongPress(final MotionEvent e) {
		renderer.setCameraAnimation(!renderer.isCameraAnimating());
	}

	@Override
	public void onShowPress(final MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(final MotionEvent e) {
		return false;
	}

	@Override
	public void onRotate(final RotationGestureDetector detector) {

		if (!isRotating) {
			lastRotation = detector.getAngle();
			isRotating = true;
		}

		float rotationDifference = lastRotation - detector.getAngle();
		lastRotation = detector.getAngle();
		renderer.rotate(0f, 0f, rotationDifference);
	}
}
