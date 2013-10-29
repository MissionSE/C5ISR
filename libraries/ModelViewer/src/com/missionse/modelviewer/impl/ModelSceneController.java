package com.missionse.modelviewer.impl;

import java.util.ArrayList;

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
import com.missionse.modelviewer.ObjectPickedListener;

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

		private boolean rotationLocked, scaleLocked, translationLocked;

		public ModelSceneRenderer(final Context context, final int model, final ModelParser parser) {
			super(context, parser);
			modelID = model;
			modelParser = parser;
			rotationLocked = false;
			scaleLocked = false;
			translationLocked = false;
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
				for (Object3D object : getObjects()) {
					objectPicker.registerObject(object);
				}

				rotationAnim = new RotateAnimation3D(Axis.Y, 360);
				rotationAnim.setDuration(8000);
				rotationAnim.setRepeatMode(RepeatMode.INFINITE);
				rotationAnim.setTransformable3D(objectGroup);
				registerAnimation(rotationAnim);
			}
		}

		protected ArrayList<Object3D> getObjects() {
			ArrayList<Object3D> objects = new ArrayList<Object3D>();
			for (int index = 0; index < objectGroup.getNumObjects(); ++index) {
				objects.add(objectGroup.getChildAt(index));
			}

			return objects;
		}

		@Override
		public ArrayList<String> getObjectList() {
			ArrayList<String> objectNames = new ArrayList<String>();
			for (Object3D object : getObjects()) {
				if (object.getName() != null) {
					objectNames.add(object.getName());
				}
			}
			return objectNames;
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
			if (!rotationLocked) {
				objectGroup.rotateAround(Vector3.Y, xAngle);
				objectGroup.rotateAround(Vector3.X, yAngle);
				objectGroup.rotateAround(Vector3.Z, zAngle);
			}
		}

		@Override
		public void scale(final float scaleFactor) {
			if (!scaleLocked) {
				Vector3 scale = objectGroup.getScale();
				objectGroup.setScaleX(scale.x * scaleFactor);
				objectGroup.setScaleY(scale.y * scaleFactor);
				objectGroup.setScaleZ(scale.z * scaleFactor);
			}
		}

		@Override
		public void translate(final float xDistance, final float yDistance, final float zDistance) {
			if (!translationLocked) {
				objectGroup.setX(objectGroup.getX() + xDistance);
				objectGroup.setY(objectGroup.getY() + yDistance);
				objectGroup.setZ(objectGroup.getZ() + zDistance);
			}
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
			if (objectPicker != null) {
				objectPicker.getObjectAt(x, y);
			}
		}

		@Override
		public void onObjectPicked(final Object3D object) {
			if (object != null && object.getName() != null) {
				for (ObjectPickedListener listener : objectPickedListeners) {
					listener.objectPicked(object.getName());
				}
			}
		}

		@Override
		public void lockRotation() {
			rotationLocked = true;
		}

		@Override
		public void unlockRotation() {
			rotationLocked = false;
		}

		@Override
		public boolean isRotationLocked() {
			return rotationLocked;
		}

		@Override
		public void lockScale() {
			scaleLocked = true;
		}

		@Override
		public void unlockScale() {
			scaleLocked = false;
		}

		@Override
		public boolean isScaleLocked() {
			return scaleLocked;
		}

		@Override
		public void lockTranslation() {
			translationLocked = true;
		}

		@Override
		public void unlockTranslation() {
			translationLocked = false;
		}

		@Override
		public boolean isTranslationLocked() {
			return translationLocked;
		}

		@Override
		public void reset() {
			if (objectGroup != null) {
				objectGroup.setPosition(new Vector3());
				objectGroup.setRotation(new Vector3());
				objectGroup.setScale(1);
			}
		}

		@Override
		public void center() {
			if (objectGroup != null) {
				objectGroup.setPosition(new Vector3());
			}
		}
	}
}
