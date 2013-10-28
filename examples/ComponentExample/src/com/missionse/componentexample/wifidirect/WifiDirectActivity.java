package com.missionse.componentexample.wifidirect;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.missionse.componentexample.R;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

public class WifiDirectActivity extends Activity {
	
	private WifiP2pManager wifiManager;
	private Channel wifiChannel;
	
	private WifiDirectBroadcastReceiver broadcastReceiver;
	
	private final IntentFilter intentFilter = new IntentFilter();
	
	private SlidingMenu navigationDrawer;
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi_direct);
		
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
		
		wifiManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		wifiChannel = wifiManager.initialize(this, getMainLooper(), null);
		
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
		broadcastReceiver = new WifiDirectBroadcastReceiver(wifiManager, wifiChannel, getFragmentManager());
		registerReceiver(broadcastReceiver, intentFilter);
	}
	
	@Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
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
	
	public void enableP2P() {
		//We can't actually enable it ourselves, but we can direct the user to do so.
		startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
	}
	
	public void discoverPeers() {
		if (!broadcastReceiver.isP2PEnabled()) {
			Toast.makeText(this, "You must enable P2P first!", Toast.LENGTH_SHORT).show();
		}
		else {
			final DeviceListFragment fragment = (DeviceListFragment) getFragmentManager()
					.findFragmentById(R.id.frag_list);
			fragment.onInitiateDiscovery();
			navigationDrawer.showContent();
			wifiManager.discoverPeers(wifiChannel, new WifiP2pManager.ActionListener() {
				@Override
				public void onSuccess() {
					Toast.makeText(WifiDirectActivity.this, "Discovery Initiated", Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onFailure(int reasonCode) {
					Toast.makeText(WifiDirectActivity.this, "Discovery Failed : " + reasonCode, Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
	
	public void showDetails(WifiP2pDevice device) {
        DeviceDetailFragment fragment = (DeviceDetailFragment) getFragmentManager()
                .findFragmentById(R.id.frag_detail);
        fragment.showDetails(device);

    }
	
	public void connect(WifiP2pConfig config) {
        wifiManager.connect(wifiChannel, config, new ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(WifiDirectActivity.this, "Initiating connection...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(WifiDirectActivity.this, "Connection failed. Retry.", Toast.LENGTH_SHORT).show();
                final DeviceDetailFragment fragment = (DeviceDetailFragment) getFragmentManager()
                		.findFragmentById(R.id.frag_detail);
                fragment.endProgressBar();
            }
        });
    }
	
	public void disconnect() {
        final DeviceDetailFragment fragment = (DeviceDetailFragment) getFragmentManager()
                .findFragmentById(R.id.frag_detail);
        fragment.resetViews();
        wifiManager.removeGroup(wifiChannel, new ActionListener() {
            @Override
            public void onFailure(int reasonCode) {
            	//Nothing to do for now.
            }

            @Override
            public void onSuccess() {
                fragment.getView().setVisibility(View.GONE);
            }
        });
    }
}
