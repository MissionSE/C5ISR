package com.missionse.kestrelweather.database;

/**
 * Created by rvieras on 2/25/14.
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
