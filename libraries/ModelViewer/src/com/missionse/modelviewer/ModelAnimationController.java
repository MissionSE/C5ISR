package com.missionse.modelviewer;

/**
 * Provides a set of functions used to animate a model.
 */
public interface ModelAnimationController {

	/**
	 * Starts rotating the model along the x axis.
	 * @param durationMs The amount of time it takes to complete one rotation.
	 */
	void startXRotation(final long durationMs);

	/**
	 * Starts rotating the model along the y axis.
	 * @param durationMs The amount of time it takes to complete one rotation.
	 */
	void startYRotation(final long durationMs);

	/**
	 * Rotates to a specified orientation over a specified duration.
	 * @param xDegrees The angle in degrees to rotate along the x axis.
	 * @param yDegrees The angle in degrees to rotate along the y axis.
	 * @param zDegrees The angle in degrees to rotate along the z axis.
	 * @param durationMs The amount of time it takes to complete the rotation.
	 */
	void rotateTo(final float xDegrees, final float yDegrees, final float zDegrees, final long durationMs);

	/**
	 * Stops the rotation animation if it is in progress.
	 */
	void stopRotation();

	/**
	 * Determines whether the rotation is in progress.
	 * @return Whether the rotation is in progress.
	 */
	boolean isRotating();

	/**
	 * Scales to a specified scaling factor over a specified duration.
	 * @param scale The amount with which to scale the model
	 * @param durationMs The amount of time it takes to complete the scaling.
	 */
	void scaleTo(final float scale, final long durationMs);

	/**
	 * Stops the scaling animation if it is in progress.
	 */
	void stopScaling();

	/**
	 * Determines whether the scaling is in progress.
	 * @return Whether the scaling is in progress.
	 */
	boolean isScaling();

	/**
	 * Translates to a specified location over a specified duration.
	 * @param x The amount with which to translate in the x direction.
	 * @param y The amount with which to translate in the y direction.
	 * @param z The amount with which to translate in the z direction.
	 * @param durationMs The amount of time it takes to complete the translation.
	 */
	void translateTo(final float x, final float y, final float z, final long durationMs);

	/**
	 * Stops the translation animation if it is in progress.
	 */
	void stopTranslation();

	/**
	 * Determines whether the scaling is in progress.
	 * @return Whether the translation is in progress.
	 */
	boolean isTranslating();
}
