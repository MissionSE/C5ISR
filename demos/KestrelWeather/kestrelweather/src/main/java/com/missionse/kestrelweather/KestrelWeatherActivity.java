package com.missionse.kestrelweather;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.DatabaseManager;
import com.missionse.kestrelweather.database.model.tables.Note;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.database.model.tables.Supplement;
import com.missionse.kestrelweather.drawer.KestrelWeatherDrawerFactory;
import com.missionse.kestrelweather.kestrel.KestrelConnectorFragment;
import com.missionse.kestrelweather.kestrel.KestrelSimulationSettingsFragment;
import com.missionse.kestrelweather.kestrel.KestrelSimulator;
import com.missionse.kestrelweather.map.MapViewerFragment;
import com.missionse.kestrelweather.map.TileProviderFactory;
import com.missionse.kestrelweather.map.TiledMap;
import com.missionse.kestrelweather.preferences.SettingsActivity;
import com.missionse.kestrelweather.reports.ReportDatabaseFragment;
import com.missionse.kestrelweather.reports.ReportSyncFragment;
import com.missionse.uiextensions.navigationdrawer.DrawerActivity;
import com.missionse.uiextensions.navigationdrawer.configuration.DrawerConfigurationContainer;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.List;

/**
 * Main activity for the Kestrel Weather application.
 */
public class KestrelWeatherActivity extends DrawerActivity {

	private static final String TAG = KestrelWeatherActivity.class.getSimpleName();
	private KestrelWeatherDrawerFactory mDrawerFactory;
	private TiledMap mTiledMap;
	private KestrelSimulator mKestrelSimulator;
	private DatabaseManager mDatabaseManager;
	private Toast mExitToast;
	private TextView mDrawerCountFooter;
	private TextView mDrawerTimestampFooter;

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

		PreferenceManager.setDefaultValues(this, R.xml.pref_units, false);
		PreferenceManager.setDefaultValues(this, R.xml.pref_data_sync, false);

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
		//TODO: Robert - Delete when done testing.
		List<Report> reports = getDatabaseAccessor().getReportTable().queryForAll();
		for (Report report : reports) {
			Log.d(TAG, "Display Report info:\n-------------------------------");
			Log.d(TAG, "id=" + report.getId());
			Log.d(TAG, "remoteId=" + report.getRemoteId());
			Log.d(TAG, "dirty=" + report.isDirty());
			Log.d(TAG, "Listing Notes:");
			for (Note note : report.getNotes()) {
				Log.d(TAG, "  (" + note.getId() + ") Title:" + note.getTitle() + "  Content:" + note.getContent());
			}
			for (Supplement sup : report.getSupplements()) {
				Log.d(TAG, "  (" + sup.getId() + ") Uri:" + sup.getUri() + "  Type:" + sup.getType());
			}
		}
		Log.d(TAG, "Dumping notes table...");
		for (Note note : getDatabaseAccessor().getNoteTable().queryForAll()) {
			StringBuilder builder = new StringBuilder();
			builder.append("\n");
			builder.append("ReportId=" + (note.getReport() == null ? "0" : note.getReport().getId()) + "\n");
			builder.append("Id=" + note.getId() + "\n");
			builder.append("Title=" + note.getTitle() + " Content=" + note.getContent());
			Log.d(TAG, builder.toString());
		}
		Log.d(TAG, "Dumping supplement table...");
		for (Supplement sup : getDatabaseAccessor().getSupplementTable().queryForAll()) {
			Log.d(TAG, "  (" + sup.getId() + ") Uri:" + sup.getUri() + "  Type:" + sup.getType());
		}
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

		mDrawerCountFooter = (TextView) getLeftDrawer().findViewById(R.id.navigation_drawer_footer_count);
		mDrawerTimestampFooter = (TextView) getLeftDrawer().findViewById(R.id.navigation_drawer_footer_time);

		updateDrawerFooterCountInformation();

		selectItem(0, getLeftDrawerList());
	}

	/**
	 * Notifies the activity to of a new unsynced item to reflect in the navigation drawer footer information.
	 */
	public void updateDrawerFooterCountInformation() {
		List<Report> allReports = mDatabaseManager.getReportTable().queryForAll();
		int unsyncedItemCount = 0;
		for (Report report : allReports) {
			if (report.isDirty()) {
				unsyncedItemCount++;
			}
		}

		if (mDrawerCountFooter != null) {
			mDrawerCountFooter.setText(unsyncedItemCount + " " + getResources().getString(R.string.drawer_footer_count));
			if (unsyncedItemCount > 0) {
				mDrawerCountFooter.setTextColor(getResources().getColor(R.color.holo_red_light));
			} else {
				mDrawerCountFooter.setTextColor(getResources().getColor(R.color.gray_medium));
			}
		}
	}

	/**
	 * Notifies the activity of a new sync event, which should be reflected in the navigation drawer.
	 */
	public void updateDrawerFooterTimeInformation() {
		if (mDrawerTimestampFooter != null) {
			mDrawerTimestampFooter.setText(getResources().getString(R.string.drawer_footer_time) + " " +
				DateTimeFormat.forPattern("yyyy-MM-dd [HH:mm:ss]").print(new DateTime()));
		}
	}


	@Override
	protected void onNavigationItemSelected(int index) {
		clearBackStack();
		if (index == KestrelWeatherDrawerFactory.MAP_OVERVIEW) {
			displayMapOverview();
		} else if (index == KestrelWeatherDrawerFactory.CREATE_REPORT) {
			displayCreateReport();
		} else if (index == KestrelWeatherDrawerFactory.REPORT_SYNC) {
			displayReportSync();
		} else if (index == KestrelWeatherDrawerFactory.REPORT_DATABASE) {
			displayReportDatabase();
		}
	}

	private void clearBackStack() {
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
				.commit();
	}

	private void displayReportSync() {
		FragmentManager fragmentManager = getFragmentManager();
		ReportSyncFragment reportSyncFragment = (ReportSyncFragment) fragmentManager
				.findFragmentByTag("report_sync");
		if (reportSyncFragment == null) {
			reportSyncFragment = new ReportSyncFragment();
		}
		fragmentManager.beginTransaction()
				.replace(R.id.content, reportSyncFragment, "report_sync")
				.commit();
	}

	private void displayReportDatabase() {
		FragmentManager fragmentManager = getFragmentManager();
		ReportDatabaseFragment reportDatabaseFragment = (ReportDatabaseFragment) fragmentManager
			.findFragmentByTag("report_database");
		if (reportDatabaseFragment == null) {
			reportDatabaseFragment = new ReportDatabaseFragment();
		}
		fragmentManager.beginTransaction()
			.replace(R.id.content, reportDatabaseFragment, "report_database")
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
			Intent i = new Intent(this, SettingsActivity.class);
			startActivity(i);
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
		if (backStackEntries == 0) {
			if (getLeftDrawerList().getCheckedItemPosition() == 0) {
				if (mExitToast != null && mExitToast.getView().isShown()) {
					finish();
				} else {
					mExitToast = Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT);
					mExitToast.show();
				}
			} else {
				selectItem(0, getLeftDrawerList());
			}
		} else {
			super.onBackPressed();
		}
	}

	/**
	 * Getter.
	 * @return Instance of DatabaseAccessor.
	 */
	public DatabaseAccessor getDatabaseAccessor() {
		return mDatabaseManager;
	}
}
