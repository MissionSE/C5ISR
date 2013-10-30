package com.missionse.modelviewer.impl;

import com.missionse.modelviewer.ModelParser;
import com.missionse.modelviewer.ModelViewerFragment;
import com.missionse.modelviewer.ModelViewerRenderer;

public class ModelFragment extends ModelViewerFragment {

	@Override
	protected ModelViewerRenderer createRenderer(final int modelID, final ModelParser parser) {
		return new ModelRenderer(getActivity(), this, modelID, parser);
	}
}
