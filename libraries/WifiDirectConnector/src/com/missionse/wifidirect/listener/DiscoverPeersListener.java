package com.missionse.wifidirect.listener;

/**
 * Provides callbacks for discovery process events.
 */
public interface DiscoverPeersListener {

	/**
	 * Called when P2P is discovered to not be enabled.
	 */
	void onP2pNotEnabled();

	/**
	 * Called when discoverPeers() is successful.
	 */
	void onDiscoverPeersSuccess();

	/**
	 * Called when discoverPeers() has failed.
	 * @param reasonCode code for failure reason
	 */
	void onDiscoverPeersFailure(final int reasonCode);
}
