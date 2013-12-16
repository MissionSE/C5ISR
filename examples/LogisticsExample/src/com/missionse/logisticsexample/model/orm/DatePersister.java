package com.missionse.logisticsexample.model.orm;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.DateType;
import com.j256.ormlite.support.DatabaseResults;

/**
 * Persist the Date types.
 */
public final class DatePersister extends DateType {

	private static final DatePersister INSTANCE = new DatePersister();
	@SuppressWarnings("deprecation")
	private static final Timestamp ZERO_TIMESTAMP = new Timestamp(1970, 0, 0, 0, 0, 0, 0);

	private DatePersister() {
		super(SqlType.DATE, new Class<?>[] { Date.class });
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
		Timestamp timestamp = results.getTimestamp(columnPos);
		if (timestamp == null || ZERO_TIMESTAMP.after(timestamp)) {
			return null;
		} else {
			return timestamp.getTime();
		}
	}

	@Override
	public Object sqlArgToJava(final FieldType fieldType, final Object sqlArg, final int columnPos) {
		if (sqlArg == null) {
			return null;
		} else {
			return new Date((Long) sqlArg);
		}
	}
}
