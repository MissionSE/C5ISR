package com.missionse.componentexample.wifidirect;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class WifiDirectChatService extends IntentService {

	public static final String TAG = WifiDirectChatService.class.getSimpleName();
	
	public static final String ACTION_SEND_TEXT = "com.missionse.componentexample.wifidirect.SEND_TEXT";
	public static final String EXTRAS_TEXT = "sendable_text";
	public static final String EXTRAS_HOST = "target_host";
	public static final String EXTRAS_PORT = "target_port";
	
	public WifiDirectChatService(String name) {
		super(name);
	}
	
	public WifiDirectChatService() {
		super("WifiDirectChatService");
	}

	@Override
	protected void onHandleIntent(final Intent intent) {
		Log.e(TAG, "got intent [" + intent.getAction() + "]");
		if (intent.getAction().equals(ACTION_SEND_TEXT)) {
			String sendableText = intent.getExtras().getString(EXTRAS_TEXT);
			String targetHost = intent.getExtras().getString(EXTRAS_HOST);
			int targetPort = intent.getExtras().getInt(EXTRAS_PORT);
			
			Log.e(TAG, "got intent to send [" + sendableText + "]");
			
			Socket socket = new Socket();
			try {
				socket.bind(null);
				socket.connect(new InetSocketAddress(targetHost, targetPort), 5000);
				
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				out.println(sendableText);
				
				Log.e(TAG, "wrote successfully");
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally {
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
