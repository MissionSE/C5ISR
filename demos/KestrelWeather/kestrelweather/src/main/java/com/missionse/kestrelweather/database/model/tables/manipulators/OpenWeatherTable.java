package com.missionse.kestrelweather.database.model.tables.manipulators;

import com.j256.ormlite.support.ConnectionSource;
import com.missionse.kestrelweather.database.model.tables.OpenWeather;

import java.sql.SQLException;

/**
 * Created by rvieras on 2/25/14.
 */
public class OpenWeatherTable extends BaseTable<OpenWeather> {
	public static final String TAG = OpenWeatherTable.class.getSimpleName();

	/**
	 * Constructor.
	 *
	 * @param connectionSource The database source connection.
	 * @throws java.sql.SQLException Thrown if any issues with connection.
	 */
	public OpenWeatherTable(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, OpenWeather.class);
	}
}
