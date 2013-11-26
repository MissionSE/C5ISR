package com.missionse.wifidirect.listener;

/**
 * Provides callbacks for connection initiation results.
 */
public interface ConnectionInitiationListener {

	/**
	 * Called when connect() ha successfully begun initiation.
	 */
	void onConnectionInitiationSuccess();

	/**
	 * Called when connect() has failed.
	 */
	void onConnectionInitiationFailure();
}
