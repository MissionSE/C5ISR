package com.missionse.commandablemodel;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;

public interface WifiDirectConnectionListener {
	void onConnectionSuccessful(final WifiP2pInfo p2pInfo, final WifiP2pDevice p2pDevice);
}
