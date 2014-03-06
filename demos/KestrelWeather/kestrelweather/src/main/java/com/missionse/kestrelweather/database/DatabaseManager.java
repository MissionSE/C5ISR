package com.missionse.kestrelweather.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.missionse.kestrelweather.database.local.LocalDatabaseHelper;
import com.missionse.kestrelweather.database.model.SupplementType;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.database.model.tables.Supplement;
import com.missionse.kestrelweather.database.model.tables.UserSettings;
import com.missionse.kestrelweather.database.model.tables.manipulators.KestrelWeatherTable;
import com.missionse.kestrelweather.database.model.tables.manipulators.NoteTable;
import com.missionse.kestrelweather.database.model.tables.manipulators.OpenWeatherTable;
import com.missionse.kestrelweather.database.model.tables.manipulators.ReportTable;
import com.missionse.kestrelweather.database.model.tables.manipulators.SupplementTable;
import com.missionse.kestrelweather.database.model.tables.manipulators.UserSettingsTable;

import org.joda.time.DateTime;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Manages the connections to the databases for all databases in this
 * application.
 */
public class DatabaseManager implements DatabaseAccessor, DatabaseLifeCycle {
	private LocalDatabaseHelper mLocalDatabaseHelper;
	private Context mContext;

	/**
	 * Constructor.
	 * @param context The android context associated with this
	 * application.
	 */
	public DatabaseManager(Context context) {
		mContext = context;
		mLocalDatabaseHelper = new LocalDatabaseHelper(context);
	}

	@Override
	public ReportTable getReportTable() {
		return mLocalDatabaseHelper.getReportTable();
	}

	@Override
	public SupplementTable getSupplementTable() {
		return mLocalDatabaseHelper.getSupplementTable();
	}

	@Override
	public KestrelWeatherTable getKestrelWeatherTable() {
		return mLocalDatabaseHelper.getKestrelWeatherTable();
	}

	@Override
	public OpenWeatherTable getOpenWeatherTable() {
		return mLocalDatabaseHelper.getOpenWeatherTable();
	}

	@Override
	public NoteTable getNoteTable() {
		return mLocalDatabaseHelper.getNoteTable();
	}

	private UserSettingsTable getUserSettingsTable() {
		return (UserSettingsTable) mLocalDatabaseHelper.getObjectDao(UserSettings.class);
	}

	@Override
	public Report getReportById(int id) {
		return getReportTable().queryForId(id);
	}

	@Override
	public String getUserName() {
		return createUserNameIfNeeded();
	}

	private String createUserNameIfNeeded() {
		String testUuid = UUID.randomUUID().toString();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		String testUserName = prefs.getString("uuid", testUuid);
		if (testUuid.equals(testUserName)) {
			//new user needed
			SharedPreferences.Editor prefEdit = prefs.edit();
			String newUuid = UUID.randomUUID().toString();
			prefEdit.putString("uuid", newUuid).commit();
			return newUuid;
		} else {
			return testUserName;
		}
	}

	@Override
	public String getLatestEvent() {
		return createSettingsIfNeeded().getLatestEvent();
	}

	@Override
	public void setLatestEvent(String latestEvent) {
		UserSettings settings = createSettingsIfNeeded();
		if (latestEvent != null && latestEvent.length() > 0) {
			settings.setLatestEvent(latestEvent);
			getUserSettingsTable().update(settings);
		}
	}

	@Override
	public DateTime getLastSyncedTime() {
		return createSettingsIfNeeded().getLastSynced();
	}

	@Override
	public void setLastSyncedTime(DateTime time) {
		UserSettings settings = createSettingsIfNeeded();
		if (time != null) {
			settings.setLastSynced(time);
			getUserSettingsTable().update(settings);
		}
	}

	private UserSettings createSettingsIfNeeded() {
		UserSettings settings = getUserSettingsTable().queryForId(1);
		if (settings == null) {
			settings = new UserSettings();
			settings.setId(1);
			getUserSettingsTable().create(settings);
		}
		return settings;
	}

	@Override
	public List<Supplement> getPhotoSupplements(final int reportId) {
		return getFilteredSupplements(reportId, SupplementType.PHOTO);
	}

	@Override
	public List<Supplement> getAudioSupplements(final int reportId) {
		return getFilteredSupplements(reportId, SupplementType.AUDIO);
	}

	private List<Supplement> getFilteredSupplements(final int reportId, SupplementType type) {
		List<Supplement> supplements = getSupplementTable().queryForAll();
		List<Supplement> retList = new LinkedList<Supplement>();
		for (Supplement supp : supplements) {
			if (supp.getType() == type &&
					supp.getReport().getId() == reportId) {
				retList.add(supp);
			}
		}
		return retList;
	}

	@Override
	public void onDestroy() {
		mLocalDatabaseHelper = null;
	}

	@Override
	public void onPause() {

	}

	@Override
	public void onResume() {

	}
}
