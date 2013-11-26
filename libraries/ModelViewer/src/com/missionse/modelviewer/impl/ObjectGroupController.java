package com.missionse.modelviewer.impl;

import java.util.ArrayList;

import rajawali.Object3D;
import rajawali.materials.Material;
import rajawali.math.Quaternion;
import rajawali.math.vector.Vector3;
import rajawali.util.ObjectColorPicker;
import rajawali.util.OnObjectPickedListener;

import com.missionse.modelviewer.ModelController;
import com.missionse.modelviewer.ModelViewerRenderer;

public class ObjectGroupController implements ModelController {

	private ModelViewerRenderer renderer;
	private OnObjectPickedListener objectPickedListener;
	private Object3D objectGroup;
	private ArrayList<Object3D> objectList;

	private ObjectColorPicker objectPicker;
	private boolean rotationLocked, scaleLocked, translationLocked;

	public ObjectGroupController(final ModelViewerRenderer modelRenderer, final OnObjectPickedListener listener) {
		renderer = modelRenderer;
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
	}

	private ArrayList<Object3D> getObjects() {
		ArrayList<Object3D> objects = new ArrayList<Object3D>();
		for (int index = 0; index < objectGroup.getNumObjects(); ++index) {
			objects.add(objectGroup.getChildAt(index));
		}

		return objects;
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
	public void rotate(final double x, final double y, final double z) {
		if (!rotationLocked && objectGroup != null) {
			Vector3 rotation = objectGroup.getRotation();
			objectGroup.setRotX(rotation.x + x);
			objectGroup.setRotY(rotation.y + y);
			objectGroup.setRotZ(rotation.z + z);
		}
	}

	@Override
	public void rotateAround(final double x, final double y, final double z) {
		if (!rotationLocked && objectGroup != null) {
			objectGroup.rotateAround(Vector3.X, x);
			objectGroup.rotateAround(Vector3.Y, y);
			objectGroup.rotateAround(Vector3.Z, z);
		}
	}

	@Override
	public void scale(final double scaleFactor) {
		scale (scaleFactor, scaleFactor, scaleFactor);
	}

	@Override
	public void scale(final double x, final double y, final double z) {
		if (!scaleLocked && objectGroup != null) {
			Vector3 scale = objectGroup.getScale();
			objectGroup.setScaleX(scale.x * x);
			objectGroup.setScaleY(scale.y * y);
			objectGroup.setScaleZ(scale.z * z);
		}
	}

	@Override
	public void translate(final double x, final double y, final double z) {
		if (!translationLocked && objectGroup != null) {
			objectGroup.setX(objectGroup.getX() + x);
			objectGroup.setY(objectGroup.getY() + y);
			objectGroup.setZ(objectGroup.getZ() + z);
		}
	}

	@Override
	public void setRotation(final double x, final double y, final double z) {
		if (!rotationLocked && objectGroup != null) {
			objectGroup.setRotX(x);
			objectGroup.setRotY(y);
			objectGroup.setRotZ(z);
		}
	}

	@Override
	public void setScale(final double x, final double y, final double z) {
		if (!scaleLocked && objectGroup != null) {
			objectGroup.setScaleX(x);
			objectGroup.setScaleY(y);
			objectGroup.setScaleZ(z);
		}
	}

	@Override
	public void setPosition(final double x, final double y, final double z) {
		if (!translationLocked && objectGroup != null) {
			objectGroup.setX(x);
			objectGroup.setY(y);
			objectGroup.setZ(z);
		}
	}

	@Override
	public void setOrientation(final double w, final double x, final double y, final double z) {
		if (!rotationLocked && objectGroup != null) {
//			Quaternion quaternion = new Quaternion();
//			quaternion.x = x;
//			quaternion.y = y;
//			quaternion.z = z;
//			quaternion.computeW();
			objectGroup.setOrientation(new Quaternion(w, x, y, z));
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

	@Override
	public double getXRotation() {
		double xRotation = 0.0f;
		if (objectGroup != null) {
			xRotation = objectGroup.getRotX();
		}
		return xRotation;
	}

	@Override
	public double getYRotation() {
		double yRotation = 0.0f;
		if (objectGroup != null) {
			yRotation = objectGroup.getRotY();
		}
		return yRotation;
	}

	@Override
	public double getZRotation() {
		double zRotation = 0.0f;
		if (objectGroup != null) {
			zRotation = objectGroup.getRotZ();
		}
		return zRotation;
	}

	@Override
	public double getXScale() {
		double xScale = 0.0f;
		if (objectGroup != null) {
			xScale = objectGroup.getScaleX();
		}
		return xScale;
	}

	@Override
	public double getYScale() {
		double yScale = 0.0f;
		if (objectGroup != null) {
			yScale = objectGroup.getScaleY();
		}
		return yScale;
	}

	@Override
	public double getZScale() {
		double zScale = 0.0f;
		if (objectGroup != null) {
			zScale = objectGroup.getScaleZ();
		}
		return zScale;
	}

	@Override
	public double getXPosition() {
		double xPosition = 0.0f;
		if (objectGroup != null) {
			xPosition = objectGroup.getX();
		}
		return xPosition;
	}

	@Override
	public double getYPosition() {
		double yPosition = 0.0f;
		if (objectGroup != null) {
			yPosition = objectGroup.getY();
		}
		return yPosition;
	}

	@Override
	public double getZPosition() {
		double zPosition = 0.0f;
		if (objectGroup != null) {
			zPosition = objectGroup.getZ();
		}
		return zPosition;
	}

	@Override
	public double getWOrientation() {
		double wOrientation = 0.0f;
		if (objectGroup != null) {
			wOrientation = objectGroup.getOrientation(new Quaternion()).w;
		}
		return wOrientation;
	}

	@Override
	public double getXOrientation() {
		double xOrientation = 0.0f;
		if (objectGroup != null) {
			xOrientation = objectGroup.getOrientation(new Quaternion()).x;
		}
		return xOrientation;
	}

	@Override
	public double getYOrientation() {
		double yOrientation = 0.0f;
		if (objectGroup != null) {
			yOrientation = objectGroup.getOrientation(new Quaternion()).y;
		}
		return yOrientation;
	}

	@Override
	public double getZOrientation() {
		double zOrientation = 0.0f;
		if (objectGroup != null) {
			zOrientation = objectGroup.getOrientation(new Quaternion()).z;
		}
		return zOrientation;
	}
}
