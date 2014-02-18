package com.missionse.kestrelweather.database.model.tables.manipulators;

import com.j256.ormlite.support.ConnectionSource;
import com.missionse.kestrelweather.database.model.tables.Supplement;

import java.sql.SQLException;

/**
 * The DAO associated with Supplement table.
 */
public class SupplementTable extends BaseTable<Supplement> {
	private static final String TAG = SupplementTable.class.getSimpleName();

	/**
	 * Constructor.
	 *
	 * @param connectionSource The database source connection.
	 * @throws SQLException Thrown if any issues with connection.
	 */
	public SupplementTable(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, Supplement.class);
	}
}
