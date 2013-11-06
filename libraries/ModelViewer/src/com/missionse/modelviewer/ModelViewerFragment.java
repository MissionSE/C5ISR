package com.missionse.modelviewer;

import java.util.ArrayList;

import rajawali.Object3D;
import rajawali.RajawaliFragment;
import rajawali.util.OnObjectPickedListener;
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

public abstract class ModelViewerFragment extends RajawaliFragment implements OnTouchListener, OnObjectPickedListener, ObjectLoadedListener {
	private ModelParser modelParser;
	private ModelViewerRenderer renderer;
	private ProgressBar progressBar;

	private ModelViewerGestureListener gestureListener;
	private GestureDetector gestureDetector;
	private ScaleGestureDetector scaleGestureDetector;
	private RotationGestureDetector rotationGestureDetector;
	private PanGestureDetector panGestureDetector;
	private ArrayList<ObjectPickedListener> objectPickedListeners;
	private ArrayList<ObjectLoadedListener> objectLoadedListeners;

	private boolean transparentSurfaceView;

	public static final String ARG_MODEL_ID = "model_id";

	public void setModelParser(final ModelParser parser) {
		modelParser = parser;
	}

	public void setGestureListener(final ModelViewerGestureListener modelViewerGestureListener) {
		gestureListener = modelViewerGestureListener;
	}

	public ModelViewerFragment() {
		objectPickedListeners = new ArrayList<ObjectPickedListener>();
		objectLoadedListeners = new ArrayList<ObjectLoadedListener>();
		transparentSurfaceView = false;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Bundle arguments = getArguments();
		final int modelID = arguments.getInt(ARG_MODEL_ID);
		if (0 == modelID) {
			throw new RuntimeException("ModelViewerFragment passed invalid model id.");
		}

		if (null == modelParser) {
			throw new RuntimeException("No valid model parser set.");
		}

		if (isTransparentSurfaceView()) {
			setGLBackgroundTransparent(true);
		}

		renderer = createRenderer(modelID, modelParser);
		renderer.setSurfaceView(mSurfaceView);
		setRenderer(renderer);

		if (gestureListener != null) {
			gestureListener.setController(getController());
			gestureDetector = new GestureDetector(getActivity(), gestureListener);
			scaleGestureDetector = new ScaleGestureDetector(getActivity(), gestureListener);
			rotationGestureDetector = new RotationGestureDetector(gestureListener);
			panGestureDetector = new PanGestureDetector(gestureListener);

			mSurfaceView.setOnTouchListener(this);
		}
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
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

	protected void showLoader() {
		if (!progressBar.isShown()) {
			progressBar.post(new Runnable() {
				@Override
				public void run() {
					progressBar.setVisibility(View.VISIBLE);
				}
			});
		}
	}

	protected void hideLoader() {
		if (progressBar.isShown()) {
			progressBar.post(new Runnable() {
				@Override
				public void run() {
					progressBar.setVisibility(View.GONE);
				}
			});
		}
	}

	public void setTransparentSurfaceView(final boolean transparent) {
		transparentSurfaceView = transparent;
	}

	protected boolean isTransparentSurfaceView() {
		return transparentSurfaceView;
	}

	@Override
	public boolean onTouch(final View v, final MotionEvent event) {
		boolean touchConsumed = false;
		if (gestureListener != null) {
			scaleGestureDetector.onTouchEvent(event);
			rotationGestureDetector.onTouchEvent(event);
			panGestureDetector.onTouchEvent(event);
			gestureDetector.onTouchEvent(event);
			touchConsumed = true;
		}

		return touchConsumed;
	}

	@Override
	public void onObjectPicked(final Object3D object) {
		if (object != null && object.getName() != null) {
			for (ObjectPickedListener listener : objectPickedListeners) {
				listener.objectPicked(object.getName());
			}
		}
	}

	@Override
	public void onObjectLoaded() {
		for (ObjectLoadedListener listener : objectLoadedListeners) {
			listener.onObjectLoaded();
		}
	}

	public void registerObjectPickedListener(final ObjectPickedListener listener) {
		objectPickedListeners.add(listener);
	}

	public void registerObjectLoadedListener(final ObjectLoadedListener listener) {
		objectLoadedListeners.add(listener);
	}

	public ModelController getController() {
		return renderer.getController();
	}

	public ModelAnimationController getAnimator() {
		return renderer.getAnimator();
	}

	protected abstract ModelViewerRenderer createRenderer(final int modelID, final ModelParser parser);
}
