package com.missionse.mapdatabaseexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

/**
 * Provides an activity that displays a map fragment.
 */
public class MapDatabaseExample extends Activity {
	private GoogleMapFragment mMapFragment;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_database_example);

		mMapFragment = new GoogleMapFragment();
		getFragmentManager().beginTransaction().replace(R.id.content, mMapFragment).commit();
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.map_database_example, menu);
		return true;
	}

}
