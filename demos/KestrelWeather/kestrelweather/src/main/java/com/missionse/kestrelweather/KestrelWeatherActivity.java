package com.missionse.kestrelweather;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.missionse.kestrelweather.drawer.KestrelWeatherDrawerFactory;
import com.missionse.kestrelweather.kestrel.KestrelConnectorFragment;
import com.missionse.kestrelweather.kestrel.KestrelSimulationSettingsFragment;
import com.missionse.kestrelweather.kestrel.KestrelSimulator;
import com.missionse.kestrelweather.map.MapViewerFragment;
import com.missionse.kestrelweather.map.TileProviderFactory;
import com.missionse.kestrelweather.map.TiledMap;
import com.missionse.kestrelweather.reports.photos.PhotoOverviewFragment;
import com.missionse.uiextensions.navigationdrawer.DrawerActivity;
import com.missionse.uiextensions.navigationdrawer.configuration.DrawerConfigurationContainer;

/**
 * Main activity for the Kestrel Weather application.
 */
public class KestrelWeatherActivity extends DrawerActivity {

	private KestrelWeatherDrawerFactory mDrawerFactory;
	private TiledMap mTiledMap;
	private KestrelSimulator mKestrelSimulator;

	/**
	 * Constructor.
	 */
	public KestrelWeatherActivity() {
		mDrawerFactory = new KestrelWeatherDrawerFactory(this);
		mTiledMap = new TiledMap();
		mKestrelSimulator = new KestrelSimulator(this);
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
	protected void onStop() {
		super.onStop();
		mKestrelSimulator.onStop();
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
		PhotoOverviewFragment photoOverviewFragment = (PhotoOverviewFragment) fragmentManager
				.findFragmentByTag("photo_overview");
		if (photoOverviewFragment == null) {
			photoOverviewFragment = new PhotoOverviewFragment();
		}
		fragmentManager.beginTransaction()
				.replace(R.id.content, photoOverviewFragment, "photo_overview")
				.addToBackStack("photo_overview")
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
}
