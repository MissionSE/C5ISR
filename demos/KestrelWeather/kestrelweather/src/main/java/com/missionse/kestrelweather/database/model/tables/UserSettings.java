package com.missionse.kestrelweather.database.model.tables;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.missionse.kestrelweather.database.model.Entity;

import org.joda.time.DateTime;

/**
 * User UserSettings.
 */
@DatabaseTable(daoClass = com.missionse.kestrelweather.database.model.tables.manipulators.UserSettingsTable.class)
public class UserSettings extends Entity {
	@DatabaseField(columnName = "latest_event")
	private String mLatestEvent = "0";

	@DatabaseField(columnName = "last_synced")
	private DateTime mLastSynced;

	/**
	 * Default constructor. Needed by API.
	 */
	public UserSettings() {
		mLatestEvent = "0";
		mLastSynced = DateTime.now().minusDays(1);
	}

	/**
	 * Getter.
	 * @return Get the latest event id.
	 */
	public String getLatestEvent() {
		return mLatestEvent;
	}

	/**
	 * Setter.
	 * @param latestEvent The latestEvent id.
	 */
	public void setLatestEvent(String latestEvent) {
		mLatestEvent = latestEvent;
	}

	/**
	 * Retrieve the last time the database was synced.
	 * @return DateTime that represents the last time the database was synced.
	 */
	public DateTime getLastSynced() {
		return mLastSynced;
	}

	/**
	 * Setter.
	 * @param lastSynced The latestEvent id.
	 */
	public void setLastSynced(DateTime lastSynced) {
		mLastSynced = lastSynced;
	}
}
