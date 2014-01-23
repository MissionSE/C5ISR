package com.missionse.wifidirect.listener;

/**
 * Provides callbacks for disconnection initiation results.
 */
public interface DisconnectionListener {

	/**
	 * Called when disconnect() is successful.
	 */
	void onDisconnectionSuccess();

	/**
	 * Called when disconnect() has failed.
	 */
	void onDisconnectionFailure();
}
