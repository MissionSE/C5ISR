package com.missionse.wifidirect.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.app.IntentService;
import android.content.Intent;

/**
 * An IntentService that sends data across a socket.
 */
public class ClientIntentService extends IntentService {

	public static final int TIMEOUT = 5000; //in ms

	public static final String ACTION_SEND_DATA = "com.missionse.wifidirect.SEND_DATA";
	public static final String EXTRAS_DATA = "data";
	public static final String EXTRAS_HOST = "target_host";
	public static final String EXTRAS_PORT = "target_port";

	/**
	 * Creates an intent service.
	 */
	public ClientIntentService() {
		super("ClientIntentService");
	}

	/**
	 * Creates an intent service with a given name.
	 * @param name the name of the intent service
	 */
	public ClientIntentService(final String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(final Intent intent) {
		if (intent.getAction().equals(ACTION_SEND_DATA)) {
			byte[] data = intent.getExtras().getByteArray(EXTRAS_DATA);
			String targetHost = intent.getExtras().getString(EXTRAS_HOST);
			int targetPort = intent.getExtras().getInt(EXTRAS_PORT);

			Socket socket = new Socket();
			try {
				socket.bind(null);
				socket.connect(new InetSocketAddress(targetHost, targetPort), TIMEOUT);

				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				out.println(data);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (socket.isConnected()) {
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
