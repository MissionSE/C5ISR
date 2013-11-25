package com.missionse.bluetooth;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;

import com.missionse.bluetooth.network.BluetoothNetworkService;

public class BluetoothConnector {

	private final BluetoothAdapter bluetoothAdapter;
	private Activity parentActivity;

	private BroadcastReceiver broadcastReceiver;
	private BluetoothNetworkService networkService;

	private final List<Handler> bluetoothEventHandlers = new ArrayList<Handler>();

	public BluetoothConnector(final Activity activity) {
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		parentActivity = activity;
	}

	public void onCreate(final BluetoothIntentListener listener) {
		// Register for broadcasts when a device is discovered and discovery has finished.
		broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(final Context context, final Intent intent) {
				String action = intent.getAction();
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					// Get the BluetoothDevice object from the Intent.
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					listener.onDeviceFound(device);
				} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
					listener.onDiscoveryFinished();
				}
			}
		};

		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		parentActivity.registerReceiver(broadcastReceiver, filter);
	}

	public void createService() {
		if (networkService == null) {
			networkService = new BluetoothNetworkService(parentActivity);
			for (Handler handler : bluetoothEventHandlers) {
				networkService.addHandler(handler);
			}
		}
	}

	public void startService() {
		if (networkService != null) {
			networkService.start(true);
		}
	}

	public void stopService() {
		parentActivity.unregisterReceiver(broadcastReceiver);

		if (networkService != null) {
			networkService.stop();
		}
	}

	public BluetoothNetworkService getService() {
		return networkService;
	}

	public void registerHandler(final Handler handler) {
		bluetoothEventHandlers.add(handler);
	}

	public void connect(final String address, final boolean secure) {
		// Get the BluetoothDevice object...
		BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
		// ...and attempt to connect to the device.
		networkService.connect(device, secure);
	}

	public void startDiscovery(final int seconds) {
		if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, seconds);
			parentActivity.startActivity(discoverableIntent);
		}
	}
}
