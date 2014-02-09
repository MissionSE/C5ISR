package com.missionse.kestrelweather.communication;

import android.app.Activity;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import com.missionse.kestrelweather.R;

import java.util.Set;

/**
 * Displays a list of discovered and paired devices in a dialog window.
 */
public class BluetoothDeviceListFragment extends DialogFragment {

	public static final String MAC_ADDRESS_KEY = "mac_address";

	private BluetoothAdapter mAdapter;
	private BluetoothDeviceAdapter mPairedDevicesArrayAdapter;
	private BluetoothDeviceAdapter mNewDevicesArrayAdapter;
	private Button mDiscoverDevicesButton;

	/**
	 * Creates a new BluetoothDeviceListFragment, packing data as necessary.
	 * @return a new BluetoothDeviceListFragment
	 */
	public static BluetoothDeviceListFragment newInstance() {
		return new BluetoothDeviceListFragment();
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_bluetooth_device_list,  container, false);
		mAdapter = BluetoothAdapter.getDefaultAdapter();

		if (contentView != null) {
			mDiscoverDevicesButton = (Button) contentView.findViewById(R.id.discover_devices_btn);
			mDiscoverDevicesButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View view) {
					mAdapter.cancelDiscovery();
					mAdapter.startDiscovery();

					mDiscoverDevicesButton.setEnabled(false);
					mDiscoverDevicesButton.setText(R.string.discovering);
				}
			});

			if (getActivity() != null) {
				mPairedDevicesArrayAdapter = new BluetoothDeviceAdapter(
					getActivity(), R.layout.fragment_bluetooth_device_list_entry);

				ListView pairedListView = (ListView) contentView.findViewById(R.id.paired_devices);
				pairedListView.setAdapter(mPairedDevicesArrayAdapter);
				pairedListView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(final AdapterView<?> adapterView, final View view, final int position,
						final long l) {
						mAdapter.cancelDiscovery();

						Intent result = new Intent();
						result.putExtra(MAC_ADDRESS_KEY, mPairedDevicesArrayAdapter.getItem(position).getAddress());
						if (getTargetFragment() != null) {
							getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, result);
						}

						BluetoothDeviceListFragment.this.dismiss();
					}
				});
				pairedListView.setEmptyView(contentView.findViewById(R.id.bluetooth_no_paired_devices));

				mNewDevicesArrayAdapter = new BluetoothDeviceAdapter(
					getActivity(), R.layout.fragment_bluetooth_device_list_entry);

				ListView newDevicesListView = (ListView) contentView.findViewById(R.id.discovered_devices);
				newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
				newDevicesListView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(final AdapterView<?> adapterView, final View view, final int position,
						final long l) {
						mAdapter.cancelDiscovery();

						Intent result = new Intent();
						result.putExtra(MAC_ADDRESS_KEY, mNewDevicesArrayAdapter.getItem(position).getAddress());
						if (getTargetFragment() != null) {
							getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, result);
						}

						BluetoothDeviceListFragment.this.dismiss();
					}
				});
				newDevicesListView.setEmptyView(contentView.findViewById(R.id.bluetooth_no_discovered_devices));

				populatePairedDevices();
			}
		}

		return contentView;
	}

	private void populatePairedDevices() {
		Set<BluetoothDevice> pairedDevices = mAdapter.getBondedDevices();
		if (pairedDevices != null && pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				mPairedDevicesArrayAdapter.add(device);
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (mAdapter != null) {
			mAdapter.cancelDiscovery();
		}
	}

	/**
	 * Adds a device to be displayed.
	 * @param device the device to display
	 */
	public void addDevice(final BluetoothDevice device) {
		mNewDevicesArrayAdapter.add(device);
	}

	/**
	 * Notifies this dialog that discovery has finished.
	 */
	public void onDiscoveryFinished() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mDiscoverDevicesButton.setEnabled(true);
				mDiscoverDevicesButton.setText(R.string.discover_devices);
			}
		}, 1000);
	}

	private class BluetoothDeviceAdapter extends ArrayAdapter<BluetoothDevice> {

		private int mResource;

		private BluetoothDeviceAdapter(final Context context, final int resource) {
			super(context, resource);
			mResource = resource;
		}

		@Override
		public View getView(final int position, View convertView, final ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(mResource, null);
			}

			if (convertView != null) {
				BluetoothDevice device = getItem(position);
				TextView deviceName = (TextView) convertView.findViewById(R.id.bluetooth_device_name);
				deviceName.setText(device.getName());
				TextView deviceMacAddress = (TextView) convertView.findViewById(R.id.bluetooth_device_mac_address);
				deviceMacAddress.setText(device.getAddress());
			}
			return convertView;
		}
	}
}
