package com.missionse.commandablemodel;

import java.util.Set;

import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class DeviceListFragment extends DialogFragment {

	private View contentView;

	private static final String SECURE_KEY = "secure";

	private static final String NO_DEVICES_FOUND = "No devices found";

	private boolean secure;

	private BluetoothAdapter adapter;
	private ArrayAdapter<String> pairedDevicesArrayAdapter;
	private ArrayAdapter<String> newDevicesArrayAdapter;

	public static DeviceListFragment newInstance(final boolean secure) {
		DeviceListFragment fragment = new DeviceListFragment();
		Bundle bundle = new Bundle();
		bundle.putBoolean(SECURE_KEY, secure);
		fragment.setArguments(bundle);

		return fragment;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		secure = getArguments().getBoolean(SECURE_KEY);

		this.setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.fragment_device_list, null);

		adapter = BluetoothAdapter.getDefaultAdapter();

		Button scanButton = (Button) contentView.findViewById(R.id.button_scan);
		scanButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View view) {
				startScanningForDiscoverableDevices();
				view.setVisibility(View.GONE);
			}
		});

		pairedDevicesArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.device_name);
		newDevicesArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.device_name);

		// Find and set up the ListView for paired devices
		ListView pairedListView = (ListView) contentView.findViewById(R.id.paired_devices);
		pairedListView.setAdapter(pairedDevicesArrayAdapter);
		pairedListView.setOnItemClickListener(mDeviceClickListener);

		// Find and set up the ListView for newly discovered devices
		ListView newDevicesListView = (ListView) contentView.findViewById(R.id.new_devices);
		newDevicesListView.setAdapter(newDevicesArrayAdapter);
		newDevicesListView.setOnItemClickListener(mDeviceClickListener);

		// Get a set of currently paired devices
		Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();

		// If there are paired devices, add each one to the ArrayAdapter
		if (pairedDevices.size() > 0) {
			contentView.findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
			for (BluetoothDevice device : pairedDevices) {
				pairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
			}
		} else {
			pairedDevicesArrayAdapter.add("No devices have been paired");
		}

		return contentView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (adapter != null) {
			adapter.cancelDiscovery();
		}
	}

	private void startScanningForDiscoverableDevices() {
		// Turn on sub-title for new devices
		contentView.findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

		adapter.cancelDiscovery();
		adapter.startDiscovery();
	}

	public void addDevice(final String string) {
		newDevicesArrayAdapter.remove(NO_DEVICES_FOUND);
		newDevicesArrayAdapter.add(string);
	}

	public void onDiscoveryFinished() {
		if (newDevicesArrayAdapter.getCount() == 0) {
			newDevicesArrayAdapter.add(NO_DEVICES_FOUND);
		}
	}

	// The on-click listener for all devices in the ListViews
	private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(final AdapterView<?> av, final View v, final int arg2, final long arg3) {
			// Cancel discovery because it's costly and we're about to connect
			adapter.cancelDiscovery();

			// Get the device MAC address, which is the last 17 chars in the View
			String info = ((TextView) v).getText().toString();
			String address = info.substring(info.length() - 17);

			((CommandableModelActivity) getActivity()).connectDevice(address, secure);

			DeviceListFragment.this.dismiss();
		}
	};
}
