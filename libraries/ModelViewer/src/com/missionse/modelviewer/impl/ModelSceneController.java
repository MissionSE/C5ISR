package com.missionse.modelviewer.impl;

import rajawali.Object3D;
import rajawali.animation.Animation3D;
import rajawali.animation.Animation3D.RepeatMode;
import rajawali.animation.RotateAnimation3D;
import rajawali.lights.DirectionalLight;
import rajawali.math.vector.Vector3;
import rajawali.math.vector.Vector3.Axis;
import android.content.Context;

import com.missionse.modelviewer.ModelParser;
import com.missionse.modelviewer.ModelViewerFragment;

public class ModelSceneController extends ModelViewerFragment {

	@Override
	protected ModelViewerRenderer createRenderer(final int modelID, final ModelParser parser) {
		return new ModelSceneRenderer(getActivity(), modelID, parser);
	}

	private final class ModelSceneRenderer extends ModelViewerRenderer {

		private final int modelID;
		private final ModelParser modelParser;

		private DirectionalLight directionalLight;
		private Object3D objectGroup;
		private Animation3D rotationAnim;

		public ModelSceneRenderer(final Context context, final int model, final ModelParser parser) {
			super(context, parser);
			modelID = model;
			modelParser = parser;
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

				rotationAnim = new RotateAnimation3D(Axis.Y, 360);
				rotationAnim.setDuration(8000);
				rotationAnim.setRepeatMode(RepeatMode.INFINITE);
				rotationAnim.setTransformable3D(objectGroup);
				registerAnimation(rotationAnim);
			}
		}

		@Override
		public void setAutoRotation(final boolean autoRotate) {
			if (!isAutoRotating()) {
				if (autoRotate) {
					rotationAnim.reset();
					rotationAnim.play();
				}
			} else if (!autoRotate) {
				rotationAnim.pause();
			}
		}

		@Override
		public boolean isAutoRotating() {
			return rotationAnim.isPlaying();
		}

		@Override
		public void rotate(final float xAngle, final float yAngle, final float zAngle) {
			objectGroup.rotateAround(Vector3.Y, xAngle);
			objectGroup.rotateAround(Vector3.X, yAngle);
			objectGroup.rotateAround(Vector3.Z, zAngle);
		}

		@Override
		public void scale(final float scaleFactor) {
			Vector3 scale = objectGroup.getScale();
			objectGroup.setScaleX(scale.x * scaleFactor);
			objectGroup.setScaleY(scale.y * scaleFactor);
			objectGroup.setScaleZ(scale.z * scaleFactor);
		}

		@Override
		public void translate(final float xDistance, final float yDistance, final float zDistance) {
			objectGroup.setX(objectGroup.getX() + xDistance);
			objectGroup.setY(objectGroup.getY() + yDistance);
			objectGroup.setZ(objectGroup.getZ() + zDistance);
		}
	}
}
