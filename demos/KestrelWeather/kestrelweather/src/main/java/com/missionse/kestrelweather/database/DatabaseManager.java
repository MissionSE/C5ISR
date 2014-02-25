package com.missionse.kestrelweather.database;

import android.content.Context;

import com.missionse.kestrelweather.database.local.LocalDatabaseHelper;
import com.missionse.kestrelweather.database.model.tables.manipulators.KestrelWeatherTable;
import com.missionse.kestrelweather.database.model.tables.manipulators.NoteTable;
import com.missionse.kestrelweather.database.model.tables.manipulators.OpenWeatherTable;
import com.missionse.kestrelweather.database.model.tables.manipulators.ReportTable;
import com.missionse.kestrelweather.database.model.tables.manipulators.SupplementTable;
import com.missionse.kestrelweather.database.remote.RemoteDatabaseHelper;

/**
 * Manages the connections to the databases for all databases in this
 * application.
 */
public class DatabaseManager implements DatabaseAccessor, DatabaseLifeCycle {
	private LocalDatabaseHelper mLocalDatabaseHelper;
	private RemoteDatabaseHelper mRemoteDatabaseHelper;

	/**
	 * Constructor.
	 * @param context The android context associated with this
	 * application.
	 */
	public DatabaseManager(Context context) {
		mLocalDatabaseHelper = new LocalDatabaseHelper(context);
		mRemoteDatabaseHelper = new RemoteDatabaseHelper(context);
	}

	@Override
	public LocalDatabaseHelper getLocalDatabaseHelper() {
		return mLocalDatabaseHelper;
	}

	@Override
	public RemoteDatabaseHelper getRemoteDatabaseHelper() {
		return mRemoteDatabaseHelper;
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

	@Override
	public void onDestroy() {
		mLocalDatabaseHelper = null;
		mRemoteDatabaseHelper = null;
	}

	@Override
	public void onPause() {

	}

	@Override
	public void onResume() {

	}
}
