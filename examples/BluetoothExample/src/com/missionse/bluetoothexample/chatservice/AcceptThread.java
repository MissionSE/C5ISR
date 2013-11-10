package com.missionse.bluetoothexample.chatservice;

import java.io.IOException;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

/**
 * This thread runs while listening for incoming connections. It behaves like a server-side client. It runs until a
 * connection is accepted (or until cancelled).
 */
public class AcceptThread extends Thread {

	private final ChatService chatService;
	private final BluetoothAdapter bluetoothAdapter;

	private BluetoothServerSocket serverSocket;

	public AcceptThread(final ChatService service, final BluetoothAdapter adapter, final boolean secure) {
		chatService = service;
		bluetoothAdapter = adapter;

		// Create a new listening server socket
		try {
			if (secure) {
				serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(ServiceIdentifier.NAME_SECURE,
						ServiceIdentifier.MY_UUID_SECURE);
			} else {
				serverSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(
						ServiceIdentifier.NAME_INSECURE, ServiceIdentifier.MY_UUID_INSECURE);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		BluetoothSocket socket = null;

		// Listen to the server socket if we're not connected
		while (chatService.getState() != ChatService.STATE_CONNECTED) {
			try {
				// This is a blocking call and will only return on a
				// successful connection or an exception
				socket = serverSocket.accept();
			} catch (IOException e) {
				break;
			}

			// If a connection was accepted
			if (socket != null) {
				switch (chatService.getState()) {
					case ChatService.STATE_LISTEN:
					case ChatService.STATE_CONNECTING:
						// Situation normal. Start the connected thread.
						chatService.onConnectionSuccessful(socket, socket.getRemoteDevice());
						break;
					case ChatService.STATE_NONE:
					case ChatService.STATE_CONNECTED:
						// Either not ready or already connected. Terminate new socket.
						try {
							socket.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						break;
				}
			}
		}
	}

	public void cancel() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
