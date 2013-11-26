package com.missionse.commandablemodel.network;

import com.missionse.commandablemodel.CommandableModelActivity;
import com.missionse.modelviewer.ModelViewerFragment;

public class ModelControllerClient implements ModelChangeRecipient {

	private ModelViewerFragment modelFragment;

	// We should only send ModelState changes at most every 20ms.
	// Note: In the future, this logic should be replaced by some sort of timed task that automatically sends
	// the current state at a fixed rate, to avoid jitter. For now, rate limiting works fine.
	private static final long PERIODIC = 17L;
	private long lastStateSentTime = 0L;

	private CommandableModelActivity activity;

	public ModelControllerClient(final CommandableModelActivity commandableModelActivity) {
		activity = commandableModelActivity;
	}

	public void setModelViewerFragment(final ModelViewerFragment fragment) {
		modelFragment = fragment;
	}

	@Override
	public void onModelChange() {
		if (System.currentTimeMillis() - lastStateSentTime > PERIODIC) {
			ModelState currentModelState = new ModelState(modelFragment.getController());
			activity.sendModelState(currentModelState.toString());
			lastStateSentTime = System.currentTimeMillis();
		}
	}
}
