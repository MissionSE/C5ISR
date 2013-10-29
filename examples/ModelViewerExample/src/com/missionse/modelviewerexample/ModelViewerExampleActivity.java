package com.missionse.modelviewerexample;

import java.util.HashMap;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.missionse.modelviewer.ModelViewerFragment;
import com.missionse.modelviewer.ModelViewerFragmentFactory;
import com.missionse.modelviewer.ObjectSelectedListener;

public class ModelViewerExampleActivity extends Activity implements ObjectSelectedListener {
	private ModelViewerFragment fragment = null;

	private HashMap<String, Integer> defaultColors;
	private int highlightColor;

	public ModelViewerExampleActivity() {
		defaultColors = new HashMap<String, Integer>();
		highlightColor = Color.BLUE;
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_model_viewer_example);

		if (savedInstanceState == null)	{
			fragment = ModelViewerFragmentFactory.createObjModelFragment(R.raw.multiobjects_obj);
			fragment.registerObjectSelectedListener(this);

			getFragmentManager().beginTransaction().add(R.id.content_frame, fragment).commit();
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
		if (fragment != null) {
			switch (item.getItemId()) {
				case R.id.action_rotate:
					fragment.setAutoRotation(!item.isChecked());
					item.setChecked(fragment.isAutoRotating());
					return true;
				case R.id.action_lock:
					fragment.setTranslationLocked(!fragment.isTranslationLocked());
					item.setChecked(fragment.isTranslationLocked());
			}
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void objectSelected(final String objectName) {
		int objectColor = fragment.getAmbientColor(objectName);

		if (!defaultColors.containsKey(objectName)) {
			defaultColors.put(objectName, objectColor);
			fragment.setAmbientColor(objectName, highlightColor);
		} else {
			fragment.setAmbientColor(objectName, defaultColors.get(objectName));
			defaultColors.remove(objectName);
		}
	}
}
