package com.missionse.kestrelweather.database;

/**
 * Life cycle needed byt the database.
 */
public interface DatabaseLifeCycle {
	/**
	 * Destroy any necessary components for cleanup.
	 */
	void onDestroy();

	/**
	 * Pause any necessary components.
	 */
	void onPause();

	/**
	 * Resume any necessary components.
	 */
	void onResume();
}
