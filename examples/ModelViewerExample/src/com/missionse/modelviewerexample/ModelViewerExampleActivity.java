package com.missionse.modelviewerexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.missionse.modelviewer.ModelViewerFragment;
import com.missionse.modelviewer.ModelViewerFragmentFactory;

public class ModelViewerExampleActivity extends Activity
{
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_model_viewer_example);

		if (savedInstanceState == null)	{
			ModelViewerFragment fragment = ModelViewerFragmentFactory.createObjModelFragment(R.raw.multiobjects_obj);
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
