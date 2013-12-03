package com.missionse.commandablemodel.network;

import java.util.ArrayList;
import java.util.StringTokenizer;

import android.util.Log;

import com.missionse.modelviewer.ModelController;

/**
 * Represents the current state (position, scale, orientation) of a model.
 */
public class ModelState {
	private static final String TAG = ModelState.class.getName();

	private ArrayList<Float> mModelState;

	public static final int POSITION_X = 0;
	public static final int POSITION_Y = 1;
	public static final int POSITION_Z = 2;

	public static final int SCALE_X = 3;
	public static final int SCALE_Y = 4;
	public static final int SCALE_Z = 5;

	public static final int ORIENTATION_W = 6;
	public static final int ORIENTATION_X = 7;
	public static final int ORIENTATION_Y = 8;
	public static final int ORIENTATION_Z = 9;
	private static final int MODEL_SIZE = ORIENTATION_Z + 1;

	/**
	 * Creates an empty ModelState with default values.
	 */
	public ModelState() {
	}

	/**
	 * Creates a new ModelState given an initial state.
	 * @param parseableState the state to parse, to set initial values
	 */
	public ModelState(final String parseableState) {
		setModelValues(parseableState);
	}

	/**
	 * Creates a new ModelState given a controller that holds the actual model state.
	 * @param controller the controller from which to retrieve data
	 */
	public ModelState(final ModelController controller) {
		String parseableState = " B ";
		parseableState += " " + controller.getXPosition();
		parseableState += " " + controller.getYPosition();
		parseableState += " " + controller.getZPosition();
		parseableState += " " + controller.getXScale();
		parseableState += " " + controller.getYScale();
		parseableState += " " + controller.getZScale();
		parseableState += " " + controller.getWOrientation();
		parseableState += " " + controller.getXOrientation();
		parseableState += " " + controller.getYOrientation();
		parseableState += " " + controller.getZOrientation();
		parseableState += " E ";

		setModelValues(parseableState);
	}

	@Override
	public String toString() {
		String parseableState = " B ";
		parseableState += " " + mModelState.get(POSITION_X);
		parseableState += " " + mModelState.get(POSITION_Y);
		parseableState += " " + mModelState.get(POSITION_Z);
		parseableState += " " + mModelState.get(SCALE_X);
		parseableState += " " + mModelState.get(SCALE_Y);
		parseableState += " " + mModelState.get(SCALE_Z);
		parseableState += " " + mModelState.get(ORIENTATION_W);
		parseableState += " " + mModelState.get(ORIENTATION_X);
		parseableState += " " + mModelState.get(ORIENTATION_Y);
		parseableState += " " + mModelState.get(ORIENTATION_Z);
		parseableState += " E ";

		return parseableState;
	}

	/**
	 * Sets the ModelState values given a parse-able string.
	 * @param modelStatus the string to parse
	 * @return whether or not the string passed in was valid for values
	 */
	public boolean setModelValues(final String modelStatus) {
		mModelState = new ArrayList<Float>();

		boolean isValid = parseMessage(modelStatus);
		if (isValid && mModelState.size() < MODEL_SIZE) {
			isValid = false;
		}

		if (!isValid) {
			Log.e(TAG, "Message invalid: " + modelStatus);
		}

		return isValid;
	}

	private boolean parseMessage(final String incomingMessage) {

		boolean messageBegan = false;
		boolean messageEnded = false;
		boolean isValid = true;

		StringTokenizer tokenizer = new StringTokenizer(incomingMessage);
		while (tokenizer.hasMoreTokens() && !messageEnded) {
			String token = tokenizer.nextToken();
			if (!messageBegan) {
				if (token.equals("B")) {
					messageBegan = true;
					mModelState = new ArrayList<Float>();
				}
			} else if (token.equals("E")) {
				if (mModelState.size() == MODEL_SIZE) {
					messageEnded = true;
				} else {
					Log.e(TAG, "Message processed with invalid size.");
					messageBegan = false;
				}
			} else if (messageBegan && !messageEnded) {
				try {
					mModelState.add(Float.valueOf(token));
				} catch (NumberFormatException e) {
					Log.e(TAG, "Got an unexpected value: " + token);
					messageBegan = false;
				}
			}
		}

		if (!messageBegan || !messageEnded || mModelState.size() != MODEL_SIZE) {
			isValid = false;
			mModelState = new ArrayList<Float>();
		}

		return isValid;
	}

	/**
	 * Retrieves the value at a given index of the ModelState.
	 * @param index the index to retrieve
	 * @return the value at that index
	 */
	public Float get(final int index) {
		Float value = 0.0f;
		try {
			value = mModelState.get(index);
		} catch (IndexOutOfBoundsException e) {
			Log.e(TAG, "Attempted to use an invalid index.");
		}
		return value;
	}
}
