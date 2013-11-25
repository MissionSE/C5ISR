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
import android.widget.TextView;

public class DeviceListFragment extends DialogFragment {

	private View contentView;

	private static final String NO_DEVICES_FOUND = "No devices found";

	private ArrayAdapter<String> discoveredDevicesArrayAdapter;
	private final Map<String, WifiP2pDevice> discoveredDevices = new HashMap<String, WifiP2pDevice>();

	public static DeviceListFragment newInstance() {
		DeviceListFragment fragment = new DeviceListFragment();
		return fragment;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.fragment_device_list, null);

		discoveredDevicesArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.device_name);

		// Find and set up the ListView for paired devices
		ListView pairedListView = (ListView) contentView.findViewById(R.id.discovered_devices);
		pairedListView.setAdapter(discoveredDevicesArrayAdapter);
		pairedListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> av, final View v, final int arg2, final long arg3) {
				// Get the device name
				String displayedText = ((TextView) v).getText().toString();
				String deviceName = displayedText.split("\n")[0];

				WifiP2pDevice selectedDevice = discoveredDevices.get(deviceName);
				if (selectedDevice != null) {
					((WifiDirectExample) getActivity()).connectToDevice(selectedDevice);
					DeviceListFragment.this.dismiss();
				}
			}
		});

		return contentView;
	}

	public void setAvailablePeers(final WifiP2pDeviceList peers) {
		discoveredDevices.clear();

		if (peers == null) {
			discoveredDevicesArrayAdapter.add(NO_DEVICES_FOUND);
		} else {
			for (WifiP2pDevice discoveredDevice : peers.getDeviceList()) {
				discoveredDevices.put(discoveredDevice.deviceName, discoveredDevice);
				discoveredDevicesArrayAdapter.add(discoveredDevice.deviceName + "\n" + discoveredDevice.deviceAddress);
			}
		}
	}
}
