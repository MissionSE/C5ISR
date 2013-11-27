package com.missionse.bluetooth;

import android.bluetooth.BluetoothDevice;

/**
 * Provides callbacks for changes in Bluetooth state from given system intents.
 */
public interface BluetoothIntentListener {

	/**
	 * Called when a new BluetoothDevice is found (does not include paired devices).
	 * @param device the found device
	 */
	void onDeviceFound(BluetoothDevice device);

	/**
	 * Called when discovery has finished.
	 */
	void onDiscoveryFinished();
}
