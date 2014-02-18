package com.missionse.kestrelweather.database.model.tables.manipulators;

import android.util.Log;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Base Class for manipulators.
 */
public class BaseTable<T> extends BaseDaoImpl<T, Integer> implements Dao<T, Integer> {
	private static final String TAG = BaseTable.class.getSimpleName();
	private static final int INVALID_DB_ID = -1;

	protected BaseTable(ConnectionSource connectionSource, Class<T> clazz) throws SQLException {
		super(connectionSource, clazz);
	}

	@Override
	public List<T> queryForAll() {
		try {
			return super.queryForAll();
		} catch (SQLException exp) {
			Log.e(TAG, "Unable to query for all reports.", exp);
			return Collections.emptyList();
		}
	}

	@Override
	public int create(T data) {
		try {
			return super.create(data);
		} catch (SQLException exp) {
			Log.e(TAG, "Unable to create report in database.", exp);
			return INVALID_DB_ID;
		}
	}

	@Override
	public int update(T data) {
		try {
			return super.update(data);
		} catch (SQLException exp) {
			Log.e(TAG, "Unable to update report in database.", exp);
			return INVALID_DB_ID;
		}
	}

	@Override
	public int delete(T data) {
		try {
			return super.delete(data);
		} catch (SQLException exp) {
			Log.e(TAG, "Unable to delete report in database.", exp);
			return INVALID_DB_ID;
		}
	}
}
