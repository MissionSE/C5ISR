package com.missionse.kestrelweather.reports.utils;

/**
 * Provides a listener interface to notify of a item removal.
 * @param <T> Type.
 */
public interface ItemRemovedListener<T> {
	/**
	 * Notifies the listener that a item was removed.
	 * @param item The item that was removed.
	 */
	void itemRemoved(final T item);
}
