package com.missionse.wifidirect.listener;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;

/**
 * Provides callback methods for Wifi P2P state changes.
 */
public interface P2pStateChangeListener {

	/**
	 * Called on receipt of WIFI_P2P_PEERS_CHANGED_ACTION.
	 * @param peers list of available peers
	 */
	void onPeersAvailable(final WifiP2pDeviceList peers);

	/**
	 * Called on receipt of WIFI_P2P_CONNECTION_CHANGED_ACTION.
	 * @param connectionInfo connection info as per result of connect()
	 */
	void onConnectionInfoAvailable(final WifiP2pInfo connectionInfo);

	/**
	 * Called on receipt of WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.
	 * @param thisDevice device information about this device
	 */
	void onDeviceChanged(final WifiP2pDevice thisDevice);
}