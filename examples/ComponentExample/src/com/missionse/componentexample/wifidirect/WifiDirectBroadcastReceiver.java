package com.missionse.componentexample.wifidirect;

import com.missionse.componentexample.R;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.util.Log;

public class WifiDirectBroadcastReceiver extends BroadcastReceiver {
	
	private WifiP2pManager wifiP2pManager;
	private Channel wifiChannel;
	private FragmentManager fragmentManager;
	
	private boolean p2pIsEnabled;
	
	public WifiDirectBroadcastReceiver(final WifiP2pManager p2pManager, final Channel channel,
			final FragmentManager manager) {
		super();
		wifiP2pManager = p2pManager;
		wifiChannel = channel;
		fragmentManager = manager;
		
		p2pIsEnabled = false;
	}
	
	public boolean isP2PEnabled() {
		return p2pIsEnabled;
	}
	
	@Override
	public void onReceive(final Context context, final Intent intent) {
		String action = intent.getAction();
		Log.e("BroadcastReceiver", "received an intent");
		if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
			Log.e("BroadcastReceiver", "received STATE_CHANGED");
			int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
			if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
				p2pIsEnabled = true;
			}
			else if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
				p2pIsEnabled = false;
			}
		}
		else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
			Log.e("BroadcastReceiver", "received PEERS");
			if (wifiP2pManager != null) {
				wifiP2pManager.requestPeers(wifiChannel, (PeerListListener) fragmentManager
						.findFragmentById(R.id.frag_list));
			}
		}
		else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
			Log.e("BroadcastReceiver", "received CONNECTION_CHANGED");
			if (wifiP2pManager != null) {
				NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
				if (networkInfo.isConnectedOrConnecting()) {
					Log.e("BroadcastReceiver", "connecting or connected");
				}
				Log.e("BroadcastReceiver", "got network info");
				if (networkInfo.isConnected()) {
					Log.e("BroadcastReceiver", "is connected");
					
					DeviceDetailFragment fragment = (DeviceDetailFragment) fragmentManager
							.findFragmentById(R.id.frag_detail);
					wifiP2pManager.requestConnectionInfo(wifiChannel, fragment);
				}
			}
		}
		else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
			Log.e("BroadcastReceiver", "received THIS_DEVICE");
			DeviceListFragment fragment = (DeviceListFragment) fragmentManager
                    .findFragmentById(R.id.frag_list);
            fragment.updateThisDevice((WifiP2pDevice) intent.getParcelableExtra(
                    WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));
		}
	}
}
