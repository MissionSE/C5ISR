package com.missionse.modelviewer;

import rajawali.Object3D;
import rajawali.renderer.RajawaliRenderer;

/**
 * Provides an abstract parse utility that parses a model file.
 */
public interface ModelParser {
	/**
	 * Parses a model file at a specified resource and returns the 3d object.
	 * @param renderer The Rajawali renderer that will render object.
	 * @param resourceId The resource id of the model.
	 * @return An Object3D representing the parsed 3d model.
	 */
	Object3D parse(RajawaliRenderer renderer, int resourceId);
}
