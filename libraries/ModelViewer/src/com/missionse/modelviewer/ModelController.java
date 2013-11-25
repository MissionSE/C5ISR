package com.missionse.modelviewer;

import java.util.ArrayList;


public interface ModelController {

	void lockRotation();
	void unlockRotation();
	boolean isRotationLocked();

	void lockScale();
	void unlockScale();
	boolean isScaleLocked();

	void lockTranslation();
	void unlockTranslation();
	boolean isTranslationLocked();

	void rotate(final double x, final double y, final double z);
	void rotateAroundAxis(final double x, final double y, final double z);
	void scale(final double scaleFactor);
	void scale(final double x, final double y, final double z);
	void translate(final double x, final double y, final double z);

	void setRotation(final double x, final double y, final double z);
	void setScale(final double x, final double y, final double z);
	void setPosition(final double x, final double y, final double z);
	void setOrientation(final double yaw, final double pitch, final double roll);

	double getXRotation();
	double getYRotation();
	double getZRotation();

	double getXScale();
	double getYScale();
	double getZScale();

	double getXPosition();
	double getYPosition();
	double getZPosition();

	double getYaw();
	double getPitch();
	double getRoll();

	void reset();
	void center();

	int getAmbientColor(final String objectName);
	boolean setAmbientColor(final String objectName, final int color);

	ArrayList<String> getObjectList();
	void getObjectAt(final float x, final float y);
}
