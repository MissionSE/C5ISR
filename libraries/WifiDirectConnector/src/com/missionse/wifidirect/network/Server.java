package com.missionse.wifidirect.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import android.os.AsyncTask;

import com.missionse.wifidirect.listener.IncomingDataListener;

public class Server extends AsyncTask<List<IncomingDataListener>, Void, Void> {

	public static final int PORT = 3456;

	public Server() {
	}

	@Override
	protected Void doInBackground(final List<IncomingDataListener>... listeners) {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(PORT);
			while (true) {
				Socket connectingClient = serverSocket.accept();

				ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

				int bufferSize = 1024;
				byte[] buffer = new byte[bufferSize];

				int len = 0;
				while ((len = connectingClient.getInputStream().read(buffer)) != -1) {
					byteBuffer.write(buffer, 0, len);
				}

				for (IncomingDataListener listener : listeners[0]) {
					listener.processReceivedData(byteBuffer.toByteArray());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (serverSocket != null) {
					serverSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
