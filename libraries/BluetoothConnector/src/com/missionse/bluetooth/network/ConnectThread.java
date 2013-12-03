package com.missionse.bluetooth.network;

import java.io.IOException;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.missionse.bluetooth.network.ServiceIdentifier.ConnectionType;
import com.missionse.bluetooth.network.ServiceIdentifier.ServiceNotIdentifiedException;

/**
 * This thread runs while attempting to make an outgoing connection with a device. It runs straight through; the
 * connection either succeeds or fails.
 */
public class ConnectThread extends Thread {

	private BluetoothNetworkService mNetworkService;

	private BluetoothSocket mSocket;
	private final BluetoothDevice mDevice;

	/**
	 * Creates a new ConnectThread.
	 * @param service the service to callback on connect events
	 * @param device the device to which to connect
	 * @param type the type of connection
	 */
	public ConnectThread(final BluetoothNetworkService service, final BluetoothDevice device, final ConnectionType type) {
		mNetworkService = service;
		mDevice = device;

		try {
			if (type == ConnectionType.SECURE) {
				mSocket = device.createRfcommSocketToServiceRecord(ServiceIdentifier.getServiceUUID(type));
			} else {
				mSocket = device.createInsecureRfcommSocketToServiceRecord(ServiceIdentifier.getServiceUUID(type));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			// This is a blocking call and will only return on a successful connection or an exception.
			mSocket.connect();
		} catch (IOException e) {
			try {
				mSocket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				mNetworkService.onConnectionFailed();
			} catch (ServiceNotIdentifiedException e2) {
				e2.printStackTrace();
			}
			return;
		}

		mNetworkService.onConnectionSuccessful(mSocket, mDevice);
	}

	/**
	 * Cancels this thread, closing the client socket.
	 */
	public void cancel() {
		try {
			mSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
