package com.missionse.modelviewer;

import rajawali.Object3D;
import rajawali.renderer.RajawaliRenderer;

public interface ModelParser {
	Object3D parse(RajawaliRenderer renderer, int resourceId);
}
