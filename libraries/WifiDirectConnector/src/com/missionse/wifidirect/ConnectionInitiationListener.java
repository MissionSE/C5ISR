package com.missionse.wifidirect;

public interface ConnectionInitiationListener {
	
	/**
	 * Called when connect() ha successfully begun initiation.
	 */
	public void onConnectionInitiationSuccess();
	
	/**
	 * Called when connect() has failed.
	 */
	public void onConnectionInitiationFailure();
}
