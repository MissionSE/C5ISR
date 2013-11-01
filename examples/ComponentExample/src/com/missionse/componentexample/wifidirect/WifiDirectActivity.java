package com.missionse.componentexample.wifidirect;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.missionse.componentexample.R;
import com.missionse.wifidirect.ConnectionInitiationListener;
import com.missionse.wifidirect.DisconnectionListener;
import com.missionse.wifidirect.P2pStateChangeHandler;
import com.missionse.wifidirect.WifiDirectConnector;
import com.missionse.wifidirect.DiscoverPeersListener;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
//import android.util.Log;
import android.widget.Toast;

public class WifiDirectActivity extends Activity {
	
	//private static final String TAG = "WifiDirectActivity";
	
	private final WifiDirectConnector wifiDirectConnector = new WifiDirectConnector();
	
	private SlidingMenu navigationDrawer;
	
	private boolean findingPeers = false;
	private boolean wasConnected = false;
	
	private DeviceListFragment deviceListFragment;
	private DeviceDetailFragment deviceDetailFragment;
		
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Wi-Fi Direct");		
		setContentView(R.layout.activity_wifi_direct);
		
		deviceListFragment = new DeviceListFragment();
		deviceDetailFragment = new DeviceDetailFragment();
		
		wifiDirectConnector.onCreate(this);
		createNavigationDrawer(savedInstanceState);
		
		FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
		transaction.replace(R.id.content, deviceListFragment);
		transaction.commit();
	}
	
	private void createNavigationDrawer(final Bundle savedInstanceState) {
		navigationDrawer = new SlidingMenu(this);
        navigationDrawer.setMode(SlidingMenu.LEFT);
        navigationDrawer.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        navigationDrawer.setShadowWidthRes(R.dimen.drawer_shadow_width);
        navigationDrawer.setShadowDrawable(R.drawable.shadow);
        navigationDrawer.setBehindWidthRes(R.dimen.drawer_width);
        navigationDrawer.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        navigationDrawer.setMenu(R.layout.nav_drawer);
        
        Fragment drawerFragment;
        if (savedInstanceState == null) {
			FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
			drawerFragment = new WifiDirectDrawerFragment();
			transaction.replace(R.id.menu_frame, drawerFragment);
			transaction.commit();
        }
	}

	@Override
	public void onResume() {
		super.onResume();
		
		findViewById(R.id.peer_discovery_progress).setVisibility(View.GONE);
		
		wifiDirectConnector.onResume(this);
		wifiDirectConnector.addStateChangeHandler(new P2pStateChangeHandler() {
			@Override
			public void onPeersAvailable(WifiP2pDeviceList peers) {
				if (findingPeers) {
					findViewById(R.id.peer_discovery_progress).setVisibility(View.GONE);
		            findingPeers = false;
		        }
				
				if (peers.getDeviceList().size() == 0) {
					if (wasConnected) {
						showToast("Disconnected.");
						deviceDetailFragment.clearForDisconnect();
					}
					else {
						showToast("No peers found.");
					}
					
					if (deviceDetailFragment.isVisible()) {
						WifiDirectActivity.this.onBackPressed();
					}
					
					wasConnected = false;
		        }
				else {
					boolean connectedToAPeer = false;
					for (WifiP2pDevice peer : peers.getDeviceList()) {
						if (peer.status == WifiP2pDevice.CONNECTED) {
							connectedToAPeer = true;
						}
					}
					if (!connectedToAPeer) {
						deviceDetailFragment.clearForDisconnect();
					}
				}

				deviceListFragment.setAvailablePeers(peers);
			}

			@Override
			public void onConnectionInfoAvailable(WifiP2pInfo connectionInfo) {
				wasConnected = true;
				deviceDetailFragment.setConnectionSuccessInfo(connectionInfo);
				
				if (deviceDetailFragment.isVisible()) {
					deviceDetailFragment.showConnectionInfo();
				}
			}

			@Override
			public void onDeviceChanged(WifiP2pDevice thisDevice) {
				TextView view = (TextView) findViewById(R.id.this_device_name);
		        view.setText(thisDevice.deviceName);
		        view = (TextView) findViewById(R.id.this_device_status);
		        view.setText(WifiDirectConnector.deviceStatuses.get(thisDevice.status));
			}
		});
	}
	
	@Override
    public void onPause() {
        super.onPause();
        wifiDirectConnector.onPause(this);
    }
	
	@Override
    public void onBackPressed() {
		if (findViewById(R.id.peer_discovery_progress) != null) {
    		findViewById(R.id.peer_discovery_progress).setVisibility(View.GONE);
    	}
		
		if (navigationDrawer.isMenuShowing()) {
    		navigationDrawer.showContent(true);
    	}
    	else {
    		super.onBackPressed();
    	}
    }
	
	private void showToast(final String message) {
		Toast.makeText(WifiDirectActivity.this, message, Toast.LENGTH_SHORT).show();
	}
	
	public void enableP2P() {
		//We can't actually enable it ourselves, but we can direct the user to do so.
		startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
	}
	
	public void discoverPeers() {
		wifiDirectConnector.discoverPeers(new DiscoverPeersListener() {
			@Override
			public void onP2pNotEnabled() {
				showToast("You must enable P2P first!");
			}

			@Override
			public void onDiscoverPeersSuccess() {
				showToast("Discovery Initiated");
				navigationDrawer.showContent();
		        findViewById(R.id.peer_discovery_progress).setVisibility(View.VISIBLE);
		        findingPeers = true;
			}

			@Override
			public void onDiscoverPeersFailure(int reasonCode) {
				showToast("Discovery Failed");
			}
		});
	}
	
	public void showPeerDetails(WifiP2pDevice device) {
		deviceDetailFragment.setTargetPeer(device);
		
		FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left,
				R.anim.slide_from_left, R.anim.slide_to_right);
		transaction.replace(R.id.content, deviceDetailFragment);
		transaction.addToBackStack(null);
		transaction.commit();
    }
	
	public void connect(WifiP2pConfig config) {
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
        wifiDirectConnector.disconnect(new DisconnectionListener() {
			@Override
			public void onDisconnectionSuccess() {
				deviceDetailFragment.clearForDisconnect();
			}

			@Override
			public void onDisconnectionFailure() {
				showToast("Disconnection failed. Try again.");
			}
        });
    }
}
