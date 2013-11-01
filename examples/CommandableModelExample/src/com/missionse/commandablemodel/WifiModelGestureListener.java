package com.missionse.commandablemodel;

import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.missionse.gesturedetector.PanGestureDetector;
import com.missionse.gesturedetector.RotationGestureDetector;
import com.missionse.modelviewer.ModelViewerGestureListener;

public class WifiModelGestureListener extends ModelViewerGestureListener {

	private final ModelNotifier notifier;

	public WifiModelGestureListener(final ModelNotifier modelNotifier) {
		notifier = modelNotifier;
	}

	@Override
	public boolean onScroll(final MotionEvent e1, final MotionEvent e2, final float distanceX, final float distanceY) {
		if (e2.getPointerCount() == 1) {
			controller.translate(-distanceX / 100.0f, distanceY / 100.0f, 0);
		}
		notifier.sendModelStatus();
		return true;
	}

	@Override
	public boolean onScale(final ScaleGestureDetector detector) {
		controller.scale(detector.getScaleFactor());
		notifier.sendModelStatus();
		return true;
	}

	@Override
	public boolean onRotate(final RotationGestureDetector detector, final float angle) {
		controller.rotate(0f, 0f, -detector.getAngle());
		notifier.sendModelStatus();
		return true;
	}

	@Override
	public boolean onPan(final PanGestureDetector detector, final float distanceX, final float distanceY) {
		controller.rotate(-distanceX / 3f, -distanceY / 3f, 0);
		notifier.sendModelStatus();
		return true;
	}
}
