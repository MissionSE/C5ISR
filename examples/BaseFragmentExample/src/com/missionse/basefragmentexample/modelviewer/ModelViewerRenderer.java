package com.missionse.basefragmentexample.modelviewer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import rajawali.Object3D;
import rajawali.lights.DirectionalLight;
import rajawali.parser.LoaderOBJ;
import rajawali.parser.ParsingException;
import rajawali.renderer.RajawaliRenderer;
import android.content.Context;
import android.util.Log;

import com.missionse.basefragmentexample.R;

public class ModelViewerRenderer extends RajawaliRenderer {
	private static final String TAG = "ModelViewerRenderer";

	protected ModelViewerFragment fragment;

	private DirectionalLight directionalLight;
	private Object3D objectGroup;

	private ModelController modelController;
	private ModelAnimationController animationController;

	public ModelViewerRenderer(final Context context, final ModelViewerFragment modelViewerFragment) {
		super (context);
		setFrameRate(60);
		fragment = modelViewerFragment;
		modelController = new ModelController(this, fragment);
		animationController = new ModelAnimationController(this);
	}

	@Override
	public void onSurfaceCreated(final GL10 gl, final EGLConfig config) {
		fragment.showLoader();
		super.onSurfaceCreated(gl, config);
	}

	@Override
	public void onDrawFrame(final GL10 glUnused) {
		super.onDrawFrame(glUnused);
		fragment.hideLoader();
	}

	@Override
	protected void initScene() {

		directionalLight = new DirectionalLight();
		directionalLight.setPosition(0, 0, 4);
		directionalLight.setPower(1);

		getCurrentScene().addLight(directionalLight);
		getCurrentCamera().setZ(16);

		LoaderOBJ objParser = new LoaderOBJ(this, R.raw.cube_obj);

		try {
			objParser.parse();
			objectGroup = objParser.getParsedObject();

			Log.d(TAG, "Number of Objects: " + objectGroup.getNumObjects());
			for (int i = 0; i < objectGroup.getNumObjects(); i++) {
				Log.d(TAG, "Object " + i + ": " + objectGroup.getChildAt(i).getName());
			}

			addChild(objectGroup);
			modelController.setObjectGroup(objectGroup);
			animationController.setObject(objectGroup);

		} catch (ParsingException e) {
			e.printStackTrace();
		}

		fragment.onObjectLoaded();
	}

	public ModelController getController() {
		return modelController;
	}

	public ModelAnimationController getAnimator() {
		return animationController;
	}
}
