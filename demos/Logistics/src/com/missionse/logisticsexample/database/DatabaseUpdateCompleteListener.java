package com.missionse.logisticsexample.database;

/**
 * Listener for when the database has been updated.
 */
public interface DatabaseUpdateCompleteListener {

	/**
	 * Called whenever the database has been updated.
	 */
	void onDatabaseUpdateComplete();
}
