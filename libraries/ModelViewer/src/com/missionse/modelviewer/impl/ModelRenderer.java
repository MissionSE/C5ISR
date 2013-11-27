package com.missionse.modelviewer.impl;

import rajawali.Object3D;
import rajawali.lights.DirectionalLight;
import android.content.Context;

import com.missionse.modelviewer.ModelAnimationController;
import com.missionse.modelviewer.ModelController;
import com.missionse.modelviewer.ModelParser;
import com.missionse.modelviewer.ModelViewerFragment;
import com.missionse.modelviewer.ModelViewerRenderer;

/**
 * Provides a renderer specific to a model viewer.
 */
public class ModelRenderer extends ModelViewerRenderer {
	private final int mModelId;
	private final ModelParser mModelParser;

	private Object3D mObjectGroup;

	private ObjectGroupController mObjectController;
	private AnimationController mAnimationController;

	private static final int LIGHT_Z_OFFSET = 4;
	private static final int CAMERA_Z_OFFSET = 16;

	/**
	 * Constructor.
	 * @param context The context of the owner of the renderer.
	 * @param fragment The fragment which contains the renderer.
	 * @param model The resource id of the model to be rendered.
	 * @param parser The parser used to parse the model file.
	 */
	public ModelRenderer(final Context context, final ModelViewerFragment fragment, final int model, final ModelParser parser) {
		super(context, fragment);
		mModelId = model;
		mModelParser = parser;
		mObjectController = new ObjectGroupController(this, fragment);
		mAnimationController = new AnimationController(this);
	}

	@Override
	protected void initScene() {

		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setPosition(0, 0, LIGHT_Z_OFFSET);
		directionalLight.setPower(1);

		getCurrentScene().addLight(directionalLight);
		getCurrentCamera().setZ(CAMERA_Z_OFFSET);

		mObjectGroup = mModelParser.parse(this, mModelId);
		if (null != mObjectGroup) {
			addChild(mObjectGroup);
			mObjectController.setObjectGroup(mObjectGroup);
			mAnimationController.setObjectGroup(mObjectGroup);
		}

		getFragment().onObjectLoaded();
	}

	@Override
	public ModelController getController() {
		return mObjectController;
	}

	@Override
	public ModelAnimationController getAnimator() {
		return mAnimationController;
	}
}
