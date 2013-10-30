package com.missionse.modelviewer.impl;

import com.missionse.modelviewer.ModelParser;
import com.missionse.modelviewer.ModelViewerFragment;
import com.missionse.modelviewer.ModelViewerRenderer;

public class ModelSceneFragment extends ModelViewerFragment {

	@Override
	protected ModelViewerRenderer createRenderer(final int modelID, final ModelParser parser) {
		return new ModelSceneRenderer(getActivity(), this, modelID, parser);
	}
}
