package com.missionse.wifidirectexample;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.missionse.wifidirect.WifiDirectConnector;
import com.missionse.wifidirect.listener.ConnectionInitiationListener;
import com.missionse.wifidirect.listener.DisconnectionListener;
import com.missionse.wifidirect.listener.DiscoverPeersListener;
import com.missionse.wifidirect.listener.P2pStateChangeListener;

/**
 * Acts as the main entry point to the WifiDirectExample application, displaying the conversation fragment and handling
 * all WifiDirectConnector callbacks.
 */
public class WifiDirectExample extends Activity {

	private Menu mMainMenu;

	private WifiDirectConnector mWifiDirectConnector;
	private ConversationFragment mConversationFragment;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi_direct_example);
		mWifiDirectConnector = new WifiDirectConnector(this);
		mWifiDirectConnector.onCreate();

		mConversationFragment = new ConversationFragment();
		getFragmentManager().beginTransaction().replace(R.id.content, mConversationFragment).commit();

		mWifiDirectConnector.registerDataListener(mConversationFragment);

		getActionBar().setSubtitle("disconnected");
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		mMainMenu = menu;
		showDiscoveryButton();
		return true;
	}

	private void showDiscoveryButton() {
		mMainMenu.findItem(R.id.disconnect).setVisible(false);
		mMainMenu.findItem(R.id.disconnect).setEnabled(false);

		mMainMenu.findItem(R.id.discover_peers).setVisible(true);
		mMainMenu.findItem(R.id.discover_peers).setEnabled(true);
	}

	private void showDisconnectButton() {
		try {
			mMainMenu.findItem(R.id.disconnect).setVisible(true);
			mMainMenu.findItem(R.id.disconnect).setEnabled(true);

			mMainMenu.findItem(R.id.discover_peers).setVisible(false);
			mMainMenu.findItem(R.id.discover_peers).setEnabled(false);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.discover_peers:
				discoverPeers();
				return true;
			case R.id.disconnect:
				disconnect();
				return true;
			default:
				break;
		}
		return false;
	}

	private void discoverPeers() {
		mWifiDirectConnector.startDiscovery(new DiscoverPeersListener() {
			@Override
			public void onP2pNotEnabled() {
				Toast.makeText(WifiDirectExample.this, "You must enable P2P first!", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onDiscoverPeersSuccess() {
				Toast.makeText(WifiDirectExample.this, "Discovery Initiated", Toast.LENGTH_SHORT).show();

				hideDiscoveryDialog();
				showDiscoveryDialog();
			}

			@Override
			public void onDiscoverPeersFailure(final int reasonCode) {
				Toast.makeText(WifiDirectExample.this, "Discovery Failed", Toast.LENGTH_SHORT).show();

				hideDiscoveryDialog();
			}
		});
	}

	private void hideDiscoveryDialog() {
		//Determine if the dialog is showing, and kill it if so.
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		Fragment previousFragment = getFragmentManager().findFragmentByTag("dialog");
		if (previousFragment != null) {
			transaction.remove(previousFragment);
		}
		transaction.addToBackStack(null).commit();
	}

	private void showDiscoveryDialog() {
		// Launch the DeviceListActivity to see devices and do a scan.
		DeviceListFragment deviceListFragment = DeviceListFragment.newInstance(mWifiDirectConnector);
		deviceListFragment.show(getFragmentManager(), "dialog");
	}

	/**
	 * Disconnects the WifiDirect connection.
	 */
	public void disconnect() {
		// Called by the PeerDetail fragment when the Disconnect button is pressed.
		mWifiDirectConnector.disconnect(new DisconnectionListener() {
			@Override
			public void onDisconnectionSuccess() {
				// The remote device has been disconnected.
				showDiscoveryButton();
				getActionBar().setSubtitle("disconnected");
				Toast.makeText(WifiDirectExample.this, "Disconnection successful.", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onDisconnectionFailure() {
				showDiscoveryButton();
				getActionBar().setSubtitle("disconnected");
				Toast.makeText(WifiDirectExample.this, "Disconnection failed. Retry.", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		mWifiDirectConnector.onResume();
		mWifiDirectConnector.registerStateChangeListener(new P2pStateChangeListener() {
			@Override
			public void onPeersAvailable(final WifiP2pDeviceList peers) {

				if (peers.getDeviceList().size() == 0) {
					Fragment deviceListFragment = getFragmentManager().findFragmentByTag("dialog");
					if (deviceListFragment != null) {
						((DeviceListFragment) deviceListFragment).setAvailablePeers(null);
					}
				} else {
					Fragment deviceListFragment = getFragmentManager().findFragmentByTag("dialog");
					if (deviceListFragment != null) {
						((DeviceListFragment) deviceListFragment).setAvailablePeers(peers);
					}
				}
			}

			@Override
			public void onConnectionInfoAvailable(final WifiP2pInfo connectionInfo) {
				// We have made a connection.
				hideDiscoveryDialog();
				getActionBar().setSubtitle("connected");
				showDisconnectButton();

				Toast.makeText(WifiDirectExample.this, "Connection successful.", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onDeviceChanged(final WifiP2pDevice thisDevice) {
				// Our own device has changed.
			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();
		mWifiDirectConnector.onPause();
		hideDiscoveryDialog();
	}

	/**
	 * Connects via WifiDirect to a specific device.
	 * @param device the device to which a connection should be initiated.
	 */
	public void connectToDevice(final WifiP2pDevice device) {
		mWifiDirectConnector.connect(device, new ConnectionInitiationListener() {
			@Override
			public void onConnectionInitiationSuccess() {
				Toast.makeText(WifiDirectExample.this, "Initiating connection...", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onConnectionInitiationFailure() {
				Toast.makeText(WifiDirectExample.this, "Connection failed. Retry.", Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * Sends a message via the WifiDirect connection.
	 * @param message the message to be sent
	 * @return the success/failure of the data sending
	 */
	public boolean sendMessage(final String message) {
		return mWifiDirectConnector.getClient().sendData(message.getBytes());
	}
}
