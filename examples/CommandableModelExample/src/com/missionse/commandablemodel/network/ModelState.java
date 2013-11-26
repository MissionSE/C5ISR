package com.missionse.commandablemodel.network;

import java.util.ArrayList;
import java.util.StringTokenizer;

import android.util.Log;

import com.missionse.modelviewer.ModelController;

public class ModelState {
	private static final String TAG = ModelState.class.getName();

	private ArrayList<Float> modelState;

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

	public ModelState() {
	}

	public ModelState(final String parseableState) {
		setModelValues(parseableState);
	}

	public ModelState(final ModelController controller) {
		String parseableState = "";
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

		setModelValues(parseableState);
	}

	@Override
	public String toString() {
		String parseableState = "";
		parseableState += " " + modelState.get(POSITION_X);
		parseableState += " " + modelState.get(POSITION_Y);
		parseableState += " " + modelState.get(POSITION_Z);
		parseableState += " " + modelState.get(SCALE_X);
		parseableState += " " + modelState.get(SCALE_Y);
		parseableState += " " + modelState.get(SCALE_Z);
		parseableState += " " + modelState.get(ORIENTATION_W);
		parseableState += " " + modelState.get(ORIENTATION_X);
		parseableState += " " + modelState.get(ORIENTATION_Y);
		parseableState += " " + modelState.get(ORIENTATION_Z);

		return parseableState;
	}

	public boolean setModelValues(final String modelStatus) {
		boolean isValid = true;
		modelState = new ArrayList<Float>();

		StringTokenizer tokenizer = new StringTokenizer(modelStatus);
		try {
			while (tokenizer.hasMoreTokens()) {
				modelState.add(Float.parseFloat(tokenizer.nextToken()));
			}
		} catch (NumberFormatException e) {
			Log.e(TAG, "Unable to parse string: " + modelStatus);
			Log.e(TAG, "Size of value list: " + modelState.size());
			isValid = false;
		}

		return isValid;
	}

	public Float get(final int index) {
		Float value = 0.0f;
		try {
			value = modelState.get(index);
		} catch (IndexOutOfBoundsException e) {
			Log.e(TAG, "Attempted to use an invalid index.");
		}
		return value;
	}
}
