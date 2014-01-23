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
import com.missionse.bluetooth.network.ServiceIdentifier.ConnectionType;

/**
 * Acts as the main wrapper around the BluetoothAdapter, managing the BluetoothNetworkService.
 */
public class BluetoothConnector {

	private final BluetoothAdapter mBluetoothAdapter;
	private Activity mParentActivity;

	private BroadcastReceiver mBroadcastReceiver;
	private BluetoothNetworkService mNetworkService;

	private final List<Handler> mBluetoothEventHandlers = new ArrayList<Handler>();

	/**
	 * Creates a new BluetoothConnector.
	 * @param activity the parent activity that created this connector
	 */
	public BluetoothConnector(final Activity activity) {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		mParentActivity = activity;
	}

	/**
	 * Prepares the broadcast receiver for incoming BT intents. To be called from parent activity's onCreate().
	 * @param listener the listener to notify of incoming BT intents
	 */
	public void onCreate(final BluetoothIntentListener listener) {
		// Register for broadcasts when a device is discovered and discovery has finished.
		mBroadcastReceiver = new BroadcastReceiver() {
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
		mParentActivity.registerReceiver(mBroadcastReceiver, filter);
	}

	/**
	 * Creates the network service.
	 */
	public void createService() {
		if (mNetworkService == null) {
			mNetworkService = new BluetoothNetworkService(mParentActivity);
			for (Handler handler : mBluetoothEventHandlers) {
				mNetworkService.addHandler(handler);
			}
		}
	}

	/**
	 * Starts the network service.
	 */
	public void startService() {
		if (mNetworkService != null) {
			mNetworkService.start(true);
		}
	}

	/**
	 * Stops the network service.
	 */
	public void stopService() {
		mParentActivity.unregisterReceiver(mBroadcastReceiver);

		if (mNetworkService != null) {
			mNetworkService.stop();
		}
	}

	/**
	 * Retrieves the network service.
	 * @return the network service
	 */
	public BluetoothNetworkService getService() {
		return mNetworkService;
	}

	/**
	 * Registers a handler to be called back on BT events.
	 * @param handler the handler to be called back
	 */
	public void registerHandler(final Handler handler) {
		mBluetoothEventHandlers.add(handler);
	}

	/**
	 * Connects this device to the specified address.
	 * @param address the address to connect to
	 * @param type the type of connection
	 */
	public void connect(final String address, final ConnectionType type) {
		// Get the BluetoothDevice object...
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
		// ...and attempt to connect to the device.
		mNetworkService.connect(device, type);
	}

	/**
	 * Starts the discover-able process.
	 * @param seconds the number of seconds to be discover-able
	 */
	public void startDiscovery(final int seconds) {
		if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, seconds);
			mParentActivity.startActivity(discoverableIntent);
		}
	}
}
