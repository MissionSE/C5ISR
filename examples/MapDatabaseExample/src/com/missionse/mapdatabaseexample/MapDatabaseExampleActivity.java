package com.missionse.mapdatabaseexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.missionse.mapdatabaseexample.model.MapLocation;
import com.missionse.mapdatabaseexample.tasks.GetAllLocationsTask;

/**
 * Provides an activity that displays a map fragment.
 */
public class MapDatabaseExample extends Activity implements MapLocationListener {
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

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_refresh:
				new GetAllLocationsTask(this, this).execute();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void locationReceived(final MapLocation location) {
		mMapFragment.addMarker(location);
	}
}
