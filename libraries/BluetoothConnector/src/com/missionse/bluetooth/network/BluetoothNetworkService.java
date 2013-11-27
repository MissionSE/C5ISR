package com.missionse.bluetooth.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;

import com.missionse.bluetooth.network.ServiceIdentifier.ConnectionType;

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

	// Current connection state enumerations
	public static final int STATE_NONE = 0;
	public static final int STATE_LISTEN = 1;
	public static final int STATE_CONNECTING = 2;
	public static final int STATE_CONNECTED = 3;

	private final BluetoothAdapter mAdapter;
	private final List<Handler> mHandlers = Collections.synchronizedList(new ArrayList<Handler>());

	private SocketConnectionAcceptThread mSecureAcceptThread;
	private SocketConnectionAcceptThread mInsecureAcceptThread;

	private ConnectThread mConnectThread;
	private ConnectedThread mConnectedThread;

	private int mServiceState;

	private boolean mSecureMode = true;

	/**
	 * Creates a new BluetoothNetworkService.
	 * @param context the parent context in which this network service resides
	 */
	public BluetoothNetworkService(final Context context) {
		mAdapter = BluetoothAdapter.getDefaultAdapter();

		setState(STATE_NONE);
	}

	/**
	 * Adds a message handler to be sent messages on various events.
	 * @param handler a handler to add for messages
	 */
	public void addHandler(final Handler handler) {
		handler.obtainMessage(MESSAGE_STATE_CHANGE, mServiceState, -1).sendToTarget();
		mHandlers.add(handler);
	}

	/**
	 * Removes a message handler from message notification.
	 * @param handler the handler to remove
	 */
	public void removeHandler(final Handler handler) {
		mHandlers.remove(handler);
	}

	private List<Handler> getHandlers() {
		return mHandlers;
	}

	private synchronized void setState(final int state) {
		mServiceState = state;

		synchronized (mHandlers) {
			for (Handler handler : getHandlers()) {
				handler.obtainMessage(MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
			}
		}
	}

	/**
	 * Starts this service.
	 * @param secure Whether or not we should start a secure or insecure connection thread
	 */
	public synchronized void start(final boolean secure) {
		mSecureMode = secure;
		if (mServiceState == STATE_NONE) {
			restart();
		}
	}

	private synchronized void restart() {
		cancelCurrentConnectionThreads();

		// Start the thread to listen on a BluetoothServerSocket.
		if (mSecureMode) {
			ServiceIdentifier.validateSecureService();
			if (mSecureAcceptThread == null) {
				mSecureAcceptThread = new SocketConnectionAcceptThread(this, mAdapter, ConnectionType.SECURE);
				mSecureAcceptThread.start();
			}
		} else {
			ServiceIdentifier.validateInsecureService();
			if (mInsecureAcceptThread == null) {
				mInsecureAcceptThread = new SocketConnectionAcceptThread(this, mAdapter, ConnectionType.INSECURE);
				mInsecureAcceptThread.start();
			}
		}

		setState(STATE_LISTEN);
	}

	private void cancelCurrentConnectionThreads() {
		// Cancel any thread attempting to make a connection.
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		// Cancel any thread currently running a connection.
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
	}

	/**
	 * Cancels current connections, and starts a thread to connect to a given device.
	 * @param device the device to connect to
	 * @param type the type of connection
	 */
	public synchronized void connect(final BluetoothDevice device, final ConnectionType type) {
		cancelCurrentConnectionThreads();

		// Cancel discovery, and start the thread to connect with the given device.
		mAdapter.cancelDiscovery();

		mConnectThread = new ConnectThread(this, device, type);

		mConnectThread.start();
		setState(STATE_CONNECTING);
	}

	/**
	 * Stops this service, canceling all current connections.
	 */
	public synchronized void stop() {
		cancelCurrentConnectionThreads();
		cancelCurrentAcceptThreads();
		setState(STATE_NONE);
	}

	private void cancelCurrentAcceptThreads() {
		if (mSecureAcceptThread != null) {
			mSecureAcceptThread.cancel();
			mSecureAcceptThread = null;
		}

		if (mInsecureAcceptThread != null) {
			mInsecureAcceptThread.cancel();
			mInsecureAcceptThread = null;
		}
	}

	/**
	 * Writes data out via the connected thread.
	 * @param out the data to write out
	 * @return whether or not the write was successful
	 */
	public boolean write(final byte[] out) {
		ConnectedThread thread;
		// Synchronize a copy of the ConnectedThread.
		synchronized (this) {
			if (mServiceState != STATE_CONNECTED) {
				return false;
			}
			thread = mConnectedThread;
		}
		// Perform the write unsynchronized.
		synchronized (this) {
			thread.write(out);
		}

		return true;
	}

	// ACCEPT THREAD HOOKS

	protected synchronized void onIncomingConnectionSuccessful(final BluetoothSocket socket,
			final BluetoothDevice device) {
		if (mServiceState == STATE_CONNECTING || mServiceState == STATE_LISTEN) {
			onConnectionSuccessful(socket, device);
		} else {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// CONNECT THEAD HOOKS

	protected synchronized void onConnectionSuccessful(final BluetoothSocket socket, final BluetoothDevice device) {
		// Cancel the accept thread because we only want to connect to one device.
		cancelCurrentAcceptThreads();

		// Cancel any thread currently running a connection.
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		// Start the thread to manage the connection and send/receive data.
		mConnectedThread = new ConnectedThread(this, socket);
		mConnectedThread.start();

		synchronized (mHandlers) {
			for (Handler handler : getHandlers()) {
				handler.obtainMessage(MESSAGE_DEVICE_NAME, device.getName()).sendToTarget();
			}
		}

		setState(STATE_CONNECTED);
	}

	protected void onConnectionFailed() {
		synchronized (mHandlers) {
			for (Handler handler : getHandlers()) {
				handler.obtainMessage(MESSAGE_TOAST, "Connection failed.").sendToTarget();
			}
		}

		restart();
	}

	// CONNECTED THREAD HOOKS

	protected void onIncomingData(final int bytes, final byte[] buffer) {
		synchronized (mHandlers) {
			for (Handler handler : getHandlers()) {
				handler.obtainMessage(MESSAGE_INCOMING_DATA, bytes, -1, buffer).sendToTarget();
			}
		}
	}

	protected void onOutgoingData(final byte[] buffer) {
		synchronized (mHandlers) {
			for (Handler handler : getHandlers()) {
				handler.obtainMessage(MESSAGE_OUTGOING_DATA, buffer).sendToTarget();
			}
		}
	}

	protected void onConnectionLost() {
		synchronized (mHandlers) {
			for (Handler handler : getHandlers()) {
				handler.obtainMessage(MESSAGE_TOAST, "Connection lost.").sendToTarget();
			}
		}

		restart();
	}
}
