package com.missionse.kestrelweather;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.missionse.kestrelweather.drawer.KestrelWeatherDrawerFactory;
import com.missionse.kestrelweather.map.MapViewerFragment;
import com.missionse.kestrelweather.map.TileProviderFactory;
import com.missionse.kestrelweather.map.TiledMap;
import com.missionse.uiextensions.navigationdrawer.DrawerActivity;
import com.missionse.uiextensions.navigationdrawer.configuration.DrawerConfigurationContainer;

/**
 * Main activity for KestrelWeather.
 */
public class KestrelWeather extends DrawerActivity {

	private KestrelWeatherDrawerFactory mDrawerFactory;
	private TiledMap mTiledMap;

	/**
	 * Constructor.
	 */
	public KestrelWeather() {
		mDrawerFactory = new KestrelWeatherDrawerFactory(this);
		mTiledMap = new TiledMap();
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

		displayMap();
	}

	private void displayMap() {
		FragmentManager fragmentManager = getFragmentManager();
		MapViewerFragment mapViewerFragment = (MapViewerFragment) fragmentManager.findFragmentByTag("map");
		if (mapViewerFragment == null) {
			mapViewerFragment = new MapViewerFragment();
			mapViewerFragment.setMapLoadedListener(mTiledMap);
		}
		fragmentManager.beginTransaction().replace(R.id.content, mapViewerFragment, "map").commit();
	}

	@Override
	protected DrawerConfigurationContainer getDrawerConfigurations() {
		return mDrawerFactory.createDrawers();
	}

	@Override
	protected void onDrawerConfigurationComplete() {
		mDrawerFactory.addNavigationMenuItems(getLeftDrawerAdapter());
	}

	@Override
	protected void onNavigationItemSelected(int i) {

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
		}
		return super.onOptionsItemSelected(item);
	}
}
