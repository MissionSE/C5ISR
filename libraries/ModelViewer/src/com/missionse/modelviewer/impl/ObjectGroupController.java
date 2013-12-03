package com.missionse.modelviewer.impl;

import java.util.ArrayList;
import java.util.List;

import rajawali.Object3D;
import rajawali.materials.Material;
import rajawali.math.Quaternion;
import rajawali.math.vector.Vector3;
import rajawali.util.ObjectColorPicker;
import rajawali.util.OnObjectPickedListener;

import com.missionse.modelviewer.ModelController;
import com.missionse.modelviewer.ModelViewerRenderer;

/**
 * Provides a set of functions used to control a model.
 */
public class ObjectGroupController implements ModelController {

	private ModelViewerRenderer mRenderer;
	private OnObjectPickedListener mObjectPickedListener;
	private Object3D mObjectGroup;
	private ArrayList<Object3D> mObjectList;

	private ObjectColorPicker mObjectPicker;
	private boolean mRotationLocked, mScaleLocked, mTranslationLocked;

	/**
	 * Constructor.
	 * @param renderer The renderer containing the object group.
	 * @param listener The listener to handle on object picked callbacks.
	 */
	public ObjectGroupController(final ModelViewerRenderer renderer, final OnObjectPickedListener listener) {
		mRenderer = renderer;
		mObjectPickedListener = listener;
	}

	/**
	 * Sets the object group to be animated.
	 * @param objectGroup The object group that will be controlled.
	 */
	public void setObjectGroup(final Object3D objectGroup) {
		mObjectGroup = objectGroup;

		mObjectPicker = new ObjectColorPicker(mRenderer);
		mObjectPicker.setOnObjectPickedListener(mObjectPickedListener);

		mObjectList = getObjects();
		for (Object3D object : mObjectList) {
			mObjectPicker.registerObject(object);
		}
	}

	private ArrayList<Object3D> getObjects() {
		ArrayList<Object3D> objects = new ArrayList<Object3D>();
		for (int index = 0; index < mObjectGroup.getNumObjects(); ++index) {
			objects.add(mObjectGroup.getChildAt(index));
		}

		return objects;
	}

	@Override
	public void lockRotation() {
		mRotationLocked = true;
	}

	@Override
	public void unlockRotation() {
		mRotationLocked = false;
	}

	@Override
	public boolean isRotationLocked() {
		return mRotationLocked;
	}

	@Override
	public void lockScale() {
		mScaleLocked = true;
	}

	@Override
	public void unlockScale() {
		mScaleLocked = false;
	}

	@Override
	public boolean isScaleLocked() {
		return mScaleLocked;
	}

	@Override
	public void lockTranslation() {
		mTranslationLocked = true;
	}

	@Override
	public void unlockTranslation() {
		mTranslationLocked = false;
	}

	@Override
	public boolean isTranslationLocked() {
		return mTranslationLocked;
	}

	@Override
	public void rotate(final double x, final double y, final double z) {
		if (!mRotationLocked && mObjectGroup != null) {
			Vector3 rotation = mObjectGroup.getRotation();
			mObjectGroup.setRotX(rotation.x + x);
			mObjectGroup.setRotY(rotation.y + y);
			mObjectGroup.setRotZ(rotation.z + z);
		}
	}

	@Override
	public void rotateAround(final double x, final double y, final double z) {
		if (!mRotationLocked && mObjectGroup != null) {
			mObjectGroup.rotateAround(Vector3.X, x);
			mObjectGroup.rotateAround(Vector3.Y, y);
			mObjectGroup.rotateAround(Vector3.Z, z);
		}
	}

	@Override
	public void scale(final double scaleFactor) {
		scale(scaleFactor, scaleFactor, scaleFactor);
	}

	@Override
	public void scale(final double x, final double y, final double z) {
		if (!mScaleLocked && mObjectGroup != null) {
			Vector3 scale = mObjectGroup.getScale();
			mObjectGroup.setScaleX(scale.x * x);
			mObjectGroup.setScaleY(scale.y * y);
			mObjectGroup.setScaleZ(scale.z * z);
		}
	}

	@Override
	public void translate(final double x, final double y, final double z) {
		if (!mTranslationLocked && mObjectGroup != null) {
			mObjectGroup.setX(mObjectGroup.getX() + x);
			mObjectGroup.setY(mObjectGroup.getY() + y);
			mObjectGroup.setZ(mObjectGroup.getZ() + z);
		}
	}

	@Override
	public void setRotation(final double x, final double y, final double z) {
		if (!mRotationLocked && mObjectGroup != null) {
			mObjectGroup.setRotX(x);
			mObjectGroup.setRotY(y);
			mObjectGroup.setRotZ(z);
		}
	}

	@Override
	public void setScale(final double x, final double y, final double z) {
		if (!mScaleLocked && mObjectGroup != null) {
			mObjectGroup.setScaleX(x);
			mObjectGroup.setScaleY(y);
			mObjectGroup.setScaleZ(z);
		}
	}

	@Override
	public void setPosition(final double x, final double y, final double z) {
		if (!mTranslationLocked && mObjectGroup != null) {
			mObjectGroup.setX(x);
			mObjectGroup.setY(y);
			mObjectGroup.setZ(z);
		}
	}

	@Override
	public void setOrientation(final double w, final double x, final double y, final double z) {
		if (!mRotationLocked && mObjectGroup != null) {
			mObjectGroup.setOrientation(new Quaternion(w, x, y, z));
		}
	}

	@Override
	public void reset() {
		if (mObjectGroup != null) {
			mObjectGroup.setPosition(new Vector3());
			mObjectGroup.setRotation(new Vector3());
			mObjectGroup.setScale(1);
		}
	}

	@Override
	public void center() {
		if (mObjectGroup != null) {
			mObjectGroup.setPosition(new Vector3());
		}
	}

	@Override
	public int getAmbientColor(final String objectName) {
		int color = -1;
		if (mObjectGroup != null) {
			Object3D object = mObjectGroup.getChildByName(objectName);
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
		if (mObjectGroup != null) {
			Object3D object = mObjectGroup.getChildByName(objectName);
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
	public List<String> getObjectList() {
		List<String> objectNames = new ArrayList<String>();

		if (mObjectList != null) {
			for (Object3D object : mObjectList) {
				if (object.getName() != null) {
					objectNames.add(object.getName());
				}
			}
		}
		return objectNames;
	}

	@Override
	public void getObjectAt(final float x, final float y) {
		if (mObjectPicker != null) {
			mObjectPicker.getObjectAt(x, y);
		}
	}

	@Override
	public double getXRotation() {
		double xRotation = 0.0f;
		if (mObjectGroup != null) {
			xRotation = mObjectGroup.getRotX();
		}
		return xRotation;
	}

	@Override
	public double getYRotation() {
		double yRotation = 0.0f;
		if (mObjectGroup != null) {
			yRotation = mObjectGroup.getRotY();
		}
		return yRotation;
	}

	@Override
	public double getZRotation() {
		double zRotation = 0.0f;
		if (mObjectGroup != null) {
			zRotation = mObjectGroup.getRotZ();
		}
		return zRotation;
	}

	@Override
	public double getXScale() {
		double xScale = 0.0f;
		if (mObjectGroup != null) {
			xScale = mObjectGroup.getScaleX();
		}
		return xScale;
	}

	@Override
	public double getYScale() {
		double yScale = 0.0f;
		if (mObjectGroup != null) {
			yScale = mObjectGroup.getScaleY();
		}
		return yScale;
	}

	@Override
	public double getZScale() {
		double zScale = 0.0f;
		if (mObjectGroup != null) {
			zScale = mObjectGroup.getScaleZ();
		}
		return zScale;
	}

	@Override
	public double getXPosition() {
		double xPosition = 0.0f;
		if (mObjectGroup != null) {
			xPosition = mObjectGroup.getX();
		}
		return xPosition;
	}

	@Override
	public double getYPosition() {
		double yPosition = 0.0f;
		if (mObjectGroup != null) {
			yPosition = mObjectGroup.getY();
		}
		return yPosition;
	}

	@Override
	public double getZPosition() {
		double zPosition = 0.0f;
		if (mObjectGroup != null) {
			zPosition = mObjectGroup.getZ();
		}
		return zPosition;
	}

	@Override
	public double getWOrientation() {
		double wOrientation = 0.0f;
		if (mObjectGroup != null) {
			wOrientation = mObjectGroup.getOrientation(new Quaternion()).w;
		}
		return wOrientation;
	}

	@Override
	public double getXOrientation() {
		double xOrientation = 0.0f;
		if (mObjectGroup != null) {
			xOrientation = mObjectGroup.getOrientation(new Quaternion()).x;
		}
		return xOrientation;
	}

	@Override
	public double getYOrientation() {
		double yOrientation = 0.0f;
		if (mObjectGroup != null) {
			yOrientation = mObjectGroup.getOrientation(new Quaternion()).y;
		}
		return yOrientation;
	}

	@Override
	public double getZOrientation() {
		double zOrientation = 0.0f;
		if (mObjectGroup != null) {
			zOrientation = mObjectGroup.getOrientation(new Quaternion()).z;
		}
		return zOrientation;
	}
}
