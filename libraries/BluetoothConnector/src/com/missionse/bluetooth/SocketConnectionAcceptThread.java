package com.missionse.bluetooth;

import java.io.IOException;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

/**
 * This thread runs while listening for incoming connections. It behaves like a server-side client. It runs until a
 * connection is accepted (or until cancelled).
 */
public class SocketConnectionAcceptThread extends Thread {

	private final BluetoothNetworkService chatService;
	private final BluetoothAdapter bluetoothAdapter;

	private BluetoothServerSocket serverSocket;

	public SocketConnectionAcceptThread(final BluetoothNetworkService service, final BluetoothAdapter adapter,
			final boolean secure) {
		chatService = service;
		bluetoothAdapter = adapter;

		// Create a new listening server socket
		try {
			if (secure) {
				serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(ServiceIdentifier.NAME_SECURE,
						ServiceIdentifier.UUID_SECURE);
			} else {
				serverSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(
						ServiceIdentifier.NAME_INSECURE, ServiceIdentifier.UUID_INSECURE);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		BluetoothSocket socket = null;

		// Listen to the server socket if we're not connected
		while (socket == null) {
			try {
				// This is a blocking call and will only return on a
				// successful connection or an exception
				socket = serverSocket.accept();
			} catch (IOException e) {
				break;
			}
		}

		// If a connection was accepted
		if (socket != null) {
			chatService.onIncomingConnectionSuccessful(socket, socket.getRemoteDevice());
		}
	}

	public void cancel() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			// No reason to print, as the thread was simply cancelled.
		}
	}
}
