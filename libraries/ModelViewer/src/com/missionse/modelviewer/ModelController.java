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

	void rotate(final float x, final float y, final float z);
	void scale(final float scaleFactor);
	void translate(final float x, final float y, final float z);

	void reset();
	void center();

	int getAmbientColor(final String objectName);
	boolean setAmbientColor(final String objectName, final int color);

	ArrayList<String> getObjectList();
	void getObjectAt(final float x, final float y);
}
