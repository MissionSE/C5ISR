package com.missionse.commandablemodel.network;

/**
 * Provides callbacks for model state change.
 */
public interface ModelChangeRecipient {

	/**
	 * Called when the model has changed.
	 */
	void onModelChange();
}
