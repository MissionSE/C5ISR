package com.missionse.commandablemodel.network;

import java.util.ArrayList;
import java.util.List;

import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.missionse.gesturedetector.PanGestureDetector;
import com.missionse.gesturedetector.RotationGestureDetector;
import com.missionse.modelviewer.ModelViewerGestureListener;

/**
 * Extends the ModelViewerGestureListener, notifying a mRecipients of change.
 */
public class NotifyingModelGestureListener extends ModelViewerGestureListener {

	private static final double TRANSLATION_SCALE = 100.0;
	private static final double ROTATION_SCALE = 2 * Math.PI;

	private final List<ModelChangeRecipient> mRecipients = new ArrayList<ModelChangeRecipient>();

	/**
	 * Adds a recipient to be notified of gestures.
	 * @param recipient the entity to be notified
	 */
	public void addRecipient(final ModelChangeRecipient recipient) {
		mRecipients.add(recipient);
	}

	@Override
	public boolean onScroll(final MotionEvent e1, final MotionEvent e2, final float distanceX, final float distanceY) {
		if (e2.getPointerCount() == 1) {
			getController().translate(-distanceX / TRANSLATION_SCALE, distanceY / TRANSLATION_SCALE, 0);
		}
		notifyRecipients();
		return true;
	}

	@Override
	public boolean onScale(final ScaleGestureDetector detector) {
		getController().scale(detector.getScaleFactor());
		notifyRecipients();
		return true;
	}

	@Override
	public boolean onRotate(final RotationGestureDetector detector, final float angle) {
		getController().rotateAround(0f, 0f, -detector.getAngle());
		notifyRecipients();
		return true;
	}

	@Override
	public boolean onPan(final PanGestureDetector detector, final float distanceX, final float distanceY) {
		getController().rotateAround(-distanceY / ROTATION_SCALE, -distanceX / ROTATION_SCALE, 0);
		notifyRecipients();
		return true;
	}

	private void notifyRecipients() {
		for (ModelChangeRecipient recipient : mRecipients) {
			recipient.onModelChange();
		}
	}
}
