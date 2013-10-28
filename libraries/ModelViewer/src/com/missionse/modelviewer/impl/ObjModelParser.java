package com.missionse.modelviewer.impl;

import rajawali.Object3D;
import rajawali.parser.LoaderOBJ;
import rajawali.parser.ParsingException;
import rajawali.renderer.RajawaliRenderer;

import com.missionse.modelviewer.ModelParser;

public class ObjModelParser implements ModelParser {

	@Override
	public Object3D parse(final RajawaliRenderer renderer, final int resourceId) {
		Object3D parsedObject = null;

		LoaderOBJ objParser = new LoaderOBJ(renderer, resourceId);

		try {
			objParser.parse();
			parsedObject = objParser.getParsedObject();
		} catch (ParsingException e) {
			e.printStackTrace();
		}

		return parsedObject;
	}
}
