package com.missionse.logisticsexample.database;

/**
 * Provides an API to handle completed database requests.
 */
public interface DatabaseRequestCompletedListener {

	/**
	 * Called upon completion of a database request.
	 */
	void databaseRequestCompleted();
}
