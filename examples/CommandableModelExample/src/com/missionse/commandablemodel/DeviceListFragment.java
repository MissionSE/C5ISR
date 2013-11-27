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

/**
 * Displays a list of paired and discovered devices.
 */
public class DeviceListFragment extends DialogFragment {

	private View mContentView;

	private static final String SECURE_KEY = "mSecure";
	private static final String NO_DEVICES_FOUND = "No devices found";
	private static final int MAC_ADDRESS_LENGTH = 17;

	private boolean mSecure;

	private BluetoothAdapter mAdapter;
	private ArrayAdapter<String> mPairedDevicesArrayAdapter;
	private ArrayAdapter<String> mNewDevicesArrayAdapter;

	/**
	 * Creates a new DeviceListFragment, packing any data necessary.
	 * @param secure whether or not the initiated scan from this fragment will be secure
	 * @return a new DeviceListFragment
	 */
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

		mSecure = getArguments().getBoolean(SECURE_KEY);

		this.setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		mContentView = inflater.inflate(R.layout.fragment_device_list, null);

		mAdapter = BluetoothAdapter.getDefaultAdapter();

		Button scanButton = (Button) mContentView.findViewById(R.id.button_scan);
		scanButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View view) {
				startScanningForDiscoverableDevices();
				view.setVisibility(View.GONE);
			}
		});

		mPairedDevicesArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.device_name);
		mNewDevicesArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.device_name);

		// Find and set up the ListView for paired devices
		ListView pairedListView = (ListView) mContentView.findViewById(R.id.paired_devices);
		pairedListView.setAdapter(mPairedDevicesArrayAdapter);
		pairedListView.setOnItemClickListener(mDeviceClickListener);

		// Find and set up the ListView for newly discovered devices
		ListView newDevicesListView = (ListView) mContentView.findViewById(R.id.new_devices);
		newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
		newDevicesListView.setOnItemClickListener(mDeviceClickListener);

		// Get a set of currently paired devices
		Set<BluetoothDevice> pairedDevices = mAdapter.getBondedDevices();

		// If there are paired devices, add each one to the ArrayAdapter
		if (pairedDevices.size() > 0) {
			mContentView.findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
			for (BluetoothDevice device : pairedDevices) {
				mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
			}
		} else {
			mPairedDevicesArrayAdapter.add("No devices have been paired");
		}

		return mContentView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (mAdapter != null) {
			mAdapter.cancelDiscovery();
		}
	}

	private void startScanningForDiscoverableDevices() {
		// Turn on sub-title for new devices
		mContentView.findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

		mAdapter.cancelDiscovery();
		mAdapter.startDiscovery();
	}

	/**
	 * Adds a device to the discovered devices list.
	 * @param string the device to display
	 */
	public void addDevice(final String string) {
		mNewDevicesArrayAdapter.remove(NO_DEVICES_FOUND);
		mNewDevicesArrayAdapter.add(string);
	}

	/**
	 * Notifies this fragment that discovery has finished.
	 */
	public void onDiscoveryFinished() {
		if (mNewDevicesArrayAdapter.getCount() == 0) {
			mNewDevicesArrayAdapter.add(NO_DEVICES_FOUND);
		}
	}

	// The on-click listener for all devices in the ListViews
	private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(final AdapterView<?> av, final View v, final int arg2, final long arg3) {
			// Cancel discovery because it's costly and we're about to connect
			mAdapter.cancelDiscovery();

			// Get the device MAC address, which is the last 17 chars in the View
			String info = ((TextView) v).getText().toString();
			String address = info.substring(info.length() - MAC_ADDRESS_LENGTH);

			((CommandableModelActivity) getActivity()).connectDevice(address, mSecure);

			DeviceListFragment.this.dismiss();
		}
	};
}
