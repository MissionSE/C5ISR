package com.missionse.commandablemodel;

import com.missionse.wifidirect.ConnectionInitiationListener;
import com.missionse.wifidirect.DisconnectionListener;
import com.missionse.wifidirect.DiscoverPeersListener;
import com.missionse.wifidirect.P2pStateChangeHandler;
import com.missionse.wifidirect.WifiDirectConnector;

import android.app.ActionBar;
import android.app.Activity;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class CommandableModelActivity extends Activity {
	
	private static final String TAG = CommandableModelActivity.class.getSimpleName();
	
	private final WifiDirectConnector wifiDirectConnector = new WifiDirectConnector();
	
	private GutterViewPager viewPager;
	private SectionFragmentPagerAdapter pagerAdapter;
	
	private PeersListFragment peersListFragment;
	private PeerDetailFragment peerDetailFragment;
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.app_name);	
		setContentView(R.layout.activity_main);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);
		
		peersListFragment = new PeersListFragment();
		peerDetailFragment = new PeerDetailFragment();
		
		pagerAdapter = new SectionFragmentPagerAdapter(getFragmentManager());
		pagerAdapter.setPage(0, peersListFragment);
		pagerAdapter.setPage(1, peerDetailFragment);
		//pagerAdapter.setPage(2, modelViewerFragment);
		
		viewPager = (GutterViewPager) findViewById(R.id.content_pager);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(pagerAdapter);
        viewPager.enableGutterForPage(2);
        
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageSelected(int pageNumber) {
				if (pageNumber == 1) {
					peerDetailFragment.refresh();
				}
			}
        });

		wifiDirectConnector.onCreate(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		Log.e(TAG, "on resume");
		wifiDirectConnector.onResume(this);
		wifiDirectConnector.addStateChangeHandler(new P2pStateChangeHandler() {
			@Override
			public void onPeersAvailable(WifiP2pDeviceList peers) {
				//Peer discovery has finished, and we have a list of peers.
				peersListFragment.setAvailablePeers(peers);
				
				if (peers.getDeviceList().size() == 0) {
					peerDetailFragment.setTargetDevice(null);
					peerDetailFragment.setConnectionSuccessfulInformation(null);
					
					if (viewPager.getCurrentItem() == 1) {
						peerDetailFragment.refresh();
					}
				}
			}

			@Override
			public void onConnectionInfoAvailable(WifiP2pInfo connectionInfo) {
				//We have made a connection, and this is the specific information.
				peerDetailFragment.setConnectionSuccessfulInformation(connectionInfo);
				
				if (viewPager.getCurrentItem() == 1) {
					peerDetailFragment.refresh();
				}
			}

			@Override
			public void onDeviceChanged(WifiP2pDevice thisDevice) {
				//Our own device has changed.
				peersListFragment.setThisDeviceInfo(thisDevice);
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
    	case R.id.menu_settings:
    		//TODO: handle settings
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
    			public void onDiscoverPeersFailure(int reasonCode) {
    				showToast("Discovery Failed");
    			}
    		});
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
	
	public void showPeerDetails(WifiP2pDevice device) {
		peerDetailFragment.setTargetDevice(device);
		viewPager.setCurrentItem(1);
    }
	
	private void showToast(final String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
				peerDetailFragment.setTargetDevice(null);
				peerDetailFragment.setConnectionSuccessfulInformation(null);
				
				if (viewPager.getCurrentItem() == 1) {
					peerDetailFragment.refresh();
				}
			}

			@Override
			public void onDisconnectionFailure() {
				showToast("Disconnection failed. Try again.");
			}
        });
    }
}
