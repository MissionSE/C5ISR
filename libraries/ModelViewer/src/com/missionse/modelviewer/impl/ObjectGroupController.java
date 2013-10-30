package com.missionse.modelviewer.impl;

import java.util.ArrayList;

import rajawali.Object3D;
import rajawali.animation.Animation3D;
import rajawali.animation.Animation3D.RepeatMode;
import rajawali.animation.RotateAnimation3D;
import rajawali.materials.Material;
import rajawali.math.vector.Vector3;
import rajawali.math.vector.Vector3.Axis;
import rajawali.renderer.RajawaliRenderer;
import rajawali.util.ObjectColorPicker;
import rajawali.util.OnObjectPickedListener;

import com.missionse.modelviewer.ModelController;

public class ObjectGroupController implements ModelController {

	private RajawaliRenderer renderer;
	private OnObjectPickedListener objectPickedListener;
	private Object3D objectGroup;
	private ArrayList<Object3D> objectList;

	private ObjectColorPicker objectPicker;
	private boolean rotationLocked, scaleLocked, translationLocked;
	private Animation3D rotationAnim;

	public ObjectGroupController(final RajawaliRenderer rajawaliRenderer, final OnObjectPickedListener listener) {
		renderer = rajawaliRenderer;
		objectPickedListener = listener;
	}

	public void setObjectGroup(final Object3D group) {
		objectGroup = group;

		objectPicker = new ObjectColorPicker(renderer);
		objectPicker.setOnObjectPickedListener(objectPickedListener);

		objectList = getObjects();
		for (Object3D object : objectList) {
			objectPicker.registerObject(object);
		}

		rotationAnim = new RotateAnimation3D(Axis.Y, 360);
		rotationAnim.setDuration(8000);
		rotationAnim.setRepeatMode(RepeatMode.INFINITE);
		rotationAnim.setTransformable3D(objectGroup);
		renderer.registerAnimation(rotationAnim);
	}

	private ArrayList<Object3D> getObjects() {
		ArrayList<Object3D> objects = new ArrayList<Object3D>();
		for (int index = 0; index < objectGroup.getNumObjects(); ++index) {
			objects.add(objectGroup.getChildAt(index));
		}

		return objects;
	}

	@Override
	public void setAutoRotation(final boolean autoRotate) {
		if (rotationAnim != null) {
			if (!isAutoRotating()) {
				if (autoRotate) {
					rotationAnim.reset();
					rotationAnim.play();
				}
			} else if (!autoRotate) {
				rotationAnim.pause();
			}
		}
	}

	@Override
	public boolean isAutoRotating() {
		return (rotationAnim != null && rotationAnim.isPlaying());
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
	public void rotate(final float x, final float y, final float z) {
		if (!rotationLocked && objectGroup != null) {
			objectGroup.rotateAround(Vector3.Y, x);
			objectGroup.rotateAround(Vector3.X, y);
			objectGroup.rotateAround(Vector3.Z, z);
		}
	}

	@Override
	public void scale(final float scaleFactor) {
		if (!scaleLocked && objectGroup != null) {
			Vector3 scale = objectGroup.getScale();
			objectGroup.setScaleX(scale.x * scaleFactor);
			objectGroup.setScaleY(scale.y * scaleFactor);
			objectGroup.setScaleZ(scale.z * scaleFactor);
		}
	}

	@Override
	public void translate(final float x, final float y, final float z) {
		if (!translationLocked && objectGroup != null) {
			objectGroup.setX(objectGroup.getX() + x);
			objectGroup.setY(objectGroup.getY() + y);
			objectGroup.setZ(objectGroup.getZ() + z);
		}
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

	@Override
	public int getAmbientColor(final String objectName) {
		int color = -1;
		if (objectGroup != null) {
			Object3D object = objectGroup.getChildByName(objectName);
			if (null != object) {
				Material material = object.getMaterial();
				if (null != material) {
					color = material.getAmbientColor();
				}
			}
		}

		return color;
	}

	@Override
	public boolean setAmbientColor(final String objectName, final int color) {
		boolean successful = false;
		if (objectGroup != null) {
			Object3D object = objectGroup.getChildByName(objectName);
			if (null != object) {
				Material material = object.getMaterial();
				if (null != material) {
					material.setAmbientColor(color);
					successful = true;
				}
			}
		}

		return successful;
	}

	@Override
	public ArrayList<String> getObjectList() {
		ArrayList<String> objectNames = new ArrayList<String>();

		if (objectList != null) {
			for (Object3D object : objectList) {
				if (object.getName() != null) {
					objectNames.add(object.getName());
				}
			}
		}
		return objectNames;
	}

	@Override
	public void getObjectAt(final float x, final float y) {
		if (objectPicker != null) {
			objectPicker.getObjectAt(x, y);
		}
	}

}
