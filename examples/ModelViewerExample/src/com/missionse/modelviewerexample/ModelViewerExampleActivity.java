package com.missionse.modelviewerexample;

import java.util.HashMap;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.missionse.modelviewer.ModelViewerFragment;
import com.missionse.modelviewer.ModelViewerFragmentFactory;
import com.missionse.modelviewer.ObjectPickedListener;

public class ModelViewerExampleActivity extends Activity implements ObjectPickedListener {
	private ModelViewerFragment fragment;
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
		if (fragment != null && fragment.getController() != null && fragment.getAnimator() != null) {
			switch (item.getItemId()) {
				case R.id.action_rotate:
					if (fragment.getAnimator().isRotating()) {
						fragment.getAnimator().stopRotation();
					} else {
						fragment.getAnimator().startXRotation(4000);
					}
					item.setChecked(fragment.getAnimator().isRotating());
					return true;
				case R.id.action_lock:
					if (fragment.getController().isTranslationLocked()) {
						fragment.getController().unlockTranslation();
					} else {
						fragment.getController().lockTranslation();
					}
					item.setChecked(fragment.getController().isTranslationLocked());
					return true;
				case R.id.action_center:
					fragment.getAnimator().translateTo(0f, 0f, 0f, 250);
					return true;
				case R.id.action_reset:
					fragment.getAnimator().rotateTo(0f, 0f, 0f, 250);
					fragment.getAnimator().scaleTo(1f, 250);
					fragment.getAnimator().translateTo(0f, 0f, 0f, 250);
					optionsMenu.findItem(R.id.action_rotate).setChecked(false);
					return true;
			}
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void objectPicked(final String objectName) {
		int objectColor = fragment.getController().getAmbientColor(objectName);

		if (!defaultColors.containsKey(objectName)) {
			defaultColors.put(objectName, objectColor);
			fragment.getController().setAmbientColor(objectName, HIGHLIGHT_COLOR);
		} else {
			fragment.getController().setAmbientColor(objectName, defaultColors.get(objectName));
			defaultColors.remove(objectName);
		}
	}
}
