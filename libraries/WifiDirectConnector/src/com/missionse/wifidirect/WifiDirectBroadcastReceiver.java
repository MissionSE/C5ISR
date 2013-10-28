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

public class WifiDirectBroadcastReceiver extends BroadcastReceiver {
	
	private WifiP2pManager wifiP2pManager;
	private Channel wifiChannel;

	private List<P2pStateChangeHandler> p2pStateChangeHandlers;
	
	private boolean p2pIsEnabled;
	
	public WifiDirectBroadcastReceiver(final WifiP2pManager p2pManager, final Channel channel) {
		super();
		wifiP2pManager = p2pManager;
		wifiChannel = channel;		
		p2pIsEnabled = false;
		
		p2pStateChangeHandlers = new ArrayList<P2pStateChangeHandler>();
	}
	
	public boolean isP2PEnabled() {
		return p2pIsEnabled;
	}
	
	public void addStateChangeHandler(final P2pStateChangeHandler handler) {
		p2pStateChangeHandlers.add(handler);
	}
	
	@Override
	public void onReceive(final Context context, final Intent intent) {
		String action = intent.getAction();
		if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
			int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
			if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
				p2pIsEnabled = true;
			}
			else if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
				p2pIsEnabled = false;
			}
		}
		else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
			if (wifiP2pManager != null) {
				wifiP2pManager.requestPeers(wifiChannel, new PeerListListener() {
					@Override
					public void onPeersAvailable(WifiP2pDeviceList peers) {
						for(P2pStateChangeHandler handler : p2pStateChangeHandlers) {
							handler.onPeersAvailable(peers);
						}
					}
				});
			}
		}
		else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
			if (wifiP2pManager != null) {
				NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
				if (networkInfo.isConnected()) {					
					wifiP2pManager.requestConnectionInfo(wifiChannel, new ConnectionInfoListener() {
						@Override
						public void onConnectionInfoAvailable(WifiP2pInfo connectionInfo) {
							for(P2pStateChangeHandler handler : p2pStateChangeHandlers) {
								handler.onConnectionInfoAvailable(connectionInfo);
							}
						}
					});
				}
			}
		}
		else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
			WifiP2pDevice thisDevice = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
			for(P2pStateChangeHandler handler : p2pStateChangeHandlers) {
				handler.onDeviceChanged(thisDevice);
			}
		}
	}
}
