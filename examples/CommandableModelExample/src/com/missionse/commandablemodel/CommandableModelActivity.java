package com.missionse.commandablemodel;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.missionse.commandablemodel.network.ModelControllerClient;
import com.missionse.commandablemodel.network.ModelControllerServer;
import com.missionse.commandablemodel.network.NotifyingModelGestureListener;
import com.missionse.modelviewer.ModelViewerFragment;
import com.missionse.modelviewer.ModelViewerFragmentFactory;
import com.missionse.uiextensions.viewpager.DisableableViewPager;
import com.missionse.uiextensions.viewpager.SectionFragmentPagerAdapter;
import com.missionse.wifidirect.ConnectionInitiationListener;
import com.missionse.wifidirect.DisconnectionListener;
import com.missionse.wifidirect.DiscoverPeersListener;
import com.missionse.wifidirect.P2pStateChangeHandler;
import com.missionse.wifidirect.WifiDirectConnector;

public class CommandableModelActivity extends Activity {

	// private static final String TAG = CommandableModelActivity.class.getSimpleName();

	@SuppressLint("UseSparseArrays")
	private final Map<Integer, Fragment> pages = new HashMap<Integer, Fragment>();

	private final WifiDirectConnector wifiDirectConnector = new WifiDirectConnector();

	private DisableableViewPager viewPager;
	private SectionFragmentPagerAdapter pagerAdapter;

	private PeersListFragment peersListFragment;
	private PeerDetailFragment peerDetailFragment;
	private ModelViewerFragment modelFragment;

	private NotifyingModelGestureListener modelGestureListener;

	private ModelControllerClient modelClient;
	private ModelControllerServer modelServer;

	private WifiP2pDevice targetDevice;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.app_name);
		setContentView(R.layout.activity_main);

		// Set up action bar for refresh button to be displayed.
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE
				| ActionBar.DISPLAY_SHOW_CUSTOM);

		// Create WiFi Direct fragments
		peersListFragment = new PeersListFragment();
		peerDetailFragment = new PeerDetailFragment();

		// Create the model client
		modelClient = new ModelControllerClient(this);

		// Create the gesture listener and add the model client as a recipient of model changes
		modelGestureListener = new NotifyingModelGestureListener();
		modelGestureListener.addRecipient(modelClient);

		// Create the ModelViewer fragment and give the ModelController to the ModelClient (to retrieve state changes)
		// when necessary
		modelFragment = ModelViewerFragmentFactory.createObjModelFragment(R.raw.multiobjects_obj, modelGestureListener);
		modelClient.setModelViewerFragment(modelFragment);

		// Set up the page number to fragment map, create Pager Adapter, and set fragments.
		pages.put(0, peersListFragment);
		pages.put(1, peerDetailFragment);
		pages.put(2, modelFragment);
		pagerAdapter = new SectionFragmentPagerAdapter(getFragmentManager());
		for (Map.Entry<Integer, Fragment> page : pages.entrySet()) {
			pagerAdapter.setPage(page.getKey().intValue(), page.getValue());
		}

		// Create ViewPager
		viewPager = (DisableableViewPager) findViewById(R.id.content_pager);
		viewPager.setOffscreenPageLimit(4);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setCurrentItem(0);

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(final int arg0) {
			}

			@Override
			public void onPageScrolled(final int arg0, final float arg1, final int arg2) {
			}

			@Override
			public void onPageSelected(final int pageNumber) {
				Fragment targetFragment = pages.get(pageNumber);
				if (targetFragment instanceof PeersListFragment) {
					setTitle(R.string.app_name);
					peersListFragment.refresh();
				} else if (targetFragment instanceof PeerDetailFragment) {
					if (targetDevice != null) {
						setTitle(targetDevice.deviceName);
					} else {
						setTitle(R.string.app_name);
					}
					peerDetailFragment.refresh();
				}
			}
		});

		// Ready our WiFi Direct connector
		wifiDirectConnector.onCreate(this);
	}

	@Override
	public void onResume() {
		super.onResume();

		wifiDirectConnector.onResume(this);
		wifiDirectConnector.addStateChangeHandler(new P2pStateChangeHandler() {
			@Override
			public void onPeersAvailable(final WifiP2pDeviceList peers) {
				// Peer discovery has finished, and we have a list of peers.
				peersListFragment.setAvailablePeers(peers);

				// If we got a list of 0 peers, we've either disconnected, or there are no peers to be found after
				// the timeout.
				if (peers.getDeviceList().size() == 0) {
					// Ensure that the detail fragment is made aware of a potential disconnect.
					peerDetailFragment.setTargetDevice(null);
					peerDetailFragment.setConnectionSuccessfulInformation(null);
					refreshPeerDetailsIfShowing();

					viewPager.setCurrentItem(0);
				}
			}

			@Override
			public void onConnectionInfoAvailable(final WifiP2pInfo connectionInfo) {
				// We have made a connection, and the PeerDetail fragment should receive the connection information.
				peerDetailFragment.setConnectionSuccessfulInformation(connectionInfo);

				refreshPeerDetailsIfShowing();

				// Start the server thread to listen for incoming model state changes.
				modelServer = new ModelControllerServer();
				modelServer.execute(modelFragment);

				modelClient.onConnectionSuccessful(connectionInfo, targetDevice);
			}

			@Override
			public void onDeviceChanged(final WifiP2pDevice thisDevice) {
				// Our own device has changed.
				peersListFragment.setThisDeviceInfo(thisDevice);

				refreshPeersListIfShowing();
			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();
		wifiDirectConnector.onPause(this);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_enable_p2p:
				// We can't actually enable it ourselves, but we can direct the user to do so.
				// Note: In most current devices (and likely those with 4.0+, if WiFi is on, so is WiFi Direct
				// automatically.
				startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
				return true;
			case R.id.menu_discover_peers:
				wifiDirectConnector.discoverPeers(new DiscoverPeersListener() {
					@Override
					public void onP2pNotEnabled() {
						showToast("You must enable P2P first!");
					}

					@Override
					public void onDiscoverPeersSuccess() {
						showToast("Discovery Initiated");
						CommandableModelActivity.this.peersListFragment.discoveryInitiated();
					}

					@Override
					public void onDiscoverPeersFailure(final int reasonCode) {
						showToast("Discovery Failed");
					}
				});
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void showPeerDetails(final WifiP2pDevice device) {
		// This is called by the PeerList fragment, when an item is selected. Save off the target device for
		// the server connection, give it to the PeerDetail fragment, and switch the ViewPager automatically.
		targetDevice = device;
		peerDetailFragment.setTargetDevice(device);
		viewPager.setCurrentItem(1);
	}

	private void showToast(final String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	public void connect() {
		// Called by the PeerDetail fragment when the Connect button is pressed.
		WifiP2pConfig config = new WifiP2pConfig();
		config.deviceAddress = targetDevice.deviceAddress;
		config.wps.setup = WpsInfo.PBC;
		config.groupOwnerIntent = 0;

		wifiDirectConnector.connect(config, new ConnectionInitiationListener() {
			@Override
			public void onConnectionInitiationSuccess() {
				showToast("Initiating connection...");
			}

			@Override
			public void onConnectionInitiationFailure() {
				showToast("Connection failed. Retry.");
			}
		});
	}

	public void disconnect() {
		// Called by the PeerDetail fragment when the Disconnect button is pressed.
		wifiDirectConnector.disconnect(new DisconnectionListener() {
			@Override
			public void onDisconnectionSuccess() {
				peerDetailFragment.setTargetDevice(null);
				peerDetailFragment.setConnectionSuccessfulInformation(null);
				refreshPeerDetailsIfShowing();
				viewPager.setCurrentItem(0);

				// On disconnect, stop the client from sending, and shut down the server thread.
				modelClient.onDisconnect();

				modelServer.cancel(true);
				modelServer = null;

				targetDevice = null;
			}

			@Override
			public void onDisconnectionFailure() {
				showToast("Disconnection failed. Try again.");
			}
		});
	}

	private void refreshPeerDetailsIfShowing() {
		// If the PeerDetail fragment is currently showing, refresh the information currently
		// displayed. Otherwise, the information will be refreshed next time we switch to it.
		Fragment currentPage = pages.get(viewPager.getCurrentItem());
		if (currentPage instanceof PeerDetailFragment) {
			peerDetailFragment.refresh();
		}
	}

	private void refreshPeersListIfShowing() {
		// If the PeersList fragment is currently showing, refresh the This Device information
		// currently displayed. Note that the list itself won't be refreshed, as that is handled automatically
		// by the ListAdapter.
		Fragment currentPage = pages.get(viewPager.getCurrentItem());
		if (currentPage instanceof PeersListFragment) {
			peersListFragment.refresh();
		}
	}
}
