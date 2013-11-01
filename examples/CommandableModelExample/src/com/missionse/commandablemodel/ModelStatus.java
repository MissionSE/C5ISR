package com.missionse.commandablemodel;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.missionse.modelviewer.ModelViewerFragment;

public class ModelStatus {
	private ModelViewerFragment modelViewer;
	private static final int POSITION_X = 0;
	private static final int POSITION_Y = 1;
	private static final int POSITION_Z = 2;

	private static final int SCALE_X = 3;
	private static final int SCALE_Y = 4;
	private static final int SCALE_Z = 5;

	private static final int ROTATION_X = 6;
	private static final int ROTATION_Y = 7;
	private static final int ROTATION_Z = 8;

	public ModelStatus() {
	}

	public void setModelViewer(final ModelViewerFragment modelViewerFragment) {
		modelViewer = modelViewerFragment;
	}

	public String toString() {
		String modelStatus = "";

		if (modelViewer.getController() != null) {
			modelStatus += modelViewer.getController().getXPosition() + " " + modelViewer.getController().getYPosition() + " " + modelViewer.getController().getZPosition();
			modelStatus += " ";
			modelStatus += modelViewer.getController().getXScale() + " " + modelViewer.getController().getYScale() + " " + modelViewer.getController().getZScale();
			modelStatus += " ";
			modelStatus += modelViewer.getController().getXRotation() + " " + modelViewer.getController().getYRotation() + " " + modelViewer.getController().getZRotation();
		}

		return modelStatus;
	}

	public void setModelStatus(final String modelStatus) {
		if (modelViewer.getController() != null) {
			ArrayList<Float> modelValues = getModelValues(modelStatus);
			modelViewer.getController().setRotation(modelValues.get(ROTATION_X), modelValues.get(ROTATION_Y), modelValues.get(ROTATION_Z));
			modelViewer.getController().setScale(modelValues.get(SCALE_X), modelValues.get(SCALE_Y), modelValues.get(SCALE_Z));
			modelViewer.getController().setPosition(modelValues.get(POSITION_X), modelValues.get(POSITION_Y), modelValues.get(POSITION_Z));
		}
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
