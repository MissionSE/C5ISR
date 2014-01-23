package com.missionse.commandablemodel.network;

import com.missionse.commandablemodel.CommandableModelActivity;
import com.missionse.modelviewer.ModelViewerFragment;

/**
 * Acts as a recipient of model state changes, and notifies the mActivity to send the state out.
 */
public class ModelControllerClient implements ModelChangeRecipient {

	private ModelViewerFragment mModelFragment;

	// We should only send ModelState changes at most every 20ms.
	// Note: In the future, this logic should be replaced by some sort of timed task that automatically sends
	// the current state at a fixed rate, to avoid jitter. For now, rate limiting works fine.
	private static final long PERIODIC = 17L;
	private long mLastStateSentTime = 0L;

	private CommandableModelActivity mActivity;

	/**
	 * Creates a new ModelControllerClient.
	 * @param commandableModelActivity the parent activity to call back on change
	 */
	public ModelControllerClient(final CommandableModelActivity commandableModelActivity) {
		mActivity = commandableModelActivity;
	}

	/**
	 * Sets the ModelViewerFragment from which we are retrieving the model state (via controller).
	 * @param fragment the fragment to get data from
	 */
	public void setModelViewerFragment(final ModelViewerFragment fragment) {
		mModelFragment = fragment;
	}

	@Override
	public void onModelChange() {
		if (System.currentTimeMillis() - mLastStateSentTime > PERIODIC) {
			ModelState currentModelState = new ModelState(mModelFragment.getController());
			mActivity.sendModelState(currentModelState.toString());
			mLastStateSentTime = System.currentTimeMillis();
		}
	}
}
