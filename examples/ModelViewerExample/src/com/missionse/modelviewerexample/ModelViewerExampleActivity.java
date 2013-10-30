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
	private ModelViewerFragment fragment = null;

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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.model_viewer_example, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (fragment != null && fragment.getController() != null) {
			switch (item.getItemId()) {
				case R.id.action_rotate:
					fragment.getController().setAutoRotation(!item.isChecked());
					item.setChecked(fragment.getController().isAutoRotating());
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
					fragment.getController().center();
					return true;
				case R.id.action_reset:
					fragment.getController().reset();
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
