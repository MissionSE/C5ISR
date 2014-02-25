package com.missionse.kestrelweather.database;

import com.missionse.kestrelweather.database.local.LocalDatabaseHelper;
import com.missionse.kestrelweather.database.model.tables.manipulators.KestrelWeatherTable;
import com.missionse.kestrelweather.database.model.tables.manipulators.NoteTable;
import com.missionse.kestrelweather.database.model.tables.manipulators.OpenWeatherTable;
import com.missionse.kestrelweather.database.model.tables.manipulators.ReportTable;
import com.missionse.kestrelweather.database.model.tables.manipulators.SupplementTable;
import com.missionse.kestrelweather.database.remote.RemoteDatabaseHelper;

/**
 * Accessor functions to the database.
 */
public interface DatabaseAccessor {
	/**
	 * Getter.
	 *
	 * @return Instance of LocalDatabaseHelper.
	 */
	LocalDatabaseHelper getLocalDatabaseHelper();

	/**
	 * Getter.
	 *
	 * @return Instance of RemoteDatabaseHelper.
	 */
	RemoteDatabaseHelper getRemoteDatabaseHelper();

	/**
	 * Getter.
	 *
	 * @return Instance of ReportTable.
	 */
	ReportTable getReportTable();

	/**
	 * Getter.
	 *
	 * @return Instance of SupplementTable.
	 */
	SupplementTable getSupplementTable();

	/**
	 * Getter.
	 *
	 * @return Instance of KestrelWeather.
	 */
	KestrelWeatherTable getKestrelWeatherTable();

	/**
	 * Getter.
	 *
	 * @return Instance of OpenWeatherTable.
	 */
	OpenWeatherTable getOpenWeatherTable();

	/**
	 * Getter.
	 *
	 * @return Instance of NoteTable.
	 */
	NoteTable getNoteTable();
}
