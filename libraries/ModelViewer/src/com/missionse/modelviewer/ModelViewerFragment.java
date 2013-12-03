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

/**
 * Provides a fragment that displays a 3d model viewer.
 */
public abstract class ModelViewerFragment extends RajawaliFragment implements
		OnTouchListener, OnObjectPickedListener, ObjectLoadedListener {

	private ModelParser mModelParser;
	private ModelViewerRenderer mRenderer;
	private ProgressBar mProgressBar;

	private ModelViewerGestureListener mGestureListener;
	private GestureDetector mGestureDetector;
	private ScaleGestureDetector mScaleGestureDetector;
	private RotationGestureDetector mRotationGestureDetector;
	private PanGestureDetector mPanGestureDetector;
	private ArrayList<ObjectPickedListener> mObjectPickedListeners;
	private ArrayList<ObjectLoadedListener> mObjectLoadedListeners;

	private boolean mTransparentSurfaceView;

	public static final String ARG_MODEL_ID = "model_id";

	/**
	 * Sets the model parser used in the fragment.
	 * @param modelParser The parser used to parse the model file.
	 */
	public void setModelParser(final ModelParser modelParser) {
		mModelParser = modelParser;
	}

	/**
	 * Sets the gesture listener used in the fragment.
	 * @param gestureListener The gesture listener used to handle all gesture processing.
	 */
	public void setGestureListener(final ModelViewerGestureListener gestureListener) {
		mGestureListener = gestureListener;
	}

	/**
	 * Default constructor.
	 */
	public ModelViewerFragment() {
		mObjectPickedListeners = new ArrayList<ObjectPickedListener>();
		mObjectLoadedListeners = new ArrayList<ObjectLoadedListener>();
		mTransparentSurfaceView = false;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

		final Bundle arguments = getArguments();
		final int modelId = arguments.getInt(ARG_MODEL_ID);
		if (0 == modelId) {
			throw new RuntimeException("ModelViewerFragment passed invalid model id.");
		}

		if (null == mModelParser) {
			throw new RuntimeException("No valid model parser set.");
		}

		if (isTransparentSurfaceView()) {
			setGLBackgroundTransparent(true);
		}

		mRenderer = createRenderer(modelId, mModelParser);
		mRenderer.setSurfaceView(mSurfaceView);
		setRenderer(mRenderer);

		if (mGestureListener != null) {
			mGestureListener.setController(getController());
			mGestureDetector = new GestureDetector(getActivity(), mGestureListener);
			mScaleGestureDetector = new ScaleGestureDetector(getActivity(), mGestureListener);
			mRotationGestureDetector = new RotationGestureDetector(mGestureListener);
			mPanGestureDetector = new PanGestureDetector(mGestureListener);

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
		mProgressBar = (ProgressBar) mLayout.findViewById(R.id.progress_bar);

		return mLayout;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mRenderer.onSurfaceDestroyed();
	}

	protected void showLoader() {
		if (!mProgressBar.isShown()) {
			mProgressBar.post(new Runnable() {
				@Override
				public void run() {
					mProgressBar.setVisibility(View.VISIBLE);
				}
			});
		}
	}

	protected void hideLoader() {
		if (mProgressBar.isShown()) {
			mProgressBar.post(new Runnable() {
				@Override
				public void run() {
					mProgressBar.setVisibility(View.GONE);
				}
			});
		}
	}

	/**
	 * Sets whether the background of the model viewer is transparent.
	 * Must be done before onCreate is called.
	 * @param transparent Whether the surface view is transparent.
	 */
	public void setTransparentSurfaceView(final boolean transparent) {
		mTransparentSurfaceView = transparent;
	}

	protected boolean isTransparentSurfaceView() {
		return mTransparentSurfaceView;
	}

	@Override
	public boolean onTouch(final View v, final MotionEvent event) {
		boolean touchConsumed = false;
		if (mGestureListener != null) {
			mScaleGestureDetector.onTouchEvent(event);
			mRotationGestureDetector.onTouchEvent(event);
			mPanGestureDetector.onTouchEvent(event);
			mGestureDetector.onTouchEvent(event);
			touchConsumed = true;
		}

		return touchConsumed;
	}

	@Override
	public void onObjectPicked(final Object3D object) {
		if (object != null && object.getName() != null) {
			for (ObjectPickedListener listener : mObjectPickedListeners) {
				listener.objectPicked(object.getName());
			}
		}
	}

	@Override
	public void onObjectLoaded() {
		for (ObjectLoadedListener listener : mObjectLoadedListeners) {
			listener.onObjectLoaded();
		}
	}

	/**
	 * Registers an object picked listener to be notified when an object is picked.
	 * @param listener The object picked listener to be registered.
	 */
	public void registerObjectPickedListener(final ObjectPickedListener listener) {
		mObjectPickedListeners.add(listener);
	}

	/**
	 * Registers an object loaded listener to be notified when the object group is loaded.
	 * @param listener The object loaded listener to be registered.
	 */
	public void registerObjectLoadedListener(final ObjectLoadedListener listener) {
		mObjectLoadedListeners.add(listener);
	}

	/**
	 * Gets the controller used to control the model.
	 * @return The model controller.
	 */
	public ModelController getController() {
		return mRenderer.getController();
	}

	/**
	 * Gets the animation controller used to control the animations of the model.
	 * @return The model animation controller.
	 */
	public ModelAnimationController getAnimator() {
		return mRenderer.getAnimator();
	}

	protected abstract ModelViewerRenderer createRenderer(final int modelID, final ModelParser parser);
}
