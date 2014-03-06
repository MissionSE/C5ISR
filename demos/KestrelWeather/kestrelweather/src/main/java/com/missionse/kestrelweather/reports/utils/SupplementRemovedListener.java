package com.missionse.kestrelweather.reports.utils;

import com.missionse.kestrelweather.database.model.tables.Supplement;

/**
 * Provides a listener interface to notify of a uri removal.
 */
public interface SupplementRemovedListener {
	/**
	 * Notifies the listener that a supplement was removed.
	 * @param supplement The supplement that was removed.
	 */
	void supplementRemoved(final Supplement supplement);
}
