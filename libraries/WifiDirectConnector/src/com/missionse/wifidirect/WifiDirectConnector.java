package com.missionse.wifidirect;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.util.SparseArray;

import com.missionse.wifidirect.listener.ConnectionInitiationListener;
import com.missionse.wifidirect.listener.DisconnectionListener;
import com.missionse.wifidirect.listener.DiscoverPeersListener;
import com.missionse.wifidirect.listener.IncomingDataListener;
import com.missionse.wifidirect.listener.P2pStateChangeListener;
import com.missionse.wifidirect.network.Client;
import com.missionse.wifidirect.network.Server;

public class WifiDirectConnector {

	private Activity parentActivity;

	private IntentFilter intentFilter;

	private WifiP2pManager wifiManager;
	private Channel wifiChannel;
	private WifiDirectBroadcastReceiver broadcastReceiver;

	private Server server;
	private final List<IncomingDataListener> dataListeners = new ArrayList<IncomingDataListener>();

	private Client client;

	private WifiP2pDevice targetDevice;

	public final static SparseArray<String> deviceStatuses = new SparseArray<String>();

	public WifiDirectConnector(final Activity activity) {
		parentActivity = activity;
		setupIntentFilter();

		deviceStatuses.append(WifiP2pDevice.AVAILABLE, "Available");
		deviceStatuses.append(WifiP2pDevice.INVITED, "Invited");
		deviceStatuses.append(WifiP2pDevice.CONNECTED, "Connected");
		deviceStatuses.append(WifiP2pDevice.FAILED, "Failed");
		deviceStatuses.append(WifiP2pDevice.UNAVAILABLE, "Unavailable");
	}

	private void setupIntentFilter() {
		intentFilter = new IntentFilter();
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
	}

	public void onCreate() {
		wifiManager = (WifiP2pManager) parentActivity.getSystemService(Context.WIFI_P2P_SERVICE);
		wifiChannel = wifiManager.initialize(parentActivity, parentActivity.getMainLooper(), null);

		client = new Client(parentActivity);
	}

	public void onResume() {
		broadcastReceiver = new WifiDirectBroadcastReceiver(wifiManager, wifiChannel);
		parentActivity.registerReceiver(broadcastReceiver, intentFilter);
	}

	public void onPause() {
		parentActivity.unregisterReceiver(broadcastReceiver);
	}

	public void discoverPeers(final DiscoverPeersListener listener) {
		if (!broadcastReceiver.isP2PEnabled()) {
			listener.onP2pNotEnabled();
		} else {
			wifiManager.discoverPeers(wifiChannel, new WifiP2pManager.ActionListener() {
				@Override
				public void onSuccess() {
					listener.onDiscoverPeersSuccess();
				}

				@Override
				public void onFailure(final int reasonCode) {
					listener.onDiscoverPeersFailure(reasonCode);
				}
			});
		}
	}

	public void connect(final WifiP2pDevice device, final ConnectionInitiationListener listener) {
		targetDevice = device;

		// Called by the PeerDetail fragment when the Connect button is pressed.
		WifiP2pConfig config = new WifiP2pConfig();
		config.deviceAddress = device.deviceAddress;
		config.wps.setup = WpsInfo.PBC;
		config.groupOwnerIntent = 0;

		wifiManager.connect(wifiChannel, config, new ActionListener() {
			@Override
			public void onSuccess() {
				listener.onConnectionInitiationSuccess();
			}

			@Override
			public void onFailure(final int arg0) {
				listener.onConnectionInitiationFailure();
			}
		});
	}

	public void disconnect(final DisconnectionListener listener) {
		wifiManager.removeGroup(wifiChannel, new ActionListener() {
			@Override
			public void onSuccess() {
				// On disconnect, stop the client from sending, and shut down the server thread.
				client.onDisconnect();

				server.cancel(true);
				server = null;

				listener.onDisconnectionSuccess();
			}

			@Override
			public void onFailure(final int reasonCode) {
				listener.onDisconnectionFailure();
			}
		});
	}

	public void registerStateChangeListener(final P2pStateChangeListener listener) {
		if (broadcastReceiver != null) {
			broadcastReceiver.addStateChangeHandler(new P2pStateChangeListener() {

				@Override
				public void onPeersAvailable(final WifiP2pDeviceList peers) {
					listener.onPeersAvailable(peers);
				}

				@SuppressWarnings("unchecked")
				@Override
				public void onConnectionInfoAvailable(final WifiP2pInfo connectionInfo) {
					server = new Server();
					server.execute(dataListeners);

					client.setConnectionSuccessful(connectionInfo, targetDevice);

					listener.onConnectionInfoAvailable(connectionInfo);
				}

				@Override
				public void onDeviceChanged(final WifiP2pDevice thisDevice) {
					listener.onDeviceChanged(thisDevice);
				}
			});
		}

	}

	public void registerDataListener(final IncomingDataListener listener) {
		dataListeners.add(listener);
	}

	public Client getClient() {
		return client;
	}
}
