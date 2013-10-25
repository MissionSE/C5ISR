package com.missionse.modelviewerexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.missionse.modelviewer.ModelViewerFragment;
import com.missionse.modelviewer.ModelViewerFragmentFactory;

public class ModelViewerExampleActivity extends Activity
{
	private ModelViewerFragment fragment = null;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_model_viewer_example);

		if (savedInstanceState == null)	{
			fragment = ModelViewerFragmentFactory.createObjModelFragment(R.raw.multiobjects_obj);
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
		switch (item.getItemId()) {
			case R.id.action_rotate:
				if (fragment != null)
					fragment.setAutoRotation(!item.isChecked());
				item.setChecked(fragment.isAutoRotating());
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
