package com.missionse.bluetoothexample.chatservice;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * This class manages all threads for connecting and maintaining a live connection with another device. It then sends
 * out messages as necessary back to the "handler", who is notified of various state changes and other actions. Finally,
 * it maintains an overall service state.
 */
public class ChatService {

	// Message types sent from the ChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_INCOMING_DATA = 2;
	public static final int MESSAGE_OUTGOING_DATA = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	// Key names for message packing
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	// Constants that indicate the current connection state
	public static final int STATE_NONE = 0;
	public static final int STATE_LISTEN = 1;
	public static final int STATE_CONNECTING = 2;
	public static final int STATE_CONNECTED = 3;

	private final BluetoothAdapter adapter;
	private final Handler handler;

	private AcceptThread secureAcceptThread;
	private AcceptThread insecureAcceptThread;

	private ConnectThread connectThread;
	private ConnectedThread connectedThread;

	private int serviceState;

	public ChatService(final Context context, final Handler handler) {
		adapter = BluetoothAdapter.getDefaultAdapter();
		serviceState = STATE_NONE;
		this.handler = handler;
	}

	/**
	 * Set the current state of the chat connection
	 * @param state An integer defining the current connection state
	 */
	private synchronized void setState(final int state) {
		serviceState = state;

		// Give the new state to the Handler so the UI Activity can update
		handler.obtainMessage(MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
	}

	/**
	 * Return the current connection state.
	 */
	public synchronized int getState() {
		return serviceState;
	}

	/**
	 * Start the chat service. Specifically start AcceptThread to begin a session in listening (server) mode. Called by
	 * the Activity onResume()
	 */
	public synchronized void start() {
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

		setState(STATE_LISTEN);

		// Start the thread to listen on a BluetoothServerSocket
		if (secureAcceptThread == null) {
			secureAcceptThread = new AcceptThread(this, adapter, true);
			secureAcceptThread.start();
		}
		if (insecureAcceptThread == null) {
			insecureAcceptThread = new AcceptThread(this, adapter, false);
			insecureAcceptThread.start();
		}
	}

	/**
	 * Start the ConnectThread to initiate a connection to a remote device.
	 * @param device The BluetoothDevice to connect
	 * @param secure Socket Security type - Secure (true) , Insecure (false)
	 */
	public synchronized void connect(final BluetoothDevice device, final boolean secure) {
		// Cancel any thread attempting to make a connection
		if (serviceState == STATE_CONNECTING) {
			if (connectThread != null) {
				connectThread.cancel();
				connectThread = null;
			}
		}

		// Cancel any thread currently running a connection
		if (connectedThread != null) {
			connectedThread.cancel();
			connectedThread = null;
		}

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
		if (connectThread != null) {
			connectThread.cancel();
			connectThread = null;
		}

		if (connectedThread != null) {
			connectedThread.cancel();
			connectedThread = null;
		}

		if (secureAcceptThread != null) {
			secureAcceptThread.cancel();
			secureAcceptThread = null;
		}

		if (insecureAcceptThread != null) {
			insecureAcceptThread.cancel();
			insecureAcceptThread = null;
		}
		setState(STATE_NONE);
	}

	/**
	 * Write to the ConnectedThread in an unsynchronized manner
	 * @param out The bytes to write
	 * @see ConnectedThread#write(byte[])
	 */
	public void write(final byte[] out) {
		// Create temporary object
		ConnectedThread r;
		// Synchronize a copy of the ConnectedThread
		synchronized (this) {
			if (serviceState != STATE_CONNECTED) {
				return;
			}
			r = connectedThread;
		}
		// Perform the write unsynchronized
		r.write(out);
	}

	/**
	 * CONNECT THREAD HOOKS
	 */

	/**
	 * Start the ConnectedThread to begin managing a Bluetooth connection
	 * @param socket The BluetoothSocket on which the connection was made
	 * @param device The BluetoothDevice that has been connected
	 */
	public synchronized void onConnectionSuccessful(final BluetoothSocket socket, final BluetoothDevice device) {

		// Cancel any thread currently running a connection
		if (connectedThread != null) {
			connectedThread.cancel();
			connectedThread = null;
		}

		// Cancel the accept thread because we only want to connect to one device
		if (secureAcceptThread != null) {
			secureAcceptThread.cancel();
			secureAcceptThread = null;
		}
		if (insecureAcceptThread != null) {
			insecureAcceptThread.cancel();
			insecureAcceptThread = null;
		}

		// Start the thread to manage the connection and perform transmissions
		connectedThread = new ConnectedThread(this, socket);
		connectedThread.start();

		// Send the name of the connected device back to the UI Activity
		Message msg = handler.obtainMessage(MESSAGE_DEVICE_NAME);
		Bundle bundle = new Bundle();
		bundle.putString(DEVICE_NAME, device.getName());
		msg.setData(bundle);
		handler.sendMessage(msg);

		setState(STATE_CONNECTED);
	}

	public void onConnectionFailed() {
		// Send a failure message back to the Activity
		Message msg = handler.obtainMessage(MESSAGE_TOAST);
		Bundle bundle = new Bundle();
		bundle.putString(TOAST, "Connection failed");
		msg.setData(bundle);
		handler.sendMessage(msg);

		// Restart the service to restart listening mode
		start();
	}

	/**
	 * CONNECTED THREAD HOOKS
	 */

	public void onIncomingData(final int bytes, final byte[] buffer) {
		//Send message to handler, notifying of incoming message.
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

		// Restart the service to restart listening mode
		start();
	}
}
