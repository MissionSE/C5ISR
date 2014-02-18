package com.missionse.kestrelweather.database.remote;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Remote database helper.
 */
public class RemoteDatabaseHelper extends OrmLiteSqliteOpenHelper {
	private static final String TAG = RemoteDatabaseHelper.class.getSimpleName();
	private static final String DATABASE_NAME = "remote.db";
	private static final int DATABASE_VERSION = 1;
	private List<Class> mClasses;

	public RemoteDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		mClasses = new LinkedList<Class>();
	}

	/**
	 * Gets a list of a certain type of object from the local database.
	 *
	 * @param <T>   The type of the object.
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

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(TAG, "onCreate");
			for (Class clazz : mClasses) {
				TableUtils.createTable(connectionSource, clazz);
			}
		} catch (SQLException exp) {
			Log.e(TAG, "Cannot create database.", exp);
			throw new RuntimeException(exp);
		}
	}

	/**
	 * Gets the Dao for an object from the database.
	 *
	 * @param <T>   The type of object to retrieve from the database.
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

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
						  int oldVersion, int newVersion) {
		try {
			Log.i(TAG, "onUpgrade");
			for (Class clazz : mClasses) {
				TableUtils.dropTable(connectionSource, clazz, true);
			}
			onCreate(db, connectionSource);
		} catch (SQLException exp) {
			Log.e(TAG, "Cannot drop database.", exp);
			throw new RuntimeException(exp);
		}
	}


}
