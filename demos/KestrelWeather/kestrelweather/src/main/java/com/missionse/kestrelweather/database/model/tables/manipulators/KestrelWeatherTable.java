package com.missionse.kestrelweather.database.model.tables.manipulators;

import com.j256.ormlite.support.ConnectionSource;
import com.missionse.kestrelweather.database.model.tables.KestrelWeather;

import java.sql.SQLException;

/**
 * The DAO associated with weather data table.
 */
public class KestrelWeatherTable extends BaseTable<KestrelWeather> {
	//private static final String TAG = KestrelWeatherTable.class.getSimpleName();

	/**
	 * Constructor.
	 * @param connectionSource The database source connection.
	 * @throws SQLException Thrown if any issues with connection.
	 */
	public KestrelWeatherTable(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, KestrelWeather.class);
	}
}
