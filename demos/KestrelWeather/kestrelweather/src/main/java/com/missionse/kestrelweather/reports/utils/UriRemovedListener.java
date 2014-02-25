package com.missionse.kestrelweather.reports.utils;

import android.net.Uri;

/**
 * Provides a listener interface to notify of a uri removal.
 */
public interface UriRemovedListener {
	/**
	 * Notifies the listener that a uri was removed.
	 * @param uri The uri that was removed.
	 */
	void uriRemoved(final Uri uri);
}
