package com.missionse.logisticsexample.database;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.missionse.logisticsexample.R;
import com.missionse.logisticsexample.model.DBEntity;

/**
 * Represents the off-line database.
 */
public class LocalDatabaseAccessor extends OrmLiteSqliteOpenHelper {
	private List<Class<? extends DBEntity>> mDaoClasses;

	/**
	 * Constructor.
	 * @param context {@link android.app.Context}
	 * @param daoClasses The list of classes providing DAOs.
	 */
	public LocalDatabaseAccessor(final Context context, final List<Class<? extends DBEntity>> daoClasses) {
		super(context, context.getString(R.string.local_database_name), null,
				context.getResources().getInteger(R.integer.local_database_version));
		mDaoClasses = daoClasses;
	}

	@Override
	public void onCreate(final SQLiteDatabase database, final ConnectionSource connectionSource) {
		try {
			for (Class<? extends DBEntity> clazz : mDaoClasses) {
				TableUtils.createTable(connectionSource, clazz);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(final SQLiteDatabase db, final ConnectionSource connectionSource,
			final int oldVersion, final int newVersion) {
		try {
			for (Class<? extends DBEntity> clazz : mDaoClasses) {
				TableUtils.dropTable(connectionSource, clazz, true);
			}
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets a list of a certain type of object from the local database.
	 * @param <T> The type of the object.
	 * @param clazz The class of the object.
	 * @return List of type <T> objects from the local database.
	 */
	public <T> List<T> fetchAll(final Class<T> clazz) {
		try {
			return getDao(clazz).queryForAll();
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
		return Collections.emptyList();
	}

	/**
	 * Gets the Dao for an object from the database.
	 * @param <T> The type of object to retrieve from the database.
	 * @param clazz The class representing the object in the database.
	 * @return A Dao object for the requested class if it exists.
	 */
	public <T> Dao<T, Integer> getObjectDao(final Class<T> clazz) {
		Dao<T, Integer> objectDao = null;
		try {
			objectDao = getDao(clazz);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return objectDao;
	}
}
