package com.missionse.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;

/**
 * This thread runs during a connection with a remote device. It handles all incoming and outgoing transmissions.
 */
public class ConnectedThread extends Thread {
	private final BluetoothNetworkService networkService;

	private final BluetoothSocket socket;

	private InputStream inStream;
	private OutputStream outStream;

	public ConnectedThread(final BluetoothNetworkService service, final BluetoothSocket socket) {
		networkService = service;
		this.socket = socket;

		try {
			inStream = socket.getInputStream();
			outStream = socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		byte[] buffer = new byte[1024];
		int bytes;

		// Keep listening to the InputStream while connected
		while (true) {
			try {
				bytes = inStream.read(buffer);
				networkService.onIncomingData(bytes, buffer);
			} catch (IOException e) {
				networkService.onConnectionLost();
				break;
			}
		}
	}

	public void write(final byte[] buffer) {
		try {
			outStream.write(buffer);
			networkService.onOutgoingData(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void cancel() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
