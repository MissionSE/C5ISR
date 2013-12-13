package com.missionse.logisticsexample;

import java.sql.SQLException;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.missionse.logisticsexample.database.DatabaseHelper;
import com.missionse.logisticsexample.drawer.LogisticsDrawerFactory;
import com.missionse.logisticsexample.map.LogisticsMap;
import com.missionse.logisticsexample.map.MapViewerFragment;
import com.missionse.logisticsexample.model.MyOrder;
import com.missionse.logisticsexample.model.Site;
import com.missionse.uiextensions.navigationdrawer.DrawerActivity;
import com.missionse.uiextensions.navigationdrawer.configuration.DrawerConfigurationContainer;
import com.missionse.uiextensions.navigationdrawer.entry.DrawerComplexItem;

/**
 * Main entry point to the Logistics application. Instantiates the two drawers, and loads the initial fragment into the
 * content space.
 */
public class LogisticsExample extends DrawerActivity {
	private LogisticsDrawerFactory mDrawerFactory;
	private LogisticsMap mLogisticsMap;

	private static final int INITIAL_NOTIF = 300;
	private static int mNotificationCount = INITIAL_NOTIF;
	
	private DatabaseHelper mDbHelper;

	/**
	 * Constructs a new LogisticsExample.
	 */
	public LogisticsExample() {
		mLogisticsMap = new LogisticsMap(this);
		mDrawerFactory = new LogisticsDrawerFactory(this);
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FragmentManager fragmentManager = getFragmentManager();
		MapViewerFragment mapViewerFragment = (MapViewerFragment) fragmentManager.findFragmentByTag("map");
		if (mapViewerFragment == null) {
			mapViewerFragment = new MapViewerFragment();
			mapViewerFragment.setMapLoadedListener(mLogisticsMap);
			fragmentManager.beginTransaction().add(R.id.content, mapViewerFragment, "map").commit();
		}
		mDbHelper = new DatabaseHelper(this);
		mDbHelper.initialize();
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.logistics_example, menu);
		return true;
	}

	@Override
	protected DrawerConfigurationContainer getDrawerConfigurations() {
		return mDrawerFactory.createDrawers();
	}

	@Override
	protected void onDrawerConfigurationComplete() {
		mDrawerFactory.onDrawerConfigurationComplete(this);

		mDrawerFactory.addNavigationMenuItems(getLeftDrawerAdapter(), new OnItemSelectedListener() {
			@Override
			public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {

			}

			@Override
			public void onNothingSelected(final AdapterView<?> parent) {
			}
		});
	}

	@Override
	protected void onNavigationItemSelected(final int id) {
		if (id == LogisticsDrawerFactory.UPDATE_HISTORY) {
			getRightDrawerAdapter().add(
					DrawerComplexItem.create(++mNotificationCount, "Notif " + mNotificationCount,
							"This is a subtitle, with extra detail about this notification.",
							R.drawable.ic_action_error, false));
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
