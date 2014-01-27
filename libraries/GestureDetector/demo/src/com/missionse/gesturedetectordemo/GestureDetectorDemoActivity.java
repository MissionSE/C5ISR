package com.missionse.gesturedetectordemo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;

import com.missionse.gesturedetector.PanGestureDetector;
import com.missionse.gesturedetector.PanGestureDetector.OnPanGestureListener;
import com.missionse.gesturedetector.RotationGestureDetector;
import com.missionse.gesturedetector.RotationGestureDetector.OnRotationGestureListener;

/**
 * Provides an example activity that shows how to use the pan and rotate gesture detectors.
 */
public class GestureDetectorDemoActivity extends Activity implements OnPanGestureListener, OnRotationGestureListener {
	private TextView mPanText;
	private TextView mRotateText;

	private PanGestureDetector mPanGestureDetector;
	private RotationGestureDetector mRotateGestureDetector;

	private boolean mDetectPan = true;
	private boolean mDetectRotate = true;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture_detector_demo);

		mPanText = (TextView) findViewById(R.id.text_pan);
		mRotateText = (TextView) findViewById(R.id.text_rotate);

		mPanGestureDetector = new PanGestureDetector(this);
		mRotateGestureDetector = new RotationGestureDetector(this);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.gesture_detector_demo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_pan:
				mDetectPan = !item.isChecked();
				item.setChecked(mDetectPan);
				return true;
			case R.id.action_rotate:
				mDetectRotate = !item.isChecked();
				item.setChecked(mDetectRotate);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		if (mDetectPan) {
			mPanGestureDetector.onTouchEvent(event);
		}
		if (mDetectRotate) {
			mRotateGestureDetector.onTouchEvent(event);
		}
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onPan(final PanGestureDetector detector, final float distanceX, final float distanceY) {
		mPanText.setText("Pan in progress:\nX Distance: " + distanceX + "\nY Distance: " + distanceY);
		return true;
	}

	@Override
	public boolean onPanBegin(final PanGestureDetector detector, final float distanceX, final float distanceY) {
		mPanText.setTextColor(Color.BLUE);
		return true;
	}

	@Override
	public void onPanEnd() {
		mPanText.setTextColor(Color.RED);
		mPanText.setText(R.string.not_panning);
	}

	@Override
	public boolean onRotate(final RotationGestureDetector detector, final float angle) {
		mRotateText.setText("Rotation in progress:\nangle = " + angle);
		return true;
	}

	@Override
	public boolean onRotateBegin(final RotationGestureDetector detector, final float angle) {
		mRotateText.setTextColor(Color.BLUE);
		return true;
	}

	@Override
	public void onRotateEnd() {
		mRotateText.setTextColor(Color.RED);
		mRotateText.setText(R.string.not_rotating);
	}
}
