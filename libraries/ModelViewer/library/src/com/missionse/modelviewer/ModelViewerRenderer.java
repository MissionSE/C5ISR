package com.missionse.modelviewer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import rajawali.renderer.RajawaliRenderer;
import android.content.Context;

/**
 * Provides a renderer specific to a model viewer.
 */
public abstract class ModelViewerRenderer extends RajawaliRenderer {
	private static final int FRAME_RATE = 60;

	private ModelViewerFragment mFragment;

	/**
	 * Default constructor.
	 * @param context The context of the activity that owns the renderer.
	 * @param modelViewerFragment The fragment that is using the renderer.
	 */
	public ModelViewerRenderer(final Context context, final ModelViewerFragment modelViewerFragment) {
		super(context);
		setFrameRate(FRAME_RATE);
		mFragment = modelViewerFragment;
	}

	@Override
	public void onSurfaceCreated(final GL10 gl, final EGLConfig config) {
		mFragment.showLoader();
		super.onSurfaceCreated(gl, config);
	}

	@Override
	public void onDrawFrame(final GL10 glUnused) {
		super.onDrawFrame(glUnused);
		mFragment.hideLoader();
	}

	protected ModelViewerFragment getFragment() {
		return mFragment;
	}

	/**
	 * Returns the ModelController used to control the renderer.
	 * @return A controller that can be used to control the objects managed.
	 */
	public abstract ModelController getController();

	/**
	 * Returns the ModelAnimationController used to control the animations in the renderer.
	 * @return An animation controller that can be used to control the animations on the objects managed.
	 */
	public abstract ModelAnimationController getAnimator();
}
