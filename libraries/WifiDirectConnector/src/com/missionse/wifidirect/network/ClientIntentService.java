package com.missionse.wifidirect.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class ClientIntentService extends IntentService {

	public static final String TAG = ClientIntentService.class.getSimpleName();
	public static final String ACTION_SEND_DATA = "com.missionse.wifidirect.SEND_DATA";
	public static final String EXTRAS_DATA = "data";
	public static final String EXTRAS_HOST = "target_host";
	public static final String EXTRAS_PORT = "target_port";

	public ClientIntentService() {
		super("ClientIntentService");
	}

	public ClientIntentService(final String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(final Intent intent) {
		Log.e(TAG, "Got intent: " + intent.getAction());
		if (intent.getAction().equals(ACTION_SEND_DATA)) {
			byte[] data = intent.getExtras().getByteArray(EXTRAS_DATA);
			String targetHost = intent.getExtras().getString(EXTRAS_HOST);
			int targetPort = intent.getExtras().getInt(EXTRAS_PORT);

			Log.e(TAG, "Sending: " + data);
			Socket socket = new Socket();
			try {
				socket.bind(null);
				socket.connect(new InetSocketAddress(targetHost, targetPort), 5000);

				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				out.println(data);

				Log.e(TAG, "Data sent successfully.");
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
