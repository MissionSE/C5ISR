package com.missionse.bluetoothexample.chatservice;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;

/**
 * This thread runs during a connection with a remote device. It handles all incoming and outgoing transmissions.
 */
public class ConnectedThread extends Thread {
	private final ChatService chatService;

	private final BluetoothSocket socket;

	private InputStream inStream;
	private OutputStream outStream;

	public ConnectedThread(final ChatService service, final BluetoothSocket socket) {
		chatService = service;
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
				// Read from the InputStream
				bytes = inStream.read(buffer);
				chatService.onIncomingData(bytes, buffer);
			} catch (IOException e) {
				chatService.onConnectionLost();
				break;
			}
		}
	}

	public void write(final byte[] buffer) {
		try {
			outStream.write(buffer);
			// Share the sent message back to the UI Activity
			chatService.onOutgoingData(buffer);
		} catch (IOException e) {
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
