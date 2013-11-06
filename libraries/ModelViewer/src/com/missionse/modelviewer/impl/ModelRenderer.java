package com.missionse.modelviewer.impl;

import rajawali.Object3D;
import rajawali.lights.DirectionalLight;
import android.content.Context;

import com.missionse.modelviewer.ModelAnimationController;
import com.missionse.modelviewer.ModelController;
import com.missionse.modelviewer.ModelParser;
import com.missionse.modelviewer.ModelViewerFragment;
import com.missionse.modelviewer.ModelViewerRenderer;

public class ModelRenderer extends ModelViewerRenderer {
	private final int modelID;
	private final ModelParser modelParser;

	private DirectionalLight directionalLight;
	private Object3D objectGroup;

	private ObjectGroupController objectController;
	private AnimationController animationController;

	public ModelRenderer(final Context context, final ModelViewerFragment fragment, final int model, final ModelParser parser) {
		super(context, fragment);
		modelID = model;
		modelParser = parser;
		objectController = new ObjectGroupController(this, fragment);
		animationController = new AnimationController(this);
	}

	@Override
	protected void initScene() {

		directionalLight = new DirectionalLight();
		directionalLight.setPosition(0, 0, 4);
		directionalLight.setPower(1);

		getCurrentScene().addLight(directionalLight);
		getCurrentCamera().setZ(16);

		objectGroup = modelParser.parse(this, modelID);
		if (null != objectGroup) {
			addChild(objectGroup);
			objectController.setObjectGroup(objectGroup);
			animationController.setObject(objectGroup);
		}

		fragment.onObjectLoaded();
	}

	@Override
	public ModelController getController() {
		return objectController;
	}

	@Override
	public ModelAnimationController getAnimator() {
		return animationController;
	}
}
