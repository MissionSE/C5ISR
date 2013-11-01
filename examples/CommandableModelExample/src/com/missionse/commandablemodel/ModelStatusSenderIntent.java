package com.missionse.commandablemodel;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class ModelStatusSenderIntent extends IntentService {

	public static final String TAG = ModelStatusSenderIntent.class.getSimpleName();
	public static final String ACTION_SEND_MODEL_STATUS = "com.missionse.commandablemodel.SEND_MODEL_STATUS";
	public static final String EXTRAS_STATUS = "sendable_model_status";
	public static final String EXTRAS_HOST = "target_host";
	public static final String EXTRAS_PORT = "target_port";

	public ModelStatusSenderIntent(final String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(final Intent intent) {
		Log.d(TAG, "Got intent: " + intent.getAction());
		if (intent.getAction().equals(ACTION_SEND_MODEL_STATUS)) {
			String modelStatus = intent.getExtras().getString(EXTRAS_STATUS);
			String targetHost = intent.getExtras().getString(EXTRAS_HOST);
			int targetPort = intent.getExtras().getInt(EXTRAS_PORT);

			Log.d(TAG, "Sending: " + modelStatus);
			Socket socket = new Socket();
			try {
				socket.bind(null);
				socket.connect(new InetSocketAddress(targetHost, targetPort), 5000);

				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				out.println(modelStatus);

				Log.d(TAG, "Model Status sent successfully.");
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
