package com.missionse.logisticsexample.model.orm;

import java.sql.SQLException;

import org.joda.time.DateTime;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.DateType;
import com.j256.ormlite.support.DatabaseResults;

/**
 * Persist the Date types.
 */
public final class DatePersister extends DateType {

	private static final DatePersister INSTANCE = new DatePersister();
	private static final DateTime ZERO_TIMESTAMP = new DateTime(0);

	private DatePersister() {
		super(SqlType.DATE, new Class<?>[] { DateTime.class });
	}

	/**
	 * Get access to the date persister.
	 * @return the date persister
	 */
	public static DatePersister getSingleton() {
		return INSTANCE;
	}

	@Override
	public Object resultToSqlArg(final FieldType fieldType, final DatabaseResults results, final int columnPos)
			throws SQLException {
		DateTime timestamp = new DateTime(results.getTimestamp(columnPos));
		if (timestamp == null || ZERO_TIMESTAMP.isAfter(timestamp)) {
			return null;
		} else {
			return timestamp;
		}
	}

	@Override
	public Object sqlArgToJava(final FieldType fieldType, final Object sqlArg, final int columnPos) {
		if (sqlArg == null) {
			return null;
		} else {
			return new DateTime(sqlArg);
		}
	}
}
