package com.missionse.kestrelweather.database.sync;

/**
 * Listener interface when sync status has changed.
 */
public interface SyncStatusListener {
	/**
	 * Called when the database has been synced.
	 */
	void onSyncComplete();

	/**
	 * Called when the database has begin syncing.
	 */
	void onSyncStarted();

	/**
	 * Called when a report has been synced.
	 */
	void onSyncedReport(int reportId);
}
