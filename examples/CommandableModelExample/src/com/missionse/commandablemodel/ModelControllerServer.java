package com.missionse.commandablemodel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.AsyncTask;
import android.util.Log;

import com.missionse.modelviewer.ModelController;

public class ModelControllerServer extends AsyncTask<ModelController, Void, String> {

	public static final String TAG = ModelControllerServer.class.getSimpleName();
	public static final int PORT = 3456;
	private final ModelStatus status;

	public ModelControllerServer(final ModelStatus modelStatus) {
		status = modelStatus;
	}

	@Override
	protected String doInBackground(final ModelController... params) {
		try {
			Log.d(TAG, "Opening a socket on port: " + PORT);
			ServerSocket serverSocket = new ServerSocket(PORT);
			while (true) {
				Log.d(TAG, "Waiting for model status.");
				Socket connectingClient = serverSocket.accept();

				BufferedReader reader = new BufferedReader(new InputStreamReader(connectingClient.getInputStream()));
				final String modelStatus = reader.readLine();
				Log.d(TAG, "Received status: " + modelStatus);

				status.setModelStatus(modelStatus);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
