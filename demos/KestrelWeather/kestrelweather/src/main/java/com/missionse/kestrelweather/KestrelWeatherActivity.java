package com.missionse.kestrelweather;

import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.DatabaseManager;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.database.util.DatabaseLogger;
import com.missionse.kestrelweather.drawer.KestrelWeatherDrawerFactory;
import com.missionse.kestrelweather.kestrel.KestrelConnectorFragment;
import com.missionse.kestrelweather.kestrel.KestrelSimulationSettingsFragment;
import com.missionse.kestrelweather.kestrel.KestrelSimulator;
import com.missionse.kestrelweather.map.MapViewerFragment;
import com.missionse.kestrelweather.map.TileProviderFactory;
import com.missionse.kestrelweather.map.TiledMap;
import com.missionse.kestrelweather.preferences.SettingsActivity;
import com.missionse.kestrelweather.reports.ReportDatabaseFragment;
import com.missionse.kestrelweather.reports.ReportDraftFragment;
import com.missionse.kestrelweather.service.AlarmReceiver;
import com.missionse.kestrelweather.service.SyncService;
import com.missionse.uiextensions.navigationdrawer.DrawerActivity;
import com.missionse.uiextensions.navigationdrawer.configuration.DrawerConfigurationContainer;
import com.missionse.uiextensions.navigationdrawer.entry.DrawerSimpleNumberedItem;

import org.joda.time.format.DateTimeFormat;

import java.util.List;

/**
 * Main activity for the Kestrel Weather application.
 */
public class KestrelWeatherActivity extends DrawerActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

	private static final String TAG = KestrelWeatherActivity.class.getSimpleName();
	private static final boolean LOG_DB = false;

	private static final int MILLIS_PER_MIN = 1000 * 60;
	private static final int MAX_REPORT_COUNT = 99;

	private KestrelWeatherDrawerFactory mDrawerFactory;
	private TiledMap mTiledMap;
	private KestrelSimulator mKestrelSimulator;
	private DatabaseManager mDatabaseManager;
	private Toast mExitToast;
	private TextView mDrawerCountFooter;
	private TextView mDrawerTimestampFooter;
	private SharedPreferences mSharedPreferences;
	private boolean mOnMap = false;

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

		if (LOG_DB) {
			DatabaseLogger.logReportTable(mDatabaseManager);
			DatabaseLogger.logNoteTable(mDatabaseManager);
			DatabaseLogger.logSupplementTable(mDatabaseManager);
		}

		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
		startSyncService();
	}

	@Override
	public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
		if (key.equals(getString(R.string.key_sync_frequency)) ||
				key.equals(getString(R.string.key_sync_enabled))) {
			startSyncService();
		}
	}

	private void startSyncService() {
		Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
		final PendingIntent pendingIntent = PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST_CODE,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);

		boolean syncEnabled = mSharedPreferences.getBoolean(getString(R.string.key_sync_enabled), true);
		if (syncEnabled) {
			float intervalInMinutes = Float.valueOf(mSharedPreferences.getString(getString(R.string.key_sync_frequency),
					String.valueOf(getResources().getInteger(R.integer.default_data_sync_interval))));
			Log.d(TAG, "Starting sync service on an interval of " + intervalInMinutes + " minutes...");
			int intervalInMillis = (int) (intervalInMinutes * MILLIS_PER_MIN);

			alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), intervalInMillis, pendingIntent);

			// Although we specify a "start time" of currentTimeMillis() (which is now) to the AlarmManager,
			// the actual start time with an inexact repeating can be as long as the interval, so we should automatically
			// do a sync when starting.
			Intent syncServiceStart = new Intent(this, SyncService.class);
			startService(syncServiceStart);
		} else {
			Intent syncServiceStop = new Intent(this, SyncService.class);
			stopService(syncServiceStop);
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

		displayHome();

		addDrawerEventListener(new DrawerLayout.DrawerListener() {
			@Override
			public void onDrawerSlide(final View drawerView, final float slideOffset) {
			}

			@Override
			public void onDrawerOpened(final View drawerView) {
				updateDraftCount();
				updateReportTotalCount();
				updateDrawerFooterCountInformation();
				updateDrawerFooterTimeInformation();
			}

			@Override
			public void onDrawerClosed(final View drawerView) {
			}

			@Override
			public void onDrawerStateChanged(final int newState) {
			}
		});
	}

	private void updateDraftCount() {
		for (int index = 0; index < getLeftDrawerAdapter().getCount(); index++) {
			if (getLeftDrawerAdapter().getItem(index).getId() == KestrelWeatherDrawerFactory.REPORT_DRAFT) {
				DrawerSimpleNumberedItem draftItem = (DrawerSimpleNumberedItem) getLeftDrawerAdapter().getItem(index);

				List<Report> allReports = mDatabaseManager.getReportTable().queryForAll();
				int draftReportCount = 0;
				for (Report report : allReports) {
					if (report.isDraft()) {
						draftReportCount++;
					}
				}

				draftItem.setNumber(String.valueOf(draftReportCount));
				getLeftDrawerAdapter().notifyDataSetChanged();
			}
		}
	}

	private void updateReportTotalCount() {
		for (int index = 0; index < getLeftDrawerAdapter().getCount(); index++) {
			if (getLeftDrawerAdapter().getItem(index).getId() == KestrelWeatherDrawerFactory.REPORT_VIEW) {
				DrawerSimpleNumberedItem draftItem = (DrawerSimpleNumberedItem) getLeftDrawerAdapter().getItem(index);

				List<Report> allReports = mDatabaseManager.getReportTable().queryForAll();

				String printableCount;
				if (allReports.size() > MAX_REPORT_COUNT) {
					printableCount = "99+";
				} else {
					printableCount = String.valueOf(allReports.size());
				}

				draftItem.setNumber(printableCount);
				getLeftDrawerAdapter().notifyDataSetChanged();
			}
		}
	}

	private void updateDrawerFooterCountInformation() {
		List<Report> allReports = mDatabaseManager.getReportTable().queryForAll();
		int unsyncedItemCount = 0;
		for (Report report : allReports) {
			if (report.isDirty() && !report.isDraft()) {
				unsyncedItemCount++;
			}
		}

		if (mDrawerCountFooter != null) {
			mDrawerCountFooter.setText(getResources().getQuantityString(R.plurals.drawer_footer_unsynced_count, unsyncedItemCount,
				unsyncedItemCount));
			if (unsyncedItemCount > 0) {
				mDrawerCountFooter.setTextColor(getResources().getColor(R.color.holo_red_light));
			} else {
				mDrawerCountFooter.setTextColor(getResources().getColor(R.color.gray_medium));
			}
		}
	}

	private void updateDrawerFooterTimeInformation() {
		if (mDrawerTimestampFooter != null) {
			mDrawerTimestampFooter.setText(getString(R.string.drawer_footer_time) + " "
				+ DateTimeFormat.forPattern("yyyy-MM-dd [HH:mm:ss]").print(mDatabaseManager.getLastSyncedTime()));
		}
	}


	@Override
	protected void onNavigationItemSelected(int index) {
		clearBackStack();
		if (index == KestrelWeatherDrawerFactory.MAP_OVERVIEW) {
			displayMapOverview();
		} else if (index == KestrelWeatherDrawerFactory.CREATE_REPORT) {
			displayCreateReport();
		} else if (index == KestrelWeatherDrawerFactory.REPORT_DRAFT) {
			displayReportDrafts();
		} else if (index == KestrelWeatherDrawerFactory.REPORT_VIEW) {
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
		mOnMap = true;
	}

	private void displayCreateReport() {
		FragmentManager fragmentManager = getFragmentManager();
		KestrelConnectorFragment kestrelConnectorFragment = (KestrelConnectorFragment) fragmentManager
				.findFragmentByTag("kestrelconnector");
		if (kestrelConnectorFragment == null) {
			kestrelConnectorFragment = new KestrelConnectorFragment();
		}
		if (mOnMap) {
			fragmentManager.beginTransaction()
					.replace(R.id.content, kestrelConnectorFragment, "kestrelconnector")
					.commit();
		} else {
			fragmentManager.beginTransaction()
					.setCustomAnimations(
							R.animator.slide_from_left, R.animator.slide_to_right,
							R.animator.slide_from_left, R.animator.slide_to_right)
					.replace(R.id.content, kestrelConnectorFragment, "kestrelconnector")
					.commit();
		}

		mOnMap = false;
	}

	private void displayReportDrafts() {
		FragmentManager fragmentManager = getFragmentManager();
		ReportDraftFragment reportDraftFragment = (ReportDraftFragment) fragmentManager
				.findFragmentByTag("report_sync");
		if (reportDraftFragment == null) {
			reportDraftFragment = new ReportDraftFragment();
		}
		if (mOnMap) {
			fragmentManager.beginTransaction()
					.replace(R.id.content, reportDraftFragment, "report_sync")
					.commit();
		} else {
			fragmentManager.beginTransaction()
					.setCustomAnimations(
							R.animator.slide_from_left, R.animator.slide_to_right,
							R.animator.slide_from_left, R.animator.slide_to_right)
					.replace(R.id.content, reportDraftFragment, "report_sync")
					.commit();
		}

		mOnMap = false;
	}

	private void displayReportDatabase() {
		FragmentManager fragmentManager = getFragmentManager();
		ReportDatabaseFragment reportDatabaseFragment = (ReportDatabaseFragment) fragmentManager
				.findFragmentByTag("report_database");
		if (reportDatabaseFragment == null) {
			reportDatabaseFragment = new ReportDatabaseFragment();
		}
		if (mOnMap) {
			fragmentManager.beginTransaction()
					.replace(R.id.content, reportDatabaseFragment, "report_database")
					.commit();
		} else {
			fragmentManager.beginTransaction()
					.setCustomAnimations(
							R.animator.slide_from_left, R.animator.slide_to_right,
							R.animator.slide_from_left, R.animator.slide_to_right)
					.replace(R.id.content, reportDatabaseFragment, "report_database")
					.commit();
		}

		mOnMap = false;
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
				displayHome();
			}
		} else {
			super.onBackPressed();
		}
	}

	/**
	 * Gets the database accessor.
	 * @return Instance of DatabaseAccessor.
	 */
	public DatabaseAccessor getDatabaseAccessor() {
		return mDatabaseManager;
	}

	/**
	 * Switches the content frame to the default home fragment.
	 */
	public void displayHome() {
		clearBackStack();
		selectItem(0, getLeftDrawerList());
	}
}
