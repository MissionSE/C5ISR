package com.missionse.databaseexample;

/**
 * To provide access to the database from the fragments. 
 * @author rvieras
 *
 */
public interface IDatabaseAccess {
	/**
	 * @return {@link com.missionse.databaseexample.DatabaseHelper}
	 */
	DatabaseHelper getDBHelper();
}
