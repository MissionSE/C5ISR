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
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.Bundle;
import android.provider.Settings;
//import android.util.Log;
import android.widget.Toast;

public class WifiDirectActivity extends Activity {
	
	//private static final String TAG = "WifiDirectActivity";
	
	private final WifiDirectConnector wifiDirectConnector = new WifiDirectConnector();
		
	private SlidingMenu navigationDrawer;
	
	private ProgressDialog progressDialog;
	private boolean findingPeers = false;
	private boolean wasConnected = false;
		
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Wi-Fi Direct");
		setContentView(R.layout.activity_wifi_direct);
		
		wifiDirectConnector.onCreate(this);
		createNavigationDrawer(savedInstanceState);
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
		wifiDirectConnector.onResume(this);
		wifiDirectConnector.addStateChangeHandler(new P2pStateChangeHandler() {
			@Override
			public void onPeersAvailable(WifiP2pDeviceList peers) {
				if (progressDialog != null && progressDialog.isShowing() && findingPeers) {
		            progressDialog.dismiss();
		            findingPeers = false;
		        }
				
				if (peers.getDeviceList().size() == 0) {
					if (wasConnected) {
						showToast("Disconnected.");
						
					}
					else {
						showToast("No peers found.");
					}
					wasConnected = false;
		        }
				
				((DeviceDetailFragment) getFragmentManager().findFragmentById(R.id.frag_detail))
					.clearForDisconnect();
				
				((DeviceListFragment) getFragmentManager().findFragmentById(R.id.frag_list))
					.displayAvailablePeers(peers);
			}

			@Override
			public void onConnectionInfoAvailable(WifiP2pInfo connectionInfo) {
				if (progressDialog != null && progressDialog.isShowing()) {
		            progressDialog.dismiss();
			    }
				wasConnected = true;
				((DeviceDetailFragment) getFragmentManager().findFragmentById(R.id.frag_detail))
					.displayConnectionSuccessInfo(connectionInfo);
			}

			@Override
			public void onDeviceChanged(WifiP2pDevice thisDevice) {
				((DeviceListFragment) getFragmentManager().findFragmentById(R.id.frag_list))
					.displayDeviceInfo(thisDevice);
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
				
		        if (progressDialog != null && progressDialog.isShowing()) {
		            progressDialog.dismiss();
		        }
		        progressDialog = ProgressDialog.show(WifiDirectActivity.this,
		        		"Press back to cancel", "Finding peers...", true, true);
		        findingPeers = true;
			}

			@Override
			public void onDiscoverPeersFailure(int reasonCode) {
				showToast("Discovery Failed");
			}
		});
	}
	
	public void showDeviceDetails(WifiP2pDevice device) {
        DeviceDetailFragment fragment = (DeviceDetailFragment) getFragmentManager()
                .findFragmentById(R.id.frag_detail);
        fragment.showDeviceDetails(device);
    }
	
	public void connect(WifiP2pConfig config) {
		if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
	    }
	    progressDialog = ProgressDialog.show(this, "Press back to cancel", "Connecting to: " +
	    		config.deviceAddress, true, true);
		wifiDirectConnector.connect(config, new ConnectionInitiationListener() {
			@Override
			public void onConnectionInitiationSuccess() {
				showToast("Initiating connection...");
			}

			@Override
			public void onConnectionInitiationFailure() {
				showToast("Connection failed. Retry.");
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
        	    }
			}
		});
    }
	
	public void disconnect() {
		wasConnected = false;
        wifiDirectConnector.disconnect(new DisconnectionListener() {
			@Override
			public void onDisconnectionSuccess() {
				final DeviceDetailFragment fragment = (DeviceDetailFragment) getFragmentManager()
		                .findFragmentById(R.id.frag_detail);
		        fragment.clearForDisconnect();
			}

			@Override
			public void onDisconnectionFailure() {
				showToast("Disconnection failed. Try again.");
			}
        });
    }
}
