package com.missionse.wifidirect;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;

public class WifiDirectConnector {
	
	private IntentFilter intentFilter;
	
	private WifiP2pManager wifiManager;
	private Channel wifiChannel;
	private WifiDirectBroadcastReceiver broadcastReceiver;
	
	public WifiDirectConnector() {
		setupIntentFilter();
	}
	
	private void setupIntentFilter() {
		intentFilter = new IntentFilter();
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
	}
	
	public void onCreate(final Context context) {
		wifiManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
		wifiChannel = wifiManager.initialize(context, context.getMainLooper(), null);
	}

	public void onResume(final Activity activity) {
		broadcastReceiver = new WifiDirectBroadcastReceiver(wifiManager, wifiChannel);
		activity.registerReceiver(broadcastReceiver, intentFilter);
	}
	
	public void onPause(final Activity activity) {
		activity.unregisterReceiver(broadcastReceiver);
	}
	
	public void discoverPeers(final DiscoverPeersListener listener) {
		if (!broadcastReceiver.isP2PEnabled()) {
			listener.onP2pNotEnabled();
		}
		else {
			wifiManager.discoverPeers(wifiChannel, new WifiP2pManager.ActionListener() {
				@Override
				public void onSuccess() {
					listener.onDiscoverPeersSuccess();
				}

				@Override
				public void onFailure(int reasonCode) {
					listener.onDiscoverPeersFailure(reasonCode);
				}
			});
		}
	}
	
	public void connect(final WifiP2pConfig config, final ConnectionInitiationListener listener) {
		wifiManager.connect(wifiChannel, config, new ActionListener() {
			@Override
			public void onSuccess() {
				listener.onConnectionInitiationSuccess();
			}
			
			@Override
			public void onFailure(int arg0) {
				listener.onConnectionInitiationFailure();
			}
		});
	}
	
	public void disconnect(final DisconnectionListener listener) {
		wifiManager.removeGroup(wifiChannel, new ActionListener() {
			@Override
            public void onSuccess() {
                listener.onDisconnectionSuccess();
            }
			
            @Override
            public void onFailure(int reasonCode) {
            	listener.onDisconnectionFailure();
            }
        });
	}
	
	public void addStateChangeHandler(final P2pStateChangeHandler handler) {
		if (broadcastReceiver != null) {
			broadcastReceiver.addStateChangeHandler(handler);
		}
	}
}
