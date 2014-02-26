package com.missionse.kestrelweather.database.model.tables;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.missionse.kestrelweather.database.model.Entity;

/**
 * User UserSettings.
 */
@DatabaseTable(daoClass = com.missionse.kestrelweather.database.model.tables.manipulators.UserSettingsTable.class)
public class UserSettings extends Entity {
	@DatabaseField(columnName = "latest_event")
	private String mLatestEvent = "0";

	/**
	 * Default constructor. Needed by API.
	 */
	public UserSettings() {
		mLatestEvent = "0";
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
}
