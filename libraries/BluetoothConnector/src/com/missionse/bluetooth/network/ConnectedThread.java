package com.missionse.bluetooth.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;

import com.missionse.bluetooth.network.ServiceIdentifier.ServiceNotIdentifiedException;

/**
 * This thread runs during a connection with a remote device. It handles all incoming and outgoing transmissions.
 */
public class ConnectedThread extends Thread {
	private static final int BUFFER_SIZE = 1024;

	private final BluetoothNetworkService mNetworkService;
	private final BluetoothSocket mSocket;

	private InputStream mInputStream;
	private OutputStream mOutputStream;

	/**
	 * Creates a new ConnectedThread.
	 * @param service the service to call back on incoming/outgoing data
	 * @param socket the socket over which we are connected
	 */
	public ConnectedThread(final BluetoothNetworkService service, final BluetoothSocket socket) {
		mNetworkService = service;
		mSocket = socket;

		try {
			mInputStream = socket.getInputStream();
			mOutputStream = socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		byte[] buffer = new byte[BUFFER_SIZE];
		int bytes;

		// Keep listening to the InputStream while connected.
		while (true) {
			try {
				bytes = mInputStream.read(buffer);
				mNetworkService.onIncomingData(bytes, buffer);
			} catch (IOException e) {
				try {
					mNetworkService.onConnectionLost();
				} catch (ServiceNotIdentifiedException e1) {
					e1.printStackTrace();
				}
				break;
			}
		}
	}

	/**
	 * Writes date over the network connection.
	 * @param buffer the data to write
	 */
	public void write(final byte[] buffer) {
		try {
			mOutputStream.write(buffer);
			mOutputStream.flush();
			mNetworkService.onOutgoingData(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
