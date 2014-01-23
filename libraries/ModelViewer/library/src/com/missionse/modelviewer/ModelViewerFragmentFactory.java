package com.missionse.modelviewer;

import android.os.Bundle;

import com.missionse.modelviewer.impl.ModelFragment;
import com.missionse.modelviewer.impl.ModelGestureListener;
import com.missionse.modelviewer.impl.ObjModelParser;

/**
 * Provides a factory used to create Model Viewer Fragments.
 */
public final class ModelViewerFragmentFactory {
	private ModelViewerFragmentFactory() {
	}

	/**
	 * Creates a model viewer fragment with the resource id of the model.
	 * @param modelId The resource id of the model.
	 * @return A fragment that displays the selected model.
	 */
	public static ModelViewerFragment createObjModelFragment(final int modelId) {
		return createObjModelFragment(modelId, new ModelGestureListener());
	}

	/**
	 * Creates a model viewer fragment with a resource id of the model and a gesture listener.
	 * @param modelId The resource id of the model.
	 * @param gestureListener A gesture listener that will handle gesture callbacks on the fragment.
	 * @return A fragment that displays the selected model.
	 */
	public static ModelViewerFragment createObjModelFragment(final int modelId, final ModelViewerGestureListener gestureListener) {
		ModelViewerFragment fragment = new ModelFragment();
		fragment.setModelParser(new ObjModelParser());
		fragment.setGestureListener(gestureListener);

		Bundle arguments = new Bundle();
		arguments.putInt(ModelViewerFragment.ARG_MODEL_ID, modelId);
		fragment.setArguments(arguments);

		return fragment;
	}
}
