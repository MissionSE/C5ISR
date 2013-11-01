package com.missionse.commandablemodel;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.missionse.modelviewer.ModelController;

public class ModelStatus {
	private final ModelController controller;
	private static final int POSITION_X = 0;
	private static final int POSITION_Y = 1;
	private static final int POSITION_Z = 2;

	private static final int SCALE_X = 3;
	private static final int SCALE_Y = 4;
	private static final int SCALE_Z = 5;

	private static final int ROTATION_X = 6;
	private static final int ROTATION_Y = 7;
	private static final int ROTATION_Z = 8;

	public ModelStatus(final ModelController modelController) {
		controller = modelController;
	}

	public String toString() {
		String modelStatus = "";
		modelStatus += controller.getXPosition() + " " + controller.getYPosition() + " " + controller.getZPosition();
		modelStatus += " ";
		modelStatus += controller.getXScale() + " " + controller.getYScale() + " " + controller.getZScale();
		modelStatus += " ";
		modelStatus += controller.getXRotation() + " " + controller.getYRotation() + " " + controller.getZRotation();

		return modelStatus;
	}

	public void setModelStatus(final String modelStatus) {
		ArrayList<Float> modelValues = getModelValues(modelStatus);
		controller.setRotation(modelValues.get(ROTATION_X), modelValues.get(ROTATION_Y), modelValues.get(ROTATION_Z));
		controller.setScale(modelValues.get(SCALE_X), modelValues.get(SCALE_Y), modelValues.get(SCALE_Z));
		controller.setPosition(modelValues.get(POSITION_X), modelValues.get(POSITION_Y), modelValues.get(POSITION_Z));
	}

	private ArrayList<Float> getModelValues(final String modelStatus) {
		ArrayList<Float> modelValues = new ArrayList<Float>();

		StringTokenizer tokenizer = new StringTokenizer(modelStatus);
		while (tokenizer.hasMoreTokens()) {
			modelValues.add(Float.parseFloat(tokenizer.nextToken()));
		}

		return modelValues;
	}
}
