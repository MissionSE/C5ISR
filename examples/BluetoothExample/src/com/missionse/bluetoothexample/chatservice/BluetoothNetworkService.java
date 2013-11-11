package com.missionse.bluetoothexample.chatservice;

import java.io.IOException;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * This class manages all threads for: listening for incoming connections (via SocketConnectionAcceptThread), connecting
 * to a remote (given service UUIDs, via ConnectThread), and sending and receiving data over the socket (via
 * ConnectedThread)
 * 
 * To communicate back to the encompassing application, it sends out messages as necessary back to the "handler", who is
 * notified of various state changes and other actions.
 */
public class BluetoothNetworkService {

	// Message types sent from the BluetoothNetworkService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_INCOMING_DATA = 2;
	public static final int MESSAGE_OUTGOING_DATA = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	// Key names for message packing
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	// Current connection state enumerations
	public static final int STATE_NONE = 0;
	public static final int STATE_LISTEN = 1;
	public static final int STATE_CONNECTING = 2;
	public static final int STATE_CONNECTED = 3;

	private final BluetoothAdapter adapter;
	private final Handler handler;

	private SocketConnectionAcceptThread secureAcceptThread;
	private SocketConnectionAcceptThread insecureAcceptThread;

	private ConnectThread connectThread;
	private ConnectedThread connectedThread;

	private int serviceState;

	public BluetoothNetworkService(final Context context, final Handler handler) {
		adapter = BluetoothAdapter.getDefaultAdapter();
		serviceState = STATE_NONE;
		this.handler = handler;
	}

	private synchronized void setState(final int state) {
		serviceState = state;

		// Give the new state to the Handler so the UI Activity can update
		handler.obtainMessage(MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
	}

	//public synchronized int getState() {
	//	return serviceState;
	//}

	public synchronized void start() {
		if (serviceState == BluetoothNetworkService.STATE_NONE) {
			restart();
		}
	}

	public synchronized void restart() {
		cancelCurrentConnectionThreads();

		// Start the thread to listen on a BluetoothServerSocket
		if (secureAcceptThread == null) {
			secureAcceptThread = new SocketConnectionAcceptThread(this, adapter, true);
			secureAcceptThread.start();
		}

		if (insecureAcceptThread == null) {
			insecureAcceptThread = new SocketConnectionAcceptThread(this, adapter, false);
			insecureAcceptThread.start();
		}

		setState(STATE_LISTEN);
	}

	private void cancelCurrentConnectionThreads() {
		// Cancel any thread attempting to make a connection
		if (connectThread != null) {
			connectThread.cancel();
			connectThread = null;
		}

		// Cancel any thread currently running a connection
		if (connectedThread != null) {
			connectedThread.cancel();
			connectedThread = null;
		}
	}

	public synchronized void connect(final BluetoothDevice device, final boolean secure) {
		cancelCurrentConnectionThreads();

		// Cancel discovery, and start the thread to connect with the given device
		adapter.cancelDiscovery();

		connectThread = new ConnectThread(this, device, secure);
		connectThread.start();
		setState(STATE_CONNECTING);
	}

	/**
	 * Stop all threads
	 */
	public synchronized void stop() {
		cancelCurrentConnectionThreads();
		cancelCurrentAcceptThreads();
		setState(STATE_NONE);
	}

	private void cancelCurrentAcceptThreads() {
		if (secureAcceptThread != null) {
			secureAcceptThread.cancel();
			secureAcceptThread = null;
		}

		if (insecureAcceptThread != null) {
			insecureAcceptThread.cancel();
			insecureAcceptThread = null;
		}
	}

	public boolean write(final byte[] out) {
		// Create temporary object
		ConnectedThread thread;
		// Synchronize a copy of the ConnectedThread
		synchronized (this) {
			if (serviceState != STATE_CONNECTED) {
				return false;
			}
			thread = connectedThread;
		}
		// Perform the write unsynchronized
		thread.write(out);
		return true;
	}

	/**
	 * ACCEPT THREAD HOOKS
	 */

	public synchronized void onIncomingConnectionSuccessful(final BluetoothSocket socket, final BluetoothDevice device) {
		if (serviceState == STATE_CONNECTING || serviceState == STATE_LISTEN) {
			onConnectionSuccessful(socket, device);
		} else {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * CONNECT THEAD HOOKS
	 */

	public synchronized void onConnectionSuccessful(final BluetoothSocket socket, final BluetoothDevice device) {
		// Cancel the accept thread because we only want to connect to one device
		cancelCurrentAcceptThreads();

		// Cancel any thread currently running a connection
		if (connectedThread != null) {
			connectedThread.cancel();
			connectedThread = null;
		}

		// Start the thread to manage the connection and send/receive data
		connectedThread = new ConnectedThread(this, socket);
		connectedThread.start();

		Message msg = handler.obtainMessage(MESSAGE_DEVICE_NAME);
		Bundle bundle = new Bundle();
		bundle.putString(DEVICE_NAME, device.getName());
		msg.setData(bundle);
		handler.sendMessage(msg);

		setState(STATE_CONNECTED);
	}

	public void onConnectionFailed() {
		Message msg = handler.obtainMessage(MESSAGE_TOAST);
		Bundle bundle = new Bundle();
		bundle.putString(TOAST, "Connection failed");
		msg.setData(bundle);
		handler.sendMessage(msg);

		restart();
	}

	/**
	 * CONNECTED THREAD HOOKS
	 */

	public void onIncomingData(final int bytes, final byte[] buffer) {
		handler.obtainMessage(MESSAGE_INCOMING_DATA, bytes, -1, buffer).sendToTarget();
	}

	public void onOutgoingData(final byte[] buffer) {
		handler.obtainMessage(MESSAGE_OUTGOING_DATA, -1, -1, buffer).sendToTarget();
	}

	public void onConnectionLost() {
		// Send a failure message back to the Activity
		Message msg = handler.obtainMessage(MESSAGE_TOAST);
		Bundle bundle = new Bundle();
		bundle.putString(TOAST, "Connection lost");
		msg.setData(bundle);
		handler.sendMessage(msg);

		restart();
	}
}
