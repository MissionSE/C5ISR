package com.missionse.modelviewerexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.missionse.modelviewer.ModelViewerFragment;

public class ModelViewerExampleActivity extends Activity
{
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_model_viewer_example);

		if (savedInstanceState == null)	{
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putInt(ModelViewerFragment.ARG_MODEL_ID, R.raw.multiobjects_obj);

			ModelViewerFragment fragment = new ModelViewerFragment();
			fragment.setArguments(arguments);
			getFragmentManager().beginTransaction().add(R.id.content_frame, fragment).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu)	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.model_viewer_example, menu);
		return true;
	}
}
