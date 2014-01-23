package com.missionse.modelviewer;

/**
 * Provides a callback to process an object being picked.
 */
public interface ObjectPickedListener {
	/**
	 * Called when an object has been picked with the picked object's name.
	 * @param objectName The name of the object that was picked.
	 */
	void objectPicked(final String objectName);
}
