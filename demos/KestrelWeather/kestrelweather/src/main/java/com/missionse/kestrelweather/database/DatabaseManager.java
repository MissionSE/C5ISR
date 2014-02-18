package com.missionse.kestrelweather.database;

import android.content.Context;

import com.missionse.kestrelweather.database.local.LocalDatabaseHelper;
import com.missionse.kestrelweather.database.remote.RemoteDatabaseHelper;

/**
 * Manages the connections to the databases for all databases in this
 * application.
 */
public class DatabaseManager {
	private LocalDatabaseHelper mLocalDatabaseHelper;
	private RemoteDatabaseHelper mRemoteDatabaseHelper;

	/**
	 * Constructor.
	 *
	 * @param context The android context associated with this
	 *                application.
	 */
	public DatabaseManager(Context context) {
		mLocalDatabaseHelper = new LocalDatabaseHelper(context);
		mRemoteDatabaseHelper = new RemoteDatabaseHelper(context);
	}

	/**
	 * Getter for local database helper.
	 * @return An instance of LocalDatabaseHelper.
	 */
	public LocalDatabaseHelper getLocalHelper() {
		return mLocalDatabaseHelper;
	}

	/**
	 * Getter for remote database helper.
	 * @return An instance of RemoteDatabaseHelper.
	 */
	public RemoteDatabaseHelper getRemoteHelper() {
		return mRemoteDatabaseHelper;
	}

	/**
	 * Stop all database related processes.
	 */
	public void destroy() {
		mLocalDatabaseHelper = null;
		mRemoteDatabaseHelper = null;
	}
}
