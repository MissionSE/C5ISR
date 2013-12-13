package com.missionse.logisticsexample.database;


/**
 * Allow fragments access to the database helper.
 */
public interface DatabaseAccessor {

	/**
	 * @return - the {@link com.missionse.logisticsexample.database.DatabaseHelper	}
	 */
	DatabaseHelper getHelper();
}
