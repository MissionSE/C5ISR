package com.missionse.kestrelweather.database.model.tables.manipulators;

import com.j256.ormlite.support.ConnectionSource;
import com.missionse.kestrelweather.database.model.tables.UserSettings;

import java.sql.SQLException;

/**
 *	The DAO associated with UserSettings object.
 */
public class UserSettingsTable extends BaseTable<UserSettings> {
	/**
	 * Constructor.
	 * @param connectionSource The database source connection.
	 * @throws java.sql.SQLException Thrown if any issues with connection.
	 */
	public UserSettingsTable(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, UserSettings.class);
	}
}
