package com.missionse.modelviewer.impl;

import com.missionse.modelviewer.ModelParser;
import com.missionse.modelviewer.ModelViewerFragment;
import com.missionse.modelviewer.ModelViewerRenderer;

/**
 * Provides a fragment that displays a 3d model viewer.
 */
public class ModelFragment extends ModelViewerFragment {
	@Override
	protected ModelViewerRenderer createRenderer(final int modelID, final ModelParser parser) {
		return new ModelRenderer(getActivity(), this, modelID, parser);
	}
}
