package com.missionse.commandablemodel;

import com.missionse.modelviewer.ModelController;

public class WifiDirectModelNotifier implements ModelNotifier {

	@Override
	public void notify(final ModelController modelController) {

	}

	public String getModelStatus(final ModelController modelController) {
		String modelStatus = "";
		modelStatus += modelController.getXPosition() + " " + modelController.getYPosition() + " " + modelController.getZPosition();
		modelStatus += " ";
		modelStatus += modelController.getXScale() + " " + modelController.getYScale() + " " + modelController.getZScale();
		modelStatus += " ";
		modelStatus += modelController.getXRotation() + " " + modelController.getYRotation() + " " + modelController.getZRotation();

		return modelStatus;
	}
}
