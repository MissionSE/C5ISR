package com.missionse.wifidirect.listener;

public abstract class DiscoverPeersListener {
	
	/**
	 * Called when P2P is discovered to not be enabled.
	 */
	public abstract void onP2pNotEnabled();
	
	/**
	 * Called when discoverPeers() is successful.
	 */
	public abstract void onDiscoverPeersSuccess();
	
	/**
	 * Called when discoverPeers() has failed.
	 * @param reasonCode code for failure reason
	 */
	public abstract void onDiscoverPeersFailure(final int reasonCode);
}
