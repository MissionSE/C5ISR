package com.missionse.kestrelweather;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.DatabaseManager;
import com.missionse.kestrelweather.database.local.LocalDatabaseHelper;
import com.missionse.kestrelweather.database.model.tables.manipulators.KestrelWeatherTable;
import com.missionse.kestrelweather.database.model.tables.manipulators.NoteTable;
import com.missionse.kestrelweather.database.model.tables.manipulators.OpenWeatherTable;
import com.missionse.kestrelweather.database.model.tables.manipulators.ReportTable;
import com.missionse.kestrelweather.database.model.tables.manipulators.SupplementTable;
import com.missionse.kestrelweather.database.remote.RemoteDatabaseHelper;
import com.missionse.kestrelweather.drawer.KestrelWeatherDrawerFactory;
import com.missionse.kestrelweather.kestrel.KestrelConnectorFragment;
import com.missionse.kestrelweather.kestrel.KestrelSimulationSettingsFragment;
import com.missionse.kestrelweather.kestrel.KestrelSimulator;
import com.missionse.kestrelweather.map.MapViewerFragment;
import com.missionse.kestrelweather.map.TileProviderFactory;
import com.missionse.kestrelweather.map.TiledMap;
import com.missionse.kestrelweather.reports.ReportListFragment;
import com.missionse.uiextensions.navigationdrawer.DrawerActivity;
import com.missionse.uiextensions.navigationdrawer.configuration.DrawerConfigurationContainer;

/**
 * Main activity for the Kestrel Weather application.
 */
public class KestrelWeatherActivity extends DrawerActivity implements DatabaseAccessor {

	private static final String TAG = KestrelWeatherActivity.class.getSimpleName();
	private KestrelWeatherDrawerFactory mDrawerFactory;
	private TiledMap mTiledMap;
	private KestrelSimulator mKestrelSimulator;
	private DatabaseManager mDatabaseManager;

	/**
	 * Constructor.
	 */
	public KestrelWeatherActivity() {
		mDrawerFactory = new KestrelWeatherDrawerFactory(this);
		mTiledMap = new TiledMap();
		mKestrelSimulator = new KestrelSimulator(this);
		mDatabaseManager = new DatabaseManager(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTiledMap.addTileProvider(getString(R.string.rain_overlay_name),
				TileProviderFactory.createUrlTileProvider(getString(R.string.rain_overlay_url)));
		mTiledMap.addTileProvider(getString(R.string.cloud_overlay_name),
				TileProviderFactory.createUrlTileProvider(getString(R.string.cloud_overlay_url)));
		mTiledMap.addTileProvider(getString(R.string.snow_overlay_name),
				TileProviderFactory.createUrlTileProvider(getString(R.string.snow_overlay_url)));
		mTiledMap.addTileProvider(getString(R.string.pressure_overlay_name),
				TileProviderFactory.createUrlTileProvider(getString(R.string.pressure_overlay_url)));
		mTiledMap.addTileProvider(getString(R.string.temp_overlay_name),
				TileProviderFactory.createUrlTileProvider(getString(R.string.temp_overlay_url)));
		mTiledMap.addTileProvider(getString(R.string.precipitation_overlay_name),
				TileProviderFactory.createUrlTileProvider(getString(R.string.precipitation_overlay_url)));
		mTiledMap.addTileProvider(getString(R.string.wind_overlay_name),
				TileProviderFactory.createUrlTileProvider(getString(R.string.wind_overlay_url)));

		mKestrelSimulator.onCreate();
	}

	@Override
	protected void onStart() {
		super.onStart();
		mKestrelSimulator.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mKestrelSimulator.onStop();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mDatabaseManager.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mDatabaseManager.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mDatabaseManager.onDestroy();
	}

	@Override
	protected DrawerConfigurationContainer getDrawerConfigurations() {
		return mDrawerFactory.createDrawers();
	}

	@Override
	protected void onDrawerConfigurationComplete() {
		mDrawerFactory.addNavigationMenuItems(getLeftDrawerAdapter());
		selectItem(0, getLeftDrawerList());
	}

	@Override
	protected void onNavigationItemSelected(int index) {
		if (index == KestrelWeatherDrawerFactory.MAP_OVERVIEW) {
			displayMapOverview();
		} else if (index == KestrelWeatherDrawerFactory.CREATE_REPORT) {
			displayCreateReport();
		} else if (index == KestrelWeatherDrawerFactory.REPORT_SYNC) {
			displayReportSync();
		}
	}

	private void displayMapOverview() {
		FragmentManager fragmentManager = getFragmentManager();
		MapViewerFragment mapViewerFragment = (MapViewerFragment) fragmentManager
				.findFragmentByTag("map");
		if (mapViewerFragment == null) {
			mapViewerFragment = new MapViewerFragment();
			mapViewerFragment.setMapLoadedListener(mTiledMap);
		}
		fragmentManager.beginTransaction()
				.replace(R.id.content, mapViewerFragment, "map")
				.addToBackStack("map")
				.commit();
	}

	private void displayCreateReport() {
		FragmentManager fragmentManager = getFragmentManager();
		KestrelConnectorFragment kestrelConnectorFragment = (KestrelConnectorFragment) fragmentManager
				.findFragmentByTag("kestrelconnector");
		if (kestrelConnectorFragment == null) {
			kestrelConnectorFragment = new KestrelConnectorFragment();
		}
		fragmentManager.beginTransaction()
				.replace(R.id.content, kestrelConnectorFragment, "kestrelconnector")
				.addToBackStack("kestrelconnector")
				.commit();
	}

	private void displayReportSync() {
		FragmentManager fragmentManager = getFragmentManager();
		ReportListFragment reportListFragment = (ReportListFragment) fragmentManager
				.findFragmentByTag("report_list");
		if (reportListFragment == null) {
			reportListFragment = new ReportListFragment();
		}
		fragmentManager.beginTransaction()
				.replace(R.id.content, reportListFragment, "report_list")
				.addToBackStack("report_list")
				.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.kestrel_weather, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		} else if (id == R.id.action_rain
				|| id == R.id.action_cloud
				|| id == R.id.action_snow
				|| id == R.id.action_pressure
				|| id == R.id.action_temp
				|| id == R.id.action_precipitation
				|| id == R.id.action_wind) {
			String overlayName = item.getTitle().toString();
			mTiledMap.setOverlayVisibility(overlayName, !item.isChecked());
			item.setChecked(mTiledMap.isOverlayVisible(overlayName));
			return true;
		} else if (id == R.id.action_simulate_kestrel) {
			if (!item.isChecked()) {
				if (mKestrelSimulator.checkBluetoothAvailability()) {
					mKestrelSimulator.startSimulator();
					item.setChecked(true);
				} else {
					item.setChecked(false);
				}
			} else {
				mKestrelSimulator.stopSimulator();
				item.setChecked(false);
			}
			return true;
		} else if (id == R.id.action_kestrel_simulation_settings) {
			FragmentManager fragmentManager = getFragmentManager();
			KestrelSimulationSettingsFragment kestrelSimulationSettingsFragment = (KestrelSimulationSettingsFragment) fragmentManager
					.findFragmentByTag("kestrelsimulationsettings");
			if (kestrelSimulationSettingsFragment == null) {
				kestrelSimulationSettingsFragment = new KestrelSimulationSettingsFragment();
			}
			fragmentManager.beginTransaction().replace(R.id.content, kestrelSimulationSettingsFragment, "kestrelsimulationsettings")
					.addToBackStack("kestrelsimulationsettings").commit();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		int backStackEntries = getFragmentManager().getBackStackEntryCount();
		if (backStackEntries == 1) {
			finish();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public LocalDatabaseHelper getLocalDatabaseHelper() {
		return mDatabaseManager.getLocalDatabaseHelper();
	}

	@Override
	public RemoteDatabaseHelper getRemoteDatabaseHelper() {
		return mDatabaseManager.getRemoteDatabaseHelper();
	}

	@Override
	public ReportTable getReportTable() {
		return mDatabaseManager.getReportTable();
	}

	@Override
	public SupplementTable getSupplementTable() {
		return mDatabaseManager.getSupplementTable();
	}

	@Override
	public KestrelWeatherTable getKestrelWeatherTable() {
		return mDatabaseManager.getKestrelWeatherTable();
	}

	@Override
	public OpenWeatherTable getOpenWeatherTable() {
		return mDatabaseManager.getOpenWeatherTable();
	}

	@Override
	public NoteTable getNoteTable() {
		return mDatabaseManager.getNoteTable();
	}
}
