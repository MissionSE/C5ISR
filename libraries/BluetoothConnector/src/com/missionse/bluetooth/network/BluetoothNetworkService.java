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

import com.missionse.bluetooth.network.ServiceIdentifier.ServiceNotIdentifiedException;

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

	private final BluetoothAdapter adapter;
	private final List<Handler> handlers = Collections.synchronizedList(new ArrayList<Handler>());

	private SocketConnectionAcceptThread secureAcceptThread;
	private SocketConnectionAcceptThread insecureAcceptThread;

	private ConnectThread connectThread;
	private ConnectedThread connectedThread;

	private int serviceState;

	private boolean secureMode = true;

	public BluetoothNetworkService(final Context context) {
		adapter = BluetoothAdapter.getDefaultAdapter();

		setState(STATE_NONE);
	}

	public void addHandler(final Handler handler) {
		handler.obtainMessage(MESSAGE_STATE_CHANGE, serviceState, -1).sendToTarget();
		handlers.add(handler);
	}

	public void removeHandler(final Handler handler) {
		handlers.remove(handler);
	}

	private List<Handler> getHandlers() {
		return handlers;
	}

	private synchronized void setState(final int state) {
		serviceState = state;

		synchronized (handlers) {
			for (Handler handler : getHandlers()) {
				handler.obtainMessage(MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
			}
		}
	}

	public synchronized void start(final boolean secure) throws ServiceNotIdentifiedException {
		secureMode = secure;
		if (serviceState == STATE_NONE) {
			restart();
		}
	}

	private synchronized void restart() throws ServiceNotIdentifiedException {
		cancelCurrentConnectionThreads();

		// Start the thread to listen on a BluetoothServerSocket.
		if (secureMode) {
			ServiceIdentifier.validateSecureService();
			if (secureAcceptThread == null) {
				secureAcceptThread = new SocketConnectionAcceptThread(this, adapter, true);
				secureAcceptThread.start();
			}
		} else {
			ServiceIdentifier.validateInsecureService();
			if (insecureAcceptThread == null) {
				insecureAcceptThread = new SocketConnectionAcceptThread(this, adapter, false);
				insecureAcceptThread.start();
			}
		}

		setState(STATE_LISTEN);
	}

	private void cancelCurrentConnectionThreads() {
		// Cancel any thread attempting to make a connection.
		if (connectThread != null) {
			connectThread.cancel();
			connectThread = null;
		}

		// Cancel any thread currently running a connection.
		if (connectedThread != null) {
			connectedThread.cancel();
			connectedThread = null;
		}
	}

	public synchronized void connect(final BluetoothDevice device, final boolean secure) {
		cancelCurrentConnectionThreads();

		// Cancel discovery, and start the thread to connect with the given device.
		adapter.cancelDiscovery();

		connectThread = new ConnectThread(this, device, secure);
		connectThread.start();
		setState(STATE_CONNECTING);
	}

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
		ConnectedThread thread;
		// Synchronize a copy of the ConnectedThread.
		synchronized (this) {
			if (serviceState != STATE_CONNECTED) {
				return false;
			}
			thread = connectedThread;
		}
		// Perform the write unsynchronized.
		thread.write(out);
		return true;
	}

	/**
	 * ACCEPT THREAD HOOKS
	 */

	protected synchronized void onIncomingConnectionSuccessful(final BluetoothSocket socket,
			final BluetoothDevice device) {
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

	protected synchronized void onConnectionSuccessful(final BluetoothSocket socket, final BluetoothDevice device) {
		// Cancel the accept thread because we only want to connect to one device.
		cancelCurrentAcceptThreads();

		// Cancel any thread currently running a connection.
		if (connectedThread != null) {
			connectedThread.cancel();
			connectedThread = null;
		}

		// Start the thread to manage the connection and send/receive data.
		connectedThread = new ConnectedThread(this, socket);
		connectedThread.start();

		synchronized (handlers) {
			for (Handler handler : getHandlers()) {
				handler.obtainMessage(MESSAGE_DEVICE_NAME, device.getName()).sendToTarget();;
			}
		}

		setState(STATE_CONNECTED);
	}

	protected void onConnectionFailed() throws ServiceNotIdentifiedException {
		synchronized (handlers) {
			for (Handler handler : getHandlers()) {
				handler.obtainMessage(MESSAGE_TOAST, "Connection failed.").sendToTarget();
			}
		}

		restart();
	}

	/**
	 * CONNECTED THREAD HOOKS
	 */

	protected void onIncomingData(final int bytes, final byte[] buffer) {
		synchronized (handlers) {
			for (Handler handler : getHandlers()) {
				handler.obtainMessage(MESSAGE_INCOMING_DATA, bytes, -1, buffer).sendToTarget();
			}
		}
	}

	protected void onOutgoingData(final byte[] buffer) {
		synchronized (handlers) {
			for (Handler handler : getHandlers()) {
				handler.obtainMessage(MESSAGE_OUTGOING_DATA, buffer).sendToTarget();
			}
		}
	}

	protected void onConnectionLost() throws ServiceNotIdentifiedException {
		synchronized (handlers) {
			for (Handler handler : getHandlers()) {
				handler.obtainMessage(MESSAGE_TOAST, "Connection lost.").sendToTarget();
			}
		}

		restart();
	}
}
