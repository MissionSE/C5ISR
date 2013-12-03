package com.missionse.bluetooth.network;

import java.io.IOException;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import com.missionse.bluetooth.network.ServiceIdentifier.ConnectionType;

/**
 * This thread runs while listening for incoming connections. It behaves like a server-side client. It runs until a
 * connection is accepted (or until cancelled).
 */
public class SocketConnectionAcceptThread extends Thread {

	private final BluetoothNetworkService mChatService;
	private final BluetoothAdapter mBluetoothAdapter;

	private BluetoothServerSocket mServerSocket;

	/**
	 * Creates a new SocketConnectionAcceptThread.
	 * @param service the parent service creating this thread
	 * @param adapter the bluetooth adapter
	 * @param type type of connection
	 */
	public SocketConnectionAcceptThread(final BluetoothNetworkService service, final BluetoothAdapter adapter,
			final ConnectionType type) {
		mChatService = service;
		mBluetoothAdapter = adapter;

		// Create a new listening server socket
		try {
			if (type == ConnectionType.SECURE) {
				mServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(
						ServiceIdentifier.getServiceName(type), ServiceIdentifier.getServiceUUID(type));
			} else {
				mServerSocket = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(
						ServiceIdentifier.getServiceName(type), ServiceIdentifier.getServiceUUID(type));
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
				socket = mServerSocket.accept();
			} catch (IOException e) {
				break;
			}
		}

		// If a connection was accepted
		if (socket != null) {
			mChatService.onIncomingConnectionSuccessful(socket, socket.getRemoteDevice());
		}
	}

	/**
	 * Ends this thread, closing the open server socket.
	 */
	public void cancel() {
		try {
			mServerSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
