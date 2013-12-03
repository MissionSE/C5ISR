package com.missionse.modelviewer;

import java.util.List;

/**
 * Provides a set of functions used to control a model.
 */
public interface ModelController {

	/**
	 * Locks rotation of the model.
	 */
	void lockRotation();

	/**
	 * Unlocks rotation of the model.
	 */
	void unlockRotation();

	/**
	 * Determines whether the rotation of the model is locked.
	 * @return Whether model rotation is locked.
	 */
	boolean isRotationLocked();

	/**
	 * Locks scaling of the model.
	 */
	void lockScale();

	/**
	 * Unlocks scaling of the model.
	 */
	void unlockScale();

	/**
	 * Determines whether the scaling of the model is locked.
	 * @return Whether model scaling is locked.
	 */
	boolean isScaleLocked();

	/**
	 * Locks translation of the model.
	 */
	void lockTranslation();

	/**
	 * Unlocks translation of the model.
	 */
	void unlockTranslation();

	/**
	 * Determines whether the translation of the model is locked.
	 * @return Whether model translation is locked.
	 */
	boolean isTranslationLocked();

	/**
	 * Rotates the model in the x, y, and z directions.
	 * @param x The angle in degrees to rotate in the x direction.
	 * @param y The angle in degrees to rotate in the y direction.
	 * @param z The angle in degrees to rotate in the z direction.
	 */
	void rotate(final double x, final double y, final double z);

	/**
	 * Rotates the model around the x, y, and z axes.
	 * @param x The angle in degrees in which to rotate around the x axis.
	 * @param y The angle in degrees in which to rotate around the y axis.
	 * @param z The angle in degrees in which to rotate around the z axis.
	 */
	void rotateAround(final double x, final double y, final double z);

	/**
	 * Scales the model by a specified scale factor.
	 * @param scaleFactor The amount in which to scale the model.
	 */
	void scale(final double scaleFactor);

	/**
	 * Scales the model by a specified amount along the x, y, and z axes.
	 * @param x The amount in which to scale the x component.
	 * @param y The amount in which to scale the y component.
	 * @param z The amount in which to scale the z component.
	 */
	void scale(final double x, final double y, final double z);

	/**
	 * Translates the model by a specified amount in the x, y, and z directions.
	 * @param x The amount in which to translate the x component.
	 * @param y The amount in which to translate the y component.
	 * @param z The amount in which to translate the z component.
	 */
	void translate(final double x, final double y, final double z);

	/**
	 * Sets the rotation of the model.
	 * @param x The angle in degrees with which the x component is rotated.
	 * @param y The angle in degrees with which the y component is rotated.
	 * @param z The angle in degrees with which the z component is rotated.
	 */
	void setRotation(final double x, final double y, final double z);

	/**
	 * Sets the scale of the model.
	 * @param x The scaling factor with which to scale the x component of the model.
	 * @param y The scaling factor with which to scale the y component of the model.
	 * @param z The scaling factor with which to scale the z component of the model.
	 */
	void setScale(final double x, final double y, final double z);

	/**
	 * Sets the position of the model.
	 * @param x The x position of the model.
	 * @param y The y position of the model.
	 * @param z The z position of the model.
	 */
	void setPosition(final double x, final double y, final double z);

	/**
	 * Sets the orientation of the model.
	 * @param w The w component of the orientation.
	 * @param x The x component of the orientation.
	 * @param y The y component of the orientation.
	 * @param z The z component of the orientation.
	 */
	void setOrientation(final double w, final double x, final double y, final double z);

	/**
	 * Gets the x component of the rotation of the model.
	 * @return The x component of the rotation of the model.
	 */
	double getXRotation();

	/**
	 * Gets the y component of the rotation of the model.
	 * @return The y component of the rotation of the model.
	 */
	double getYRotation();

	/**
	 * Gets the z component of the rotation of the model.
	 * @return The z component of the rotation of the model.
	 */
	double getZRotation();

	/**
	 * Gets the x component of the scale of the model.
	 * @return The x component of the scale of the model.
	 */
	double getXScale();

	/**
	 * Gets the y component of the scale of the model.
	 * @return The y component of the scale of the model.
	 */
	double getYScale();

	/**
	 * Gets the z component of the scale of the model.
	 * @return The z component of the scale of the model.
	 */
	double getZScale();

	/**
	 * Gets the x component of the position of the model.
	 * @return The x component of the position of the model.
	 */
	double getXPosition();

	/**
	 * Gets the y component of the position of the model.
	 * @return The y component of the position of the model.
	 */
	double getYPosition();

	/**
	 * Gets the z component of the position of the model.
	 * @return The z component of the position of the model.
	 */
	double getZPosition();

	/**
	 * Gets the w component of the orientation of the model.
	 * @return The w component of the orientation of the model.
	 */
	double getWOrientation();

	/**
	 * Gets the x component of the orientation of the model.
	 * @return The x component of the orientation of the model.
	 */
	double getXOrientation();

	/**
	 * Gets the y component of the orientation of the model.
	 * @return The y component of the orientation of the model.
	 */
	double getYOrientation();

	/**
	 * Gets the z component of the orientation of the model.
	 * @return The z component of the orientation of the model.
	 */
	double getZOrientation();

	/**
	 * Resets the model to the initial state.
	 */
	void reset();

	/**
	 * Centers the model in the scene.
	 */
	void center();

	/**
	 * Gets the ambient color of a specified object.
	 * @param objectName The name of the object.
	 * @return The ambient color of the object.
	 */
	int getAmbientColor(final String objectName);

	/**
	 * Sets the ambient color of the specified object.
	 * @param objectName The name of the object.
	 * @param color The ambient color of the object.
	 * @return Whether the ambient color was successfully set for the object.
	 */
	boolean setAmbientColor(final String objectName, final int color);

	/**
	 * Get a list of the names of objects in the scene.
	 * @return A list of the names of objects in the scene.
	 */
	List<String> getObjectList();

	/**
	 * Attempts to find an object at the specified location and notifies any ObjectPickedListeners of picked objects.
	 * @param x The x coordinate to search for an object.
	 * @param y The y coordinate to search for an object.
	 */
	void getObjectAt(final float x, final float y);
}
