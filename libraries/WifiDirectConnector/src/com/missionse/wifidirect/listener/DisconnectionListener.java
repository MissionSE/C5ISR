package com.missionse.wifidirect.listener;

public interface DisconnectionListener {
	
	/**
	 * Called when disconnect() is successful.
	 */
	public void onDisconnectionSuccess();
	
	/**
	 * Called when disconnect() has failed.
	 */
	public void onDisconnectionFailure();
}
