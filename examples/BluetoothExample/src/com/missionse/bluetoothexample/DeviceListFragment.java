package com.missionse.bluetoothexample;

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

	private boolean secure;

	private BluetoothAdapter mBtAdapter;
	private ArrayAdapter<String> mPairedDevicesArrayAdapter;
	private ArrayAdapter<String> mNewDevicesArrayAdapter;

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
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.device_list, null);

		Button scanButton = (Button) contentView.findViewById(R.id.button_scan);
		scanButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View view) {
				startScanningForDiscoverableDevices();
				view.setVisibility(View.GONE);
			}
		});

		// Initialize array adapters. One for already paired devices and
		// one for newly discovered devices
		mPairedDevicesArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.device_name);
		mNewDevicesArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.device_name);

		// Find and set up the ListView for paired devices
		ListView pairedListView = (ListView) contentView.findViewById(R.id.paired_devices);
		pairedListView.setAdapter(mPairedDevicesArrayAdapter);
		pairedListView.setOnItemClickListener(mDeviceClickListener);

		// Find and set up the ListView for newly discovered devices
		ListView newDevicesListView = (ListView) contentView.findViewById(R.id.new_devices);
		newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
		newDevicesListView.setOnItemClickListener(mDeviceClickListener);

		// Get the local Bluetooth adapter
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();

		// Get a set of currently paired devices
		Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

		// If there are paired devices, add each one to the ArrayAdapter
		if (pairedDevices.size() > 0) {
			contentView.findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
			for (BluetoothDevice device : pairedDevices) {
				mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
			}
		} else {
			String noDevices = getResources().getText(R.string.none_paired).toString();
			mPairedDevicesArrayAdapter.add(noDevices);
		}

		return contentView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// Make sure we're not doing discovery anymore
		if (mBtAdapter != null) {
			mBtAdapter.cancelDiscovery();
		}
	}

	private void startScanningForDiscoverableDevices() {
		// Indicate scanning in the title
		//setProgressBarIndeterminateVisibility(true);
		//setTitle(R.string.scanning);

		// Turn on sub-title for new devices
		contentView.findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

		// If we're already discovering, stop it
		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();
		}

		// Request discover from BluetoothAdapter
		mBtAdapter.startDiscovery();
	}

	public void addDevice(final String string) {
		mNewDevicesArrayAdapter.add(string);
	}

	public void onDiscoveryFinished() {
		if (mNewDevicesArrayAdapter.getCount() == 0) {
			String noDevices = getResources().getText(R.string.none_found).toString();
			mNewDevicesArrayAdapter.add(noDevices);
		}
	}

	// The on-click listener for all devices in the ListViews
	private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(final AdapterView<?> av, final View v, final int arg2, final long arg3) {
			// Cancel discovery because it's costly and we're about to connect
			mBtAdapter.cancelDiscovery();

			// Get the device MAC address, which is the last 17 chars in the View
			String info = ((TextView) v).getText().toString();
			String address = info.substring(info.length() - 17);

			((BluetoothExample) getActivity()).connectDevice(address, secure);

			DeviceListFragment.this.dismiss();
		}
	};
}
