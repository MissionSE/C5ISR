package com.missionse.modelviewer;


public interface ModelAnimationController {

	public void startXRotation(final long durationMS);
	public void startYRotation(final long durationMS);
	public void rotateTo(final float xDegrees, final float yDegrees, final float zDegrees, final long durationMS);
	public void stopRotation();
	public boolean isRotating();

	public void scaleTo(final float scale, final long durationMS);
	public void stopScaling();
	public boolean isScaling();

	public void translateTo(final float x, final float y, final float z, final long durationMS);
	public void stopTranslation();
	public boolean isTranslating();
}
