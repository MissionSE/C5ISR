package com.missionse.logisticsexample.database;

/**
 * Provides an API for performing database operations.
 */
public interface DatabaseRequestor {

	/**
	 * Retrieves all of the data from a table in the database.
	 * @param requestCompleteListener The listener that will receive a callback upon completion.
	 */
	void fetchAll(final DatabaseRequestCompletedListener requestCompleteListener);

	/**
	 * Returns whether the database request has been completed.
	 * @return Whether the database request has been completed.
	 */
	boolean hasCompleted();
}
