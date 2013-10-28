package com.missionse.modelviewer;

import android.os.Bundle;

import com.missionse.modelviewer.impl.ModelSceneController;
import com.missionse.modelviewer.impl.ObjModelParser;

public final class ModelViewerFragmentFactory
{
	private ModelViewerFragmentFactory() {
	}

	public static ModelViewerFragment createObjModelFragment(final int modelID) {
		ModelViewerFragment fragment = new ModelSceneController();
		fragment.setModelParser(new ObjModelParser());

		Bundle arguments = new Bundle();
		arguments.putInt(ModelViewerFragment.ARG_MODEL_ID, modelID);
		fragment.setArguments(arguments);

		return fragment;
	}
}
