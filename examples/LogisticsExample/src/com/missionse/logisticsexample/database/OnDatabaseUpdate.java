package com.missionse.logisticsexample.database;

import android.content.Context;

/**
 * Listener for when the database has been updated. 
 */
public interface OnDatabaseUpdate {
	
	/**
	 * Called whenever the database has been updated. 
	 * @param helper - the database helper
	 */
	void onDatabaseUpdate(DatabaseHelper helper);
	
	/**
	 * @see android.app.Activity#runOnUiThread(Runnable)
	 * @param action - runnable 
	 */
	void runOnUiThread(Runnable action);
	
	/**
	 * @see android.app.Activity#getApplicationContext()
	 * @return {@link android.content.Context}
	 */
	Context getApplicationContext();

}
