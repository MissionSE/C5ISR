package com.missionse.modelviewer;

import rajawali.Object3D;
import rajawali.animation.Animation3D;
import rajawali.animation.Animation3D.RepeatMode;
import rajawali.animation.RotateAnimation3D;
import rajawali.lights.DirectionalLight;
import rajawali.math.vector.Vector3.Axis;
import rajawali.parser.LoaderOBJ;
import rajawali.parser.ParsingException;
import rajawali.renderer.RajawaliRenderer;
import android.content.Context;

public class ModelViewerRenderer extends RajawaliRenderer {
	private final int modelID;

	private DirectionalLight directionalLight;
	private Object3D objectGroup;
	private Animation3D cameraAnim;

	public ModelViewerRenderer(final Context context, final int model) {
		super(context);
		modelID = model;

		setFrameRate(60);
	}

	@Override
	protected void initScene() {
		directionalLight = new DirectionalLight();
		directionalLight.setPosition(0, 0, 4);
		directionalLight.setPower(1);

		getCurrentScene().addLight(directionalLight);
		getCurrentCamera().setZ(16);

		LoaderOBJ objParser = new LoaderOBJ(mContext.getResources(),
				mTextureManager, modelID);
		try	{
			objParser.parse();
			objectGroup = objParser.getParsedObject();
			addChild(objectGroup);
		} catch (ParsingException e) {
			e.printStackTrace();
		}

		initCameraAnimation();
	}

	private void initCameraAnimation() {
		cameraAnim = new RotateAnimation3D(Axis.Y, 360);
		cameraAnim.setDuration(8000);
		cameraAnim.setRepeatMode(RepeatMode.INFINITE);
		cameraAnim.setTransformable3D(objectGroup);

		registerAnimation(cameraAnim);
	}

	public void setCameraAnimation(final boolean animating) {
		if (!isCameraAnimating()) {
			if (animating) {
				cameraAnim.play();
			}
		} else if (!animating) {
			cameraAnim.pause();
		}
	}

	public boolean isCameraAnimating() {
		return cameraAnim.isPlaying();
	}
}
