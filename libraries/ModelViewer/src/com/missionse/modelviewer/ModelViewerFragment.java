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
import com.missionse.modelviewer.impl.ModelControlListener;

public abstract class ModelViewerFragment extends RajawaliFragment implements OnTouchListener {
	protected ModelParser parser;
	protected ModelViewerRenderer renderer;
	protected ProgressBar progressBar;

	private ModelViewerGestureListener gestureListener;
	private GestureDetector gestureDetector;
	private ScaleGestureDetector scaleGestureDetector;
	private RotationGestureDetector rotationGestureDetector;
	private PanGestureDetector panGestureDetector;
	private ObjectSelectedListener objectSelectedListener;

	private boolean translationLocked;

	public static final String ARG_MODEL_ID = "model_id";

	public void setModelParser(final ModelParser modelParser) {
		parser = modelParser;
		translationLocked = false;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Bundle arguments = getArguments();
		final int modelID = arguments.getInt(ARG_MODEL_ID);
		if (0 == modelID) {
			throw new RuntimeException("ModelViewerFragment passed invalid model id.");
		}

		if (null == parser) {
			throw new RuntimeException("No valid model parser set.");
		}

		if (isTransparentSurfaceView())
			setGLBackgroundTransparent(true);

		renderer = createRenderer(modelID, parser);
		renderer.setSurfaceView(mSurfaceView);
		renderer.registerObjectSelectedListener(objectSelectedListener);
		setRenderer(renderer);

		gestureListener = new ModelControlListener(renderer);
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

		scaleGestureDetector.onTouchEvent(event);
		rotationGestureDetector.onTouchEvent(event);
		panGestureDetector.onTouchEvent(event);

		if (!translationLocked) {
			gestureDetector.onTouchEvent(event);
		}

		return true;
	}

	public void setTranslationLocked(final boolean lock) {
		translationLocked = lock;
	}

	public boolean isTranslationLocked() {
		return translationLocked;
	}

	public void setAutoRotation(final boolean autoRotate) {
		renderer.setAutoRotation(autoRotate);
	}

	public boolean isAutoRotating() {
		return renderer.isAutoRotating();
	}

	public int getAmbientColor(final String objectName) {
		return renderer.getAmbientColor(objectName);
	}

	public boolean setAmbientColor(final String objectName, final int color) {
		return renderer.setAmbientColor(objectName, color);
	}

	public void registerObjectSelectedListener(final ObjectSelectedListener listener) {
		objectSelectedListener = listener;
		if (renderer != null) {
			renderer.registerObjectSelectedListener(listener);
		}
	}

	protected abstract ModelViewerRenderer createRenderer(final int modelID, final ModelParser parser);

	protected abstract class ModelViewerRenderer extends RajawaliRenderer implements ModelController {
		public ModelViewerRenderer(final Context context, final ModelParser parser) {
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
	}
}
