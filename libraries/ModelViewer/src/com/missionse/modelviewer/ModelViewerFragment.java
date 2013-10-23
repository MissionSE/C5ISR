package com.missionse.modelviewer;

import rajawali.RajawaliFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class ModelViewerFragment extends RajawaliFragment implements OnTouchListener {
	protected ModelViewerRenderer renderer;
	public static final String ARG_MODEL_ID = "model_id";

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Bundle arguments = getArguments();
		final int modelID = arguments.getInt(ARG_MODEL_ID);
		if (0 == modelID) {
			throw new RuntimeException("ModelViewerFragment passed invalid model id.");
		}

		renderer = new ModelViewerRenderer(getActivity(), modelID);
		renderer.setSurfaceView(mSurfaceView);
		setRenderer(renderer);

		mSurfaceView.setOnTouchListener(this);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
			final Bundle savedInstanceState) {
		mLayout = (FrameLayout) inflater.inflate(R.layout.fragment_model_viewer, container, false);
		mLayout.addView(mSurfaceView);

		return mLayout;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		renderer.onSurfaceDestroyed();
	}

	@Override
	public boolean onTouch(final View v, final MotionEvent event) {
		boolean touchConsumed = false;

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			renderer.setCameraAnimation(!renderer.isCameraAnimating());
			touchConsumed = true;
		}
		return touchConsumed;
	}
}
