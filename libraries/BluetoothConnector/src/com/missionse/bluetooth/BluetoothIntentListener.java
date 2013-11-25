package com.missionse.bluetooth;

import android.bluetooth.BluetoothDevice;

public interface BluetoothIntentListener {
	public void onDeviceFound(BluetoothDevice device);

	public void onDiscoveryFinished();
}
