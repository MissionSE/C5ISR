package com.missionse.modelviewer;

import com.missionse.modelviewer.impl.ObjModelFragment;

import android.os.Bundle;

public final class ModelViewerFragmentFactory
{
	private ModelViewerFragmentFactory() {
	}

	public static ModelViewerFragment createObjModelFragment(final int modelID) {
		ModelViewerFragment fragment = new ObjModelFragment();

		Bundle arguments = new Bundle();
		arguments.putInt(ModelViewerFragment.ARG_MODEL_ID, modelID);
		fragment.setArguments(arguments);

		return fragment;
	}
}
