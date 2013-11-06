package com.missionse.modelviewerexample;

import java.util.HashMap;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.missionse.modelviewer.ModelAnimationController;
import com.missionse.modelviewer.ModelController;
import com.missionse.modelviewer.ModelViewerFragment;
import com.missionse.modelviewer.ModelViewerFragmentFactory;
import com.missionse.modelviewer.ObjectLoadedListener;
import com.missionse.modelviewer.ObjectPickedListener;

public class ModelViewerExampleActivity extends Activity implements ObjectPickedListener, ObjectLoadedListener {
	private ModelViewerFragment fragment;
	private ModelController controller;
	private ModelAnimationController animator;
	private Menu optionsMenu;

	private HashMap<String, Integer> defaultColors;
	private static final int HIGHLIGHT_COLOR = Color.BLUE;

	public ModelViewerExampleActivity() {
		defaultColors = new HashMap<String, Integer>();
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_model_viewer_example);

		if (savedInstanceState == null)	{
			fragment = ModelViewerFragmentFactory.createObjModelFragment(R.raw.multiobjects_obj);
			fragment.registerObjectPickedListener(this);
			fragment.registerObjectLoadedListener(this);

			getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu)	{
		optionsMenu = menu;
		getMenuInflater().inflate(R.menu.model_viewer_example, optionsMenu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (fragment != null && controller != null && animator != null) {
			switch (item.getItemId()) {
				case R.id.action_rotate:
					if (animator.isRotating()) {
						animator.stopRotation();
					} else {
						animator.startXRotation(4000);
					}
					item.setChecked(animator.isRotating());
					return true;
				case R.id.action_lock:
					if (controller.isTranslationLocked()) {
						controller.unlockTranslation();
					} else {
						controller.lockTranslation();
					}
					item.setChecked(controller.isTranslationLocked());
					return true;
				case R.id.action_center:
					animator.translateTo(0f, 0f, 0f, 250);
					return true;
				case R.id.action_reset:
					animator.rotateTo(0f, 0f, 0f, 250);
					animator.scaleTo(1f, 250);
					animator.translateTo(0f, 0f, 0f, 250);
					optionsMenu.findItem(R.id.action_rotate).setChecked(false);
					return true;
			}
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void objectPicked(final String objectName) {
		int objectColor = controller.getAmbientColor(objectName);

		if (!defaultColors.containsKey(objectName)) {
			defaultColors.put(objectName, objectColor);
			controller.setAmbientColor(objectName, HIGHLIGHT_COLOR);
		} else {
			controller.setAmbientColor(objectName, defaultColors.get(objectName));
			defaultColors.remove(objectName);
		}
	}

	@Override
	public void onObjectLoaded() {
		controller = fragment.getController();
		animator = fragment.getAnimator();
	}
}
