package com.missionse.modelviewer.impl;

import rajawali.Object3D;
import rajawali.parser.LoaderOBJ;
import rajawali.parser.ParsingException;
import rajawali.renderer.RajawaliRenderer;
import android.util.Log;

import com.missionse.modelviewer.ModelParser;

public class ObjModelParser implements ModelParser {

	private static final String TAG = "ObjModelParser";

	@Override
	public Object3D parse(final RajawaliRenderer renderer, final int resourceId) {

		Object3D parsedObject = null;
		LoaderOBJ objParser = new LoaderOBJ(renderer, resourceId);

		try {
			objParser.parse();
			parsedObject = objParser.getParsedObject();

			Log.d(TAG, "Number of Objects: " + parsedObject.getNumObjects());
			for (int i = 0; i < parsedObject.getNumObjects(); i++) {
				Log.d(TAG, "Object " + i + ": " + parsedObject.getChildAt(i).getName());
			}

		} catch (ParsingException e) {
			e.printStackTrace();
		}

		return parsedObject;
	}
}
