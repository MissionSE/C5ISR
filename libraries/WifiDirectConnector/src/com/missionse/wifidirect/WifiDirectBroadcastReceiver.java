package com.missionse.wifidirect;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;

import com.missionse.wifidirect.listener.P2pStateChangeListener;

/**
 * Acts as a receiver of all intents, filtering them for relevant calls and notifying handlers of changes.
 */
public class WifiDirectBroadcastReceiver extends BroadcastReceiver {

	private WifiP2pManager mWifiP2pManager;
	private Channel mWifiChannel;

	private List<P2pStateChangeListener> mP2pStateChangeHandlers;

	private boolean mP2pIsEnabled = false;

	/**
	 * Creates a new WifiDirectBroadcastReceiver to handle incoming intents.
	 * @param p2pManager the WifiP2pManager via the Android base OS
	 * @param channel the channel over which all WifiP2p connections and discovery are being done
	 */
	public WifiDirectBroadcastReceiver(final WifiP2pManager p2pManager, final Channel channel) {
		super();
		mWifiP2pManager = p2pManager;
		mWifiChannel = channel;

		mP2pStateChangeHandlers = new ArrayList<P2pStateChangeListener>();
	}

	/**
	 * Returns the state of P2P for the system.
	 * @return whether or not P2P is enabled
	 */
	public boolean isP2PEnabled() {
		return mP2pIsEnabled;
	}

	/**
	 * Adds a handler for state changes.
	 * @param handler the handlers to be notified of state changes
	 */
	public void addStateChangeHandler(final P2pStateChangeListener handler) {
		mP2pStateChangeHandlers.add(handler);
	}

	@Override
	public void onReceive(final Context context, final Intent intent) {
		String action = intent.getAction();
		if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
			int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
			if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
				mP2pIsEnabled = true;
			} else if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
				mP2pIsEnabled = false;
			}
		} else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
			if (mWifiP2pManager != null) {
				mWifiP2pManager.requestPeers(mWifiChannel, new PeerListListener() {
					@Override
					public void onPeersAvailable(final WifiP2pDeviceList peers) {
						for (P2pStateChangeListener handler : mP2pStateChangeHandlers) {
							handler.onPeersAvailable(peers);
						}
					}
				});
			}
		} else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
			if (mWifiP2pManager != null) {
				NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
				if (networkInfo.isConnected()) {
					mWifiP2pManager.requestConnectionInfo(mWifiChannel, new ConnectionInfoListener() {
						@Override
						public void onConnectionInfoAvailable(final WifiP2pInfo connectionInfo) {
							for (P2pStateChangeListener handler : mP2pStateChangeHandlers) {
								handler.onConnectionInfoAvailable(connectionInfo);
							}
						}
					});
				}
			}
		} else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
			WifiP2pDevice thisDevice = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
			for (P2pStateChangeListener handler : mP2pStateChangeHandlers) {
				handler.onDeviceChanged(thisDevice);
			}
		}
	}
}
