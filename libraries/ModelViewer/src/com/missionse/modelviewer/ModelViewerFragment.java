package com.missionse.modelviewer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import rajawali.RajawaliFragment;
import rajawali.renderer.RajawaliRenderer;
import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.missionse.gesturedetector.PanGestureDetector;
import com.missionse.gesturedetector.RotationGestureDetector;

public abstract class ModelViewerFragment extends RajawaliFragment implements OnTouchListener {
	protected ModelViewerRenderer renderer;
	protected ProgressBar progressBar;

	private ModelViewerGestureListener gestureListener;
	private GestureDetector gestureDetector;
	private ScaleGestureDetector scaleGestureDetector;
	private RotationGestureDetector rotationGestureDetector;
	private PanGestureDetector panGestureDetector;

	public static final String ARG_MODEL_ID = "model_id";

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Bundle arguments = getArguments();
		final int modelID = arguments.getInt(ARG_MODEL_ID);
		if (0 == modelID) {
			throw new RuntimeException("ModelViewerFragment passed invalid model id.");
		}

		if (isTransparentSurfaceView())
			setGLBackgroundTransparent(true);

		renderer = createRenderer(modelID);
		renderer.setSurfaceView(mSurfaceView);
		setRenderer(renderer);

		gestureListener = new ModelViewerGestureListener(renderer);
		gestureDetector = new GestureDetector(getActivity(), gestureListener);
		scaleGestureDetector = new ScaleGestureDetector(getActivity(), gestureListener);
		rotationGestureDetector = new RotationGestureDetector(gestureListener);
		panGestureDetector = new PanGestureDetector(gestureListener);

		mSurfaceView.setOnTouchListener(this);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
			final Bundle savedInstanceState) {
		mLayout = (FrameLayout) inflater.inflate(R.layout.fragment_model_viewer, container, false);

		if (mSurfaceView.getParent() != null) {
			((ViewGroup) mSurfaceView.getParent()).removeView(mSurfaceView);
		}

		mSurfaceView.setId(R.id.content_frame);
		mLayout.addView(mSurfaceView);

		mLayout.findViewById(R.id.progress_bar_container).bringToFront();
		progressBar = (ProgressBar) mLayout.findViewById(R.id.progress_bar);

		return mLayout;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		renderer.onSurfaceDestroyed();
	}

	protected void hideLoader() {
		progressBar.post(new Runnable() {
			@Override
			public void run() {
				progressBar.setVisibility(View.GONE);
			}
		});
	}

	protected boolean isTransparentSurfaceView() {
		return false;
	}

	protected void showLoader() {
		progressBar.post(new Runnable() {
			@Override
			public void run() {
				progressBar.setVisibility(View.VISIBLE);
			}
		});
	}

	@Override
	public boolean onTouch(final View v, final MotionEvent event) {

		boolean touchConsumed = scaleGestureDetector.onTouchEvent(event);
		rotationGestureDetector.onTouchEvent(event);
		panGestureDetector.onTouchEvent(event);
		if (!scaleGestureDetector.isInProgress())
			touchConsumed = gestureDetector.onTouchEvent(event);

		return touchConsumed;
	}

	public void setAutoRotation(final boolean autoRotate) {
		renderer.setAutoRotation(autoRotate);
	}

	public boolean isAutoRotating() {
		return renderer.isAutoRotating();
	}


	protected abstract ModelViewerRenderer createRenderer(final int modelID);

	protected abstract class ModelViewerRenderer extends RajawaliRenderer {
		public ModelViewerRenderer(final Context context) {
			super(context);
			setFrameRate(60);
		}

		public void onSurfaceCreated(final GL10 gl, final EGLConfig config) {
			showLoader();
			super.onSurfaceCreated(gl, config);
		}

		@Override
		public void onDrawFrame(final GL10 glUnused) {
			super.onDrawFrame(glUnused);
			if (progressBar.isShown())
				hideLoader();
		}

		protected abstract void setAutoRotation(final boolean autoRotate);

		protected abstract boolean isAutoRotating();

		protected abstract void rotate(final float xAngle, final float yAngle, final float zAngle);

		protected abstract void scale(final float scaleFactor);

		protected abstract void translate(final float xDistance, final float yDistance, final float zDistance);
	}
}
