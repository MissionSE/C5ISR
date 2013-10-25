package com.missionse.modelviewer;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.missionse.gesturedetector.RotationGestureDetector;
import com.missionse.modelviewer.ModelViewerFragment.ModelViewerRenderer;

public class ModelViewerGestureListener implements
		GestureDetector.OnGestureListener,
		ScaleGestureDetector.OnScaleGestureListener,
		RotationGestureDetector.OnRotationGestureListener {

	private ModelViewerRenderer renderer;

	public ModelViewerGestureListener(final ModelViewerRenderer modelRenderer) {
		renderer = modelRenderer;
	}

	@Override
	public boolean onDown(final MotionEvent e) {
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
	}

	@Override
	public void onShowPress(final MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(final MotionEvent e) {
		return false;
	}

	@Override
	public boolean onRotate(final RotationGestureDetector detector) {
		renderer.rotate(0f, 0f, -detector.getAngle());
		return true;
	}

	@Override
	public boolean onRotateStart(final RotationGestureDetector detector) {
		return true;
	}

	@Override
	public void onRotateEnd() {
	}
}
