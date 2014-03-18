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

	/**
	 * Retrieve a list of classes/tables that can be forced dropped.
	 * @return List<Class> that can be forced dropped.
	 */
	public abstract List<Class> getForceDropTables();

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			for (Class clazz : getSupportedClasses()) {
				TableUtils.createTable(connectionSource, clazz);
			}
		} catch (SQLException e) {
			Log.e(TAG, "Cannot create database.", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		try {
			for (Class clazz : getSupportedClasses()) {
				TableUtils.dropTable(connectionSource, clazz, true);
			}
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(TAG, "Cannot drop database.", e);
			throw new RuntimeException(e);
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
	 * Force clear some database tables.
	 */
	public void forceClearTables() {
		for (Class clazz : getForceDropTables()) {
			try {
				TableUtils.clearTable(getConnectionSource(), clazz);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private ReportTable mReportTable;
	private SupplementTable mSupplementTable;
	private KestrelWeatherTable mKestrelWeatherTable;
	private OpenWeatherTable mOpenWeatherTable;
	private NoteTable mNoteTable;

	/**
	 * Getter.
	 * @return Instance of ReportTable.
	 */
	public ReportTable getReportTable() {
		if (mReportTable == null) {
			mReportTable = (ReportTable) getObjectDao(Report.class);
		}
		return mReportTable;
	}

	/**
	 * Getter.
	 * @return Instance of SupplementTable.
	 */
	public SupplementTable getSupplementTable() {
		if (mSupplementTable == null) {
			mSupplementTable = (SupplementTable) getObjectDao(Supplement.class);
		}
		return mSupplementTable;
	}

	/**
	 * Getter.
	 * @return Instance of KestrelWeather.
	 */
	public KestrelWeatherTable getKestrelWeatherTable() {
		if (mKestrelWeatherTable == null) {
			mKestrelWeatherTable = (KestrelWeatherTable) getObjectDao(KestrelWeather.class);
		}
		return mKestrelWeatherTable;
	}

	/**
	 * Getter.
	 * @return Instance of OpenWeatherTable.
	 */
	public OpenWeatherTable getOpenWeatherTable() {
		if (mOpenWeatherTable == null) {
			mOpenWeatherTable = (OpenWeatherTable) getObjectDao(OpenWeather.class);
		}
		return mOpenWeatherTable;
	}

	/**
	 * Getter.
	 * @return Instance of NoteTable.
	 */
	public NoteTable getNoteTable() {
		if (mNoteTable == null) {
			mNoteTable = (NoteTable) getObjectDao(Note.class);
		}
		return mNoteTable;
	}
}
