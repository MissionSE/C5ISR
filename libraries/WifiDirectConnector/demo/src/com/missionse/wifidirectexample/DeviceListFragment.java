package com.missionse.wifidirectexample;

import java.util.HashMap;
import java.util.Map;

import android.app.DialogFragment;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.missionse.wifidirect.WifiDirectConnector;

/**
 * Provides a user interface for displaying and selecting a device for WifiDirect connection initiation.
 */
public class DeviceListFragment extends DialogFragment {

	private WifiDirectConnector mWifiDirectConnector;

	private View mContentView;
	private ProgressBar mProgressBar;

	private static final String NO_DEVICES_FOUND = "No devices found";

	private ArrayAdapter<String> mDiscoveredDevicesArrayAdapter;
	private final Map<String, WifiP2pDevice> mDiscoveredDevices = new HashMap<String, WifiP2pDevice>();

	/**
	 * Provides a new DeviceListFragment.
	 * @param wifiDirectConnector connector via which WifiDirect calls can be made.
	 * @return a new DeviceListFragment
	 */
	public static DeviceListFragment newInstance(final WifiDirectConnector wifiDirectConnector) {
		DeviceListFragment fragment = new DeviceListFragment();
		fragment.setWifiDirectConnector(wifiDirectConnector);
		return fragment;
	}

	private void setWifiDirectConnector(final WifiDirectConnector connector) {
		mWifiDirectConnector = connector;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		mContentView = inflater.inflate(R.layout.fragment_device_list, null);

		mDiscoveredDevicesArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.device_name);

		// Find and set up the ListView for paired devices
		ListView pairedListView = (ListView) mContentView.findViewById(R.id.discovered_devices);
		pairedListView.setAdapter(mDiscoveredDevicesArrayAdapter);
		pairedListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> av, final View v, final int arg2, final long arg3) {
				// Get the device name
				String displayedText = ((TextView) v).getText().toString();
				String deviceName = displayedText.split("\n")[0];

				WifiP2pDevice selectedDevice = mDiscoveredDevices.get(deviceName);
				if (selectedDevice != null) {
					((WifiDirectExample) getActivity()).connectToDevice(selectedDevice);
					DeviceListFragment.this.dismiss();
				}
			}
		});

		mProgressBar = (ProgressBar) mContentView.findViewById(R.id.discovery_progress);
		mProgressBar.setVisibility(View.VISIBLE);

		return mContentView;
	}

	/**
	 * Sets the displayed available peers.
	 * @param peers list of discovered WifiP2pDevices
	 */
	public void setAvailablePeers(final WifiP2pDeviceList peers) {
		mDiscoveredDevices.clear();
		mDiscoveredDevicesArrayAdapter.clear();

		mProgressBar.setVisibility(View.INVISIBLE);

		if (peers == null) {
			mDiscoveredDevicesArrayAdapter.add(NO_DEVICES_FOUND);
		} else {
			for (WifiP2pDevice discoveredDevice : peers.getDeviceList()) {
				mDiscoveredDevices.put(discoveredDevice.deviceName, discoveredDevice);
				mDiscoveredDevicesArrayAdapter.add(discoveredDevice.deviceName + "\n" + discoveredDevice.deviceAddress);
			}
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		mWifiDirectConnector.cancelDiscovery();
	}
}
