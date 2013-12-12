package com.missionse.mapdatabaseexample;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.missionse.mapdatabaseexample.map.DatabaseMap;
import com.missionse.mapdatabaseexample.map.MapLocation;
import com.missionse.mapdatabaseexample.map.MapLocationListener;
import com.missionse.mapdatabaseexample.map.MapViewerFragment;
import com.missionse.mapdatabaseexample.tasks.GetAllLocationsTask;

/**
 * Provides an activity that displays a map fragment.
 */
public class MapDatabaseExampleActivity extends Activity implements MapLocationListener {
	private DatabaseMap mDatabaseMap;
	private Menu mOptionsMenu;

	/**
	 * Constructor.
	 */
	public MapDatabaseExampleActivity() {
		mDatabaseMap = new DatabaseMap(this);
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_database_example);

		FragmentManager fragmentManager = getFragmentManager();
		MapViewerFragment mapViewerFragment = (MapViewerFragment) fragmentManager.findFragmentByTag("map");
		if (mapViewerFragment == null) {
			mapViewerFragment = new MapViewerFragment();
			mapViewerFragment.setMapLoadedListener(mDatabaseMap);
			fragmentManager.beginTransaction().add(R.id.content, mapViewerFragment, "map").commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		mOptionsMenu = menu;
		getMenuInflater().inflate(R.menu.menu_map_database_example, menu);
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

	/**
	 * Shows a progress bar in the menu bar when a database connection is in progress.
	 * @param inProgress Whether the database connection is in progress.
	 */
	public void setDatabaseConnectionInProgress(final boolean inProgress) {
		if (mOptionsMenu != null) {
			final MenuItem refreshItem = mOptionsMenu.findItem(R.id.action_refresh);
			if (refreshItem != null) {
				if (inProgress) {
					refreshItem.setActionView(R.layout.actionbar_database_progress);
				} else {
					refreshItem.setActionView(null);
				}
			}
		}
	}

	@Override
	public void locationReceived(final MapLocation location) {
		mDatabaseMap.addMarker(location);
	}
}
