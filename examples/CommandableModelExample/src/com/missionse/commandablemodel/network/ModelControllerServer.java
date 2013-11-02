package com.missionse.commandablemodel.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.AsyncTask;

import com.missionse.modelviewer.ModelViewerFragment;

public class ModelControllerServer extends AsyncTask<ModelViewerFragment, Void, String> {

	// public static final String TAG = ModelControllerServer.class.getSimpleName();

	public static final int PORT = 3456;

	public ModelControllerServer() {
	}

	@Override
	protected String doInBackground(final ModelViewerFragment... params) {
		try {
			ServerSocket serverSocket = new ServerSocket(PORT);
			ModelViewerFragment modelViewerFragment = params[0];
			ModelState modelState = new ModelState();
			while (true) {
				Socket connectingClient = serverSocket.accept();

				BufferedReader reader = new BufferedReader(new InputStreamReader(connectingClient.getInputStream()));
				final String receivedModelState = reader.readLine();

				modelState.setModelValues(receivedModelState);

				setModelState(modelViewerFragment, modelState);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void setModelState(final ModelViewerFragment fragment, final ModelState state) {
		fragment.getController().setRotation(state.get(ModelState.ROTATION_X), state.get(ModelState.ROTATION_Y),
				state.get(ModelState.ROTATION_Z));
		fragment.getController().setScale(state.get(ModelState.SCALE_X), state.get(ModelState.SCALE_Y),
				state.get(ModelState.SCALE_Z));
		fragment.getController().setPosition(state.get(ModelState.POSITION_X), state.get(ModelState.POSITION_Y),
				state.get(ModelState.POSITION_Z));
	}
}
