package com.missionse.bluetoothexample.chatservice;

import java.io.IOException;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

/**
 * This thread runs while attempting to make an outgoing connection with a device. It runs straight through; the
 * connection either succeeds or fails.
 */
public class ConnectThread extends Thread {

	private BluetoothNetworkService networkService;

	private BluetoothSocket socket;
	private final BluetoothDevice device;

	public ConnectThread(final BluetoothNetworkService service, final BluetoothDevice device, final boolean secure) {
		networkService = service;
		this.device = device;

		try {
			if (secure) {
				socket = device.createRfcommSocketToServiceRecord(ServiceIdentifier.MY_UUID_SECURE);
			} else {
				socket = device.createInsecureRfcommSocketToServiceRecord(ServiceIdentifier.MY_UUID_INSECURE);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			// This is a blocking call and will only return on a
			// successful connection or an exception
			socket.connect();
		} catch (IOException e) {
			try {
				socket.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			networkService.onConnectionFailed();
			return;
		}

		// Start the connected thread
		networkService.onConnectionSuccessful(socket, device);
	}

	public void cancel() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
