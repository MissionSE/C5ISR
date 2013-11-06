package com.missionse.modelviewer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import rajawali.renderer.RajawaliRenderer;
import android.content.Context;

public abstract class ModelViewerRenderer extends RajawaliRenderer {
	protected ModelViewerFragment fragment;

	public ModelViewerRenderer(final Context context, final ModelViewerFragment modelViewerFragment) {
		super (context);
		setFrameRate(60);
		fragment = modelViewerFragment;
	}

	@Override
	public void onSurfaceCreated(final GL10 gl, final EGLConfig config) {
		fragment.showLoader();
		super.onSurfaceCreated(gl, config);
	}

	@Override
	public void onDrawFrame(final GL10 glUnused) {
		super.onDrawFrame(glUnused);
		fragment.hideLoader();
	}

	public abstract ModelController getController();
	public abstract ModelAnimationController getAnimator();
}
