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

import com.missionse.wifidirect.listener.ConnectionInitiationListener;
import com.missionse.wifidirect.listener.DisconnectionListener;
import com.missionse.wifidirect.listener.DiscoverPeersListener;
import com.missionse.wifidirect.listener.IncomingDataListener;
import com.missionse.wifidirect.listener.P2pStateChangeListener;
import com.missionse.wifidirect.network.Client;
import com.missionse.wifidirect.network.Server;

/**
 * Provides an easy-to-use API against the WifiManager and WifiP2p connections provided by Android.
 */
public class WifiDirectConnector {

	private Activity mParentActivity;

	private WifiP2pManager mWifiManager;
	private Channel mWifiChannel;
	private WifiDirectBroadcastReceiver mBroadcastReceiver;

	private Server mServer;
	private final List<IncomingDataListener> mDataListeners = new ArrayList<IncomingDataListener>();

	private Client mClient;

	private WifiP2pDevice mTargetDevice;

	/**
	 * Creates a WifiDirectConnector.
	 * @param activity the parent activity creating this connector
	 */
	public WifiDirectConnector(final Activity activity) {
		mParentActivity = activity;
	}

	/**
	 * Sets up the WifiDirectConnector. Meant to be called during the parent activity's onCreate().
	 */
	public void onCreate() {
		mWifiManager = (WifiP2pManager) mParentActivity.getSystemService(Context.WIFI_P2P_SERVICE);
		mWifiChannel = mWifiManager.initialize(mParentActivity, mParentActivity.getMainLooper(), null);

		mClient = new Client(mParentActivity);
	}

	/**
	 * Sets the IntentFilter for Wifi P2P intents. Meant to be called during the parent activity's onResume().
	 */
	public void onResume() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

		mBroadcastReceiver = new WifiDirectBroadcastReceiver(mWifiManager, mWifiChannel);
		mParentActivity.registerReceiver(mBroadcastReceiver, intentFilter);
	}

	/**
	 * Halts reception of registered intents. Meant to be called during the parent activity's onPause().
	 */
	public void onPause() {
		mParentActivity.unregisterReceiver(mBroadcastReceiver);
	}

	/**
	 * Starts the WifiP2p discovery process.
	 * @param listener the listener to be called back with results
	 */
	public void startDiscovery(final DiscoverPeersListener listener) {
		if (!mBroadcastReceiver.isP2PEnabled()) {
			listener.onP2pNotEnabled();
		} else {
			mWifiManager.discoverPeers(mWifiChannel, new WifiP2pManager.ActionListener() {
				@Override
				public void onSuccess() {
					listener.onDiscoverPeersSuccess();
				}

				@Override
				public void onFailure(final int reasonCode) {
					listener.onDiscoverPeersFailure(reasonCode);
					mWifiManager.cancelConnect(mWifiChannel, null);
					mWifiManager.stopPeerDiscovery(mWifiChannel, null);
				}
			});
		}
	}

	/**
	 * Cancels the discovery process.
	 */
	public void cancelDiscovery() {
		mWifiManager.stopPeerDiscovery(mWifiChannel, null);
	}

	/**
	 * Connects this device to a target device.
	 * @param device the device to connect to
	 * @param listener a listener to notify of results
	 */
	public void connect(final WifiP2pDevice device, final ConnectionInitiationListener listener) {
		mTargetDevice = device;

		// Called by the PeerDetail fragment when the Connect button is pressed.
		WifiP2pConfig config = new WifiP2pConfig();
		config.deviceAddress = device.deviceAddress;
		config.wps.setup = WpsInfo.PBC;
		config.groupOwnerIntent = 0;

		mWifiManager.connect(mWifiChannel, config, new ActionListener() {
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

	/**
	 * Disconnects this device from its current connection.
	 * @param listener a listener to notify of results
	 */
	public void disconnect(final DisconnectionListener listener) {
		mWifiManager.removeGroup(mWifiChannel, new ActionListener() {
			@Override
			public void onSuccess() {
				// On disconnect, stop the mClient from sending, and shut down the mServer thread.
				mClient.onDisconnect();

				mServer.cancel(true);
				mServer = null;

				listener.onDisconnectionSuccess();
			}

			@Override
			public void onFailure(final int reasonCode) {
				listener.onDisconnectionFailure();
				//mWifiManager.cancelConnect(mWifiChannel, null);
				//mWifiManager.stopPeerDiscovery(mWifiChannel, null);
			}
		});
	}

	/**
	 * Registers a StateChangeListener to be called back.
	 * @param listener the listener to be called back with results
	 */
	public void registerStateChangeListener(final P2pStateChangeListener listener) {
		if (mBroadcastReceiver != null) {
			mBroadcastReceiver.addStateChangeHandler(new P2pStateChangeListener() {

				@Override
				public void onPeersAvailable(final WifiP2pDeviceList peers) {
					listener.onPeersAvailable(peers);
				}

				@SuppressWarnings("unchecked")
				@Override
				public void onConnectionInfoAvailable(final WifiP2pInfo connectionInfo) {
					mServer = new Server();
					mServer.execute(mDataListeners);

					mClient.setConnectionSuccessful(connectionInfo, mTargetDevice);

					listener.onConnectionInfoAvailable(connectionInfo);
				}

				@Override
				public void onDeviceChanged(final WifiP2pDevice thisDevice) {
					listener.onDeviceChanged(thisDevice);
				}
			});
		}
	}

	/**
	 * Registers a data listener for incoming data on the WifiDirect connection.
	 * @param listener the listener to notify of incoming data
	 */
	public void registerDataListener(final IncomingDataListener listener) {
		mDataListeners.add(listener);
	}

	/**
	 * Returns the connection client.
	 * @return the connection client
	 */
	public Client getClient() {
		return mClient;
	}
}
