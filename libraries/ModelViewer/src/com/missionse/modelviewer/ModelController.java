package com.missionse.modelviewer;

public interface ModelController {
	void setAutoRotation(final boolean autoRotate);
	boolean isAutoRotating();

	void rotate(final float xAngle, final float yAngle, final float zAngle);
	void scale(final float scaleFactor);
	void translate(final float xDistance, final float yDistance, final float zDistance);

	int getAmbientColor(final String objectName);
	boolean setAmbientColor(final String objectName, final int color);

	void getObjectAt(final float x, final float y);
	void registerObjectSelectedListener(final ObjectSelectedListener listener);
}
