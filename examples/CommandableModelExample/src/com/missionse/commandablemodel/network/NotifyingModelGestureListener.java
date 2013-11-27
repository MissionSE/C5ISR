package com.missionse.commandablemodel.network;

import java.util.ArrayList;
import java.util.List;

import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.missionse.gesturedetector.PanGestureDetector;
import com.missionse.gesturedetector.RotationGestureDetector;
import com.missionse.modelviewer.ModelViewerGestureListener;

public class NotifyingModelGestureListener extends ModelViewerGestureListener {

	private final List<ModelChangeRecipient> recipients = new ArrayList<ModelChangeRecipient>();

	public NotifyingModelGestureListener() {
	}

	public void addRecipient(final ModelChangeRecipient recipient) {
		recipients.add(recipient);
	}

	@Override
	public boolean onScroll(final MotionEvent e1, final MotionEvent e2, final float distanceX, final float distanceY) {
		if (e2.getPointerCount() == 1) {
			getController().translate(-distanceX / 100.0f, distanceY / 100.0f, 0);
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
		getController().rotateAround(-distanceY / 3f, -distanceX / 3f, 0);
		notifyRecipients();
		return true;
	}

	private void notifyRecipients() {
		for (ModelChangeRecipient recipient : recipients) {
			recipient.onModelChange();
		}
	}
}
