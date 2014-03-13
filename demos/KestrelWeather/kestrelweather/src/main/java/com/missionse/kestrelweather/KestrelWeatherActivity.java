package com.missionse.kestrelweather;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.model.TileProvider;
import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.DatabaseManager;
import com.missionse.kestrelweather.database.util.DatabaseLogger;
import com.missionse.kestrelweather.drawer.KestrelWeatherDrawerFactory;
import com.missionse.kestrelweather.kestrel.KestrelConnectorFragment;
import com.missionse.kestrelweather.kestrel.KestrelSimulator;
import com.missionse.kestrelweather.map.MapViewerFragment;
import com.missionse.kestrelweather.map.OpenWeatherOverlayFactory;
import com.missionse.kestrelweather.map.OpenWeatherOverlayOptionsMenu;
import com.missionse.kestrelweather.map.TiledMap;
import com.missionse.kestrelweather.preferences.SettingsActivity;
import com.missionse.kestrelweather.reports.ReportDatabaseFragment;
import com.missionse.kestrelweather.reports.ReportDraftFragment;
import com.missionse.kestrelweather.service.SyncService;
import com.missionse.uiextensions.navigationdrawer.DrawerActivity;
import com.missionse.uiextensions.navigationdrawer.configuration.DrawerConfigurationContainer;
import com.missionse.uiextensions.navigationdrawer.entry.DrawerSimpleNumberedItem;

import org.joda.time.format.DateTimeFormat;

import java.util.Map;

/**
 * Main activity for the Kestrel Weather application.
 */
public class KestrelWeatherActivity extends DrawerActivity implements
		SharedPreferences.OnSharedPreferenceChangeListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	private static final String TAG = KestrelWeatherActivity.class.getSimpleName();
	private static final boolean LOG_DB = false;

	private static final int MILLIS_PER_MIN = 1000 * 60;
	private static final int MAX_REPORT_COUNT = 99;
	private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	private KestrelWeatherDrawerFactory mDrawerFactory;
	private TiledMap mTiledMap;
	private KestrelSimulator mKestrelSimulator;
	private DatabaseManager mDatabaseManager;
	private Toast mExitToast;
	private TextView mDrawerCountFooter;
	private TextView mDrawerTimestampFooter;
	private SharedPreferences mSharedPreferences;
	private int mCurrentNavigationIndex = KestrelWeatherDrawerFactory.MAP_OVERVIEW;
	private LocationClient mLocationClient;
	private String mServerAddress;

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
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PreferenceManager.setDefaultValues(this, R.xml.pref_units, false);
		PreferenceManager.setDefaultValues(this, R.xml.pref_data_sync, false);

		Map<String, TileProvider> tileProviders = OpenWeatherOverlayFactory.getOpenWeatherTileProviders(this);
		for (String tileProvider : tileProviders.keySet()) {
			mTiledMap.addTileProvider(tileProvider, tileProviders.get(tileProvider));
		}

		mKestrelSimulator.onCreate();

		if (LOG_DB) {
			DatabaseLogger.logReportTable(mDatabaseManager);
			DatabaseLogger.logNoteTable(mDatabaseManager);
			DatabaseLogger.logSupplementTable(mDatabaseManager);
		}

		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
		startSyncService();

		mServerAddress = getPreference(R.string.key_server, "");
		if (mServerAddress.equals("")) {
			mServerAddress = getString(R.string.remote_server_development);
			mSharedPreferences.edit().putString(getString(R.string.key_server), mServerAddress);
		}

		mLocationClient = new LocationClient(this, this, this);
	}

	@Override
	public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
		if (key.equals(getString(R.string.key_sync_frequency)) ||
				key.equals(getString(R.string.key_sync_enabled))) {
			startSyncService();
		}


		String simulationModeKey = getString(R.string.key_simulation_mode);
		if (key.equals(simulationModeKey)) {
			if (mSharedPreferences.getBoolean(simulationModeKey, false)) {
				mKestrelSimulator.startSimulator();
			} else {
				mKestrelSimulator.stopSimulator();
			}
		}

		String serverKey = getString(R.string.key_server);
		if (key.equals(serverKey)) {
			String newServerAddress = getPreference(R.string.key_server, getString(R.string.remote_server_development));
			if (!newServerAddress.equals(mServerAddress)) {
				mServerAddress = newServerAddress;

				if (mDatabaseManager != null) {
					stopSyncService();
					mDatabaseManager.clearDataTables();
					startSyncService();
				}
			}
		}
	}

	private void stopSyncService() {
		Intent syncIntent = new Intent(this, SyncService.class);
		boolean sync = PreferenceManager.getDefaultSharedPreferences(this)
				.getBoolean(getString(R.string.key_sync_enabled), false);
		if (sync) {
			Bundle extra = new Bundle();
			extra.putString(SyncService.STOP_REQUEST, SyncService.STOP_REQUEST);
			syncIntent.putExtras(extra);
			startService(syncIntent);
		} else {
			stopService(syncIntent);
		}
	}

	private void startSyncService() {
		Intent intent = new Intent(getApplicationContext(), SyncService.class);
		final PendingIntent pendingIntent = PendingIntent.getService(this, SyncService.REQUEST_CODE, intent, 0);

		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);

		boolean syncEnabled = mSharedPreferences.getBoolean(getString(R.string.key_sync_enabled), true);
		if (syncEnabled) {
			final String defaultSyncFrequency = String.valueOf(getResources().getInteger(R.integer.default_data_sync_interval));
			float intervalInMinutes = Float.valueOf(getPreference(R.string.key_sync_frequency, defaultSyncFrequency));
			Log.d(TAG, "Starting sync service on an interval of " + intervalInMinutes + " minutes...");
			int intervalInMillis = (int) (intervalInMinutes * MILLIS_PER_MIN);

			alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), intervalInMillis, pendingIntent);

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

	private String getPreference(final int key, final String defaultValue) {
		return mSharedPreferences.getString(getString(key), defaultValue);
	}

	@Override
	protected void onStart() {
		super.onStart();
		mKestrelSimulator.onStart();

		if (mSharedPreferences.getBoolean(getString(R.string.key_simulation_mode), false)) {
			mKestrelSimulator.startSimulator();
		}

		if (isGooglePlayServicesAvailable()) {
			mLocationClient.connect();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		mKestrelSimulator.onStop();
		shutdownKestrelSimulator();
		mLocationClient.disconnect();
	}

	private void shutdownKestrelSimulator() {
		mKestrelSimulator.stopSimulator();
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

		clearBackStack();
		selectItemById(mCurrentNavigationIndex, getLeftDrawerList());

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

				int draftReportCount = mDatabaseManager.getDraftCount();
				draftItem.setNumber(String.valueOf(draftReportCount));
				getLeftDrawerAdapter().notifyDataSetChanged();
			}
		}
	}

	private void updateReportTotalCount() {
		for (int index = 0; index < getLeftDrawerAdapter().getCount(); index++) {
			if (getLeftDrawerAdapter().getItem(index).getId() == KestrelWeatherDrawerFactory.REPORT_VIEW) {
				DrawerSimpleNumberedItem draftItem = (DrawerSimpleNumberedItem) getLeftDrawerAdapter().getItem(index);

				int reportSize = mDatabaseManager.getSyncedCount() + mDatabaseManager.getUnSynedCount();
				String printableCount = String.valueOf(reportSize);
				if (reportSize > MAX_REPORT_COUNT) {
					printableCount = "99+";
				}

				draftItem.setNumber(printableCount);
				getLeftDrawerAdapter().notifyDataSetChanged();
			}
		}
	}

	private void updateDrawerFooterCountInformation() {
		int unsyncedItemCount = mDatabaseManager.getUnSynedCount();
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
			mapViewerFragment.setOptionsMenuListener(new OpenWeatherOverlayOptionsMenu(mTiledMap));
		}
		fragmentManager.beginTransaction()
				.setCustomAnimations(
						R.animator.fade_in, R.animator.fade_out,
						R.animator.fade_in, R.animator.fade_out)
				.replace(R.id.content, mapViewerFragment, "map")
				.commit();
		mCurrentNavigationIndex = KestrelWeatherDrawerFactory.MAP_OVERVIEW;
	}

	private void displayCreateReport() {
		FragmentManager fragmentManager = getFragmentManager();
		KestrelConnectorFragment kestrelConnectorFragment = (KestrelConnectorFragment) fragmentManager
				.findFragmentByTag("kestrelconnector");
		if (kestrelConnectorFragment == null) {
			kestrelConnectorFragment = new KestrelConnectorFragment();
		}
		fragmentManager.beginTransaction()
				.setCustomAnimations(
						R.animator.fade_in, R.animator.fade_out,
						R.animator.fade_in, R.animator.fade_out)
				.replace(R.id.content, kestrelConnectorFragment, "kestrelconnector")
				.commit();

		mCurrentNavigationIndex = KestrelWeatherDrawerFactory.CREATE_REPORT;
	}

	private void displayReportDrafts() {
		FragmentManager fragmentManager = getFragmentManager();
		ReportDraftFragment reportDraftFragment = (ReportDraftFragment) fragmentManager
				.findFragmentByTag("report_sync");
		if (reportDraftFragment == null) {
			reportDraftFragment = new ReportDraftFragment();
		}
		fragmentManager.beginTransaction()
				.setCustomAnimations(
						R.animator.fade_in, R.animator.fade_out,
						R.animator.fade_in, R.animator.fade_out)
				.replace(R.id.content, reportDraftFragment, "report_sync")
				.commit();

		mCurrentNavigationIndex = KestrelWeatherDrawerFactory.REPORT_DRAFT;
	}

	private void displayReportDatabase() {
		FragmentManager fragmentManager = getFragmentManager();
		ReportDatabaseFragment reportDatabaseFragment = (ReportDatabaseFragment) fragmentManager
				.findFragmentByTag("report_database");
		if (reportDatabaseFragment == null) {
			reportDatabaseFragment = new ReportDatabaseFragment();
		}
		fragmentManager.beginTransaction()
				.setCustomAnimations(
						R.animator.fade_in, R.animator.fade_out,
						R.animator.fade_in, R.animator.fade_out)
				.replace(R.id.content, reportDatabaseFragment, "report_database")
				.commit();

		mCurrentNavigationIndex = KestrelWeatherDrawerFactory.REPORT_VIEW;
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
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		// Pass the event to the map fragment to see if it should handle the action first
		MapViewerFragment mapViewerFragment = (MapViewerFragment) getFragmentManager()
				.findFragmentByTag("map");
		if (mapViewerFragment != null && mapViewerFragment.isVisible() && mapViewerFragment.onBackPressed()) {
			return;
		}

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
		selectItemById(KestrelWeatherDrawerFactory.MAP_OVERVIEW, getLeftDrawerList());
	}

	/**
	 * Get the last location from the location client.
	 * @return the last location if the
	 */
	public Location getLastLocation() {
		if (mLocationClient.isConnected()) {
			return mLocationClient.getLastLocation();
		}
		return null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CONNECTION_FAILURE_RESOLUTION_REQUEST
				&& resultCode == RESULT_OK) {
			mLocationClient.connect();
		}
	}

	@Override
	public void onConnected(Bundle bundle) {
	}

	@Override
	public void onDisconnected() {
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (connectionResult.hasResolution()) {
			try {
				connectionResult.startResolutionForResult(
						this,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
			} catch (IntentSender.SendIntentException e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(this, "Sorry. Location services not available to you", Toast.LENGTH_LONG).show();
		}
	}

	private boolean isGooglePlayServicesAvailable() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (ConnectionResult.SUCCESS == resultCode) {
			Log.d(TAG, "Google Play services is available.");
			return true;
		} else {
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode,
					this,
					CONNECTION_FAILURE_RESOLUTION_REQUEST);

			if (errorDialog != null) {
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(errorDialog);
				errorFragment.show(getFragmentManager(), "Location Updates");
			}

			return false;
		}
	}

	/**
	 * Define a DialogFragment that displays the error dialog.
	 */
	public static class ErrorDialogFragment extends DialogFragment {

		private Dialog mDialog;

		/**
		 * Default constructor. Sets the dialog field to null.
		 */
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		/**
		 * Set the dialog to display.
		 * @param dialog the dialog
		 */
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}
}
