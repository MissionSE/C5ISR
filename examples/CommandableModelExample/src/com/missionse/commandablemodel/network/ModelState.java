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

	public static final int YAW = 6;
	public static final int PITCH = 7;
	public static final int ROLL = 8;

	public ModelState() {
	}

	public ModelState(final String parseableState) {
		setModelValues(parseableState);
	}

	public ModelState(final ModelController controller) {
		String parseableState = " ";
		parseableState += controller.getXPosition() + " " + controller.getYPosition() + " " + controller.getZPosition();
		parseableState += " ";
		parseableState += controller.getXScale() + " " + controller.getYScale() + " " + controller.getZScale();
		parseableState += " ";
		parseableState += controller.getYaw() + " " + controller.getPitch() + " " + controller.getRoll();

		setModelValues(parseableState);
	}

	@Override
	public String toString() {
		String parseableState = " ";

		parseableState += modelState.get(POSITION_X) + " " + modelState.get(POSITION_Y) + " " + modelState.get(POSITION_Z);
		parseableState += " ";
		parseableState += modelState.get(SCALE_X) + " " + modelState.get(SCALE_Y) + " " + modelState.get(SCALE_Z);
		parseableState += " ";
		parseableState += modelState.get(YAW) + " " + modelState.get(PITCH) + " " + modelState.get(ROLL);

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
			isValid = false;
		}

		return isValid;
	}

	public Float get(final int index) {
		return modelState.get(index);
	}
}
