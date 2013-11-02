package com.missionse.commandablemodel.network;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.missionse.modelviewer.ModelController;

public class ModelState {

	private ArrayList<Float> modelState;

	public static final int POSITION_X = 0;
	public static final int POSITION_Y = 1;
	public static final int POSITION_Z = 2;

	public static final int SCALE_X = 3;
	public static final int SCALE_Y = 4;
	public static final int SCALE_Z = 5;

	public static final int ROTATION_X = 6;
	public static final int ROTATION_Y = 7;
	public static final int ROTATION_Z = 8;

	public ModelState() {
	}

	public ModelState(final String parseableState) {
		setModelValues(parseableState);
	}

	public ModelState(final ModelController controller) {
		String parseableState = "";
		parseableState += controller.getXPosition() + " " + controller.getYPosition() + " " + controller.getZPosition();
		parseableState += " ";
		parseableState += controller.getXScale() + " " + controller.getYScale() + " " + controller.getZScale();
		parseableState += " ";
		parseableState += controller.getXRotation() + " " + controller.getYRotation() + " " + controller.getZRotation();

		setModelValues(parseableState);
	}

	@Override
	public String toString() {
		String parseableState = "";

		parseableState += modelState.get(POSITION_X) + " " + modelState.get(POSITION_Y) + " "
				+ modelState.get(POSITION_Z);
		parseableState += " ";
		parseableState += modelState.get(SCALE_X) + " " + modelState.get(SCALE_Y) + " " + modelState.get(SCALE_Z);
		parseableState += " ";
		parseableState += modelState.get(ROTATION_X) + " " + modelState.get(ROTATION_Y) + " "
				+ modelState.get(ROTATION_Z);

		return parseableState;
	}

	public void setModelValues(final String modelStatus) {
		modelState = new ArrayList<Float>();

		StringTokenizer tokenizer = new StringTokenizer(modelStatus);
		while (tokenizer.hasMoreTokens()) {
			modelState.add(Float.parseFloat(tokenizer.nextToken()));
		}
	}

	public Float get(final int index) {
		return modelState.get(index);
	}
}
