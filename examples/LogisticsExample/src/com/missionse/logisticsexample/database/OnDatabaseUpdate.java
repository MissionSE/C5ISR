package com.missionse.logisticsexample.database;
/**
 * Listener for when the database has been updated. 
 */
public interface OnDatabaseUpdate {
	
	/**
	 * Called whenever the database has been updated. 
	 * @param helper - the database helper
	 */
	void onDatabaseUpdate(DatabaseHelper helper);

}
