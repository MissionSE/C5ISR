package com.missionse.kestrelweather.database.model.tables.manipulators;

import com.j256.ormlite.support.ConnectionSource;
import com.missionse.kestrelweather.database.model.tables.WeatherData;

import java.sql.SQLException;

/**
 * The DAO associated with weather data table.
 */
public class WeatherDataTable extends BaseTable<WeatherData> {
	private static final String TAG = WeatherDataTable.class.getSimpleName();

	/**
	 * Constructor.
	 * @param connectionSource The database source connection.
	 * @throws SQLException Thrown if any issues with connection.
	 */
	public WeatherDataTable(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, WeatherData.class);
	}
}
