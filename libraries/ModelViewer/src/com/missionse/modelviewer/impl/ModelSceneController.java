package com.missionse.modelviewer.impl;

import rajawali.Object3D;
import rajawali.animation.Animation3D;
import rajawali.animation.Animation3D.RepeatMode;
import rajawali.animation.RotateAnimation3D;
import rajawali.lights.DirectionalLight;
import rajawali.materials.Material;
import rajawali.math.vector.Vector3;
import rajawali.math.vector.Vector3.Axis;
import rajawali.util.ObjectColorPicker;
import rajawali.util.OnObjectPickedListener;
import android.content.Context;

import com.missionse.modelviewer.ModelParser;
import com.missionse.modelviewer.ModelViewerFragment;
import com.missionse.modelviewer.ObjectSelectedListener;

public class ModelSceneController extends ModelViewerFragment {

	@Override
	protected ModelViewerRenderer createRenderer(final int modelID, final ModelParser parser) {
		return new ModelSceneRenderer(getActivity(), modelID, parser);
	}

	private final class ModelSceneRenderer extends ModelViewerRenderer implements OnObjectPickedListener {

		private final int modelID;
		private final ModelParser modelParser;

		private DirectionalLight directionalLight;
		private Object3D objectGroup;
		private Animation3D rotationAnim;
		private ObjectColorPicker objectPicker;
		private ObjectSelectedListener listener = null;

		public ModelSceneRenderer(final Context context, final int model, final ModelParser parser) {
			super(context, parser);
			modelID = model;
			modelParser = parser;
		}

		@Override
		protected void initScene() {
			objectPicker = new ObjectColorPicker(this);
			objectPicker.setOnObjectPickedListener(this);

			directionalLight = new DirectionalLight();
			directionalLight.setPosition(0, 0, 4);
			directionalLight.setPower(1);

			getCurrentScene().addLight(directionalLight);
			getCurrentCamera().setZ(16);

			objectGroup = modelParser.parse(this, modelID);
			if (null != objectGroup) {
				addChild(objectGroup);
				objectPicker.registerObject(objectGroup.getChildAt(0));
				objectPicker.registerObject(objectGroup.getChildAt(1));
				objectPicker.registerObject(objectGroup.getChildAt(2));
				objectPicker.registerObject(objectGroup.getChildAt(3));

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

		@Override
		public boolean setAmbientColor(final String objectName, final int color) {
			boolean successful = false;
			Object3D object = objectGroup.getChildByName(objectName);
			if (null != object) {
				Material material = object.getMaterial();
				if (null != material) {
					material.setAmbientColor(color);
					successful = true;
				}
			}

			return successful;
		}

		@Override
		public int getAmbientColor(final String objectName) {
			int color = -1;
			Object3D object = objectGroup.getChildByName(objectName);
			if (null != object) {
				Material material = object.getMaterial();
				if (null != material) {
					color = material.getAmbientColor();
				}
			}

			return color;
		}

		@Override
		public void getObjectAt(final float x, final float y) {
			objectPicker.getObjectAt(x, y);
		}

		@Override
		public void onObjectPicked(final Object3D object) {
			if (listener != null && object != null && object.getName() != null) {
				listener.objectSelected(object.getName());
			}
		}

		@Override
		public void registerObjectSelectedListener(final ObjectSelectedListener objectSelectedListener) {
			listener = objectSelectedListener;
		}
	}
}
