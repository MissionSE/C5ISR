package com.missionse.kestrelweather.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.missionse.kestrelweather.database.model.tables.KestrelWeather;
import com.missionse.kestrelweather.database.model.tables.Note;
import com.missionse.kestrelweather.database.model.tables.OpenWeather;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.database.model.tables.Supplement;
import com.missionse.kestrelweather.database.model.tables.manipulators.KestrelWeatherTable;
import com.missionse.kestrelweather.database.model.tables.manipulators.NoteTable;
import com.missionse.kestrelweather.database.model.tables.manipulators.OpenWeatherTable;
import com.missionse.kestrelweather.database.model.tables.manipulators.ReportTable;
import com.missionse.kestrelweather.database.model.tables.manipulators.SupplementTable;

import java.sql.SQLException;
import java.util.List;

/**
 * Base class for database helpers.
 */
public abstract class SqlLiteOpenHelper extends OrmLiteSqliteOpenHelper {
	private static final String TAG = SqlLiteOpenHelper.class.getSimpleName();

	/**
	 * Default constructor.
	 * @param context Context.
	 * @param databaseName Database name.
	 * @param factory null.
	 * @param databaseVersion Database version.
	 */
	public SqlLiteOpenHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
		super(context, databaseName, factory, databaseVersion);
	}

	/**
	 * Return a list of supported classes for this database.
	 * @return List<Class>s supported by this database.
	 */
	public abstract List<Class> getSupportedClasses();


	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(TAG, "onCreate");
			for (Class clazz : getSupportedClasses()) {
				TableUtils.createTable(connectionSource, clazz);
			}
		} catch (SQLException exp) {
			Log.e(TAG, "Cannot create database.", exp);
			throw new RuntimeException(exp);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
						  int oldVersion, int newVersion) {
		try {
			Log.i(TAG, "onUpgrade");
			for (Class clazz : getSupportedClasses()) {
				TableUtils.dropTable(connectionSource, clazz, true);
			}
			onCreate(db, connectionSource);
		} catch (SQLException exp) {
			Log.e(TAG, "Cannot drop database.", exp);
			throw new RuntimeException(exp);
		}
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

	/**
	 * Getter.
	 * @return Instance of ReportTable.
	 */
	public ReportTable getReportTable() {
		return (ReportTable) getObjectDao(Report.class);
	}

	/**
	 * Getter.
	 * @return Instance of SupplementTable.
	 */
	public SupplementTable getSupplementTable() {
		return (SupplementTable) getObjectDao(Supplement.class);
	}

	/**
	 * Getter.
	 * @return Instance of KestrelWeather.
	 */
	public KestrelWeatherTable getKestrelWeatherTable() {
		return (KestrelWeatherTable) getObjectDao(KestrelWeather.class);
	}

	/**
	 * Getter.
	 * @return Instance of OpenWeatherTable.
	 */
	public OpenWeatherTable getOpenWeatherTable() {
		return (OpenWeatherTable) getObjectDao(OpenWeather.class);
	}

	/**
	 * Getter.
	 * @return Instance of NoteTable.
	 */
	public NoteTable getNoteTable() {
		return (NoteTable) getObjectDao(Note.class);
	}
}
