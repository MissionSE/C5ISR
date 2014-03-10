package com.missionse.kestrelweather.database;

import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.database.model.tables.Supplement;
import com.missionse.kestrelweather.database.model.tables.manipulators.KestrelWeatherTable;
import com.missionse.kestrelweather.database.model.tables.manipulators.NoteTable;
import com.missionse.kestrelweather.database.model.tables.manipulators.OpenWeatherTable;
import com.missionse.kestrelweather.database.model.tables.manipulators.ReportTable;
import com.missionse.kestrelweather.database.model.tables.manipulators.SupplementTable;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Accessor functions to the database.
 */
public interface DatabaseAccessor {
	/**
	 * Getter.
	 * @return Instance of ReportTable.
	 */
	ReportTable getReportTable();

	/**
	 * Getter.
	 * @return Instance of SupplementTable.
	 */
	SupplementTable getSupplementTable();

	/**
	 * Getter.
	 * @return Instance of KestrelWeather.
	 */
	KestrelWeatherTable getKestrelWeatherTable();

	/**
	 * Getter.
	 * @return Instance of OpenWeatherTable.
	 */
	OpenWeatherTable getOpenWeatherTable();

	/**
	 * Getter.
	 * @return Instance of NoteTable.
	 */
	NoteTable getNoteTable();

	/**
	 * Retrieve a Report object based on the ID given.
	 * @param id The database ID.
	 * @return Instance of Report. Null if id does not exist.
	 */
	Report getReportById(int id);

	/**
	 * Getter.
	 * @return Get the users current name.
	 */
	String getUserName();

	/**
	 * Getter.
	 * @return Get the latest event id.
	 */
	String getLatestEvent();

	/**
	 * Setter.
	 * @param latestEvent The new latestEvent id.
	 */
	void setLatestEvent(String latestEvent);

	/**
	 * Getter.
	 * @param reportId The DB report ID
	 * @return List of supplements that are of type SupplementType.PHOTO.
	 */
	List<Supplement> getPhotoSupplements(int reportId);

	/**
	 * Getter.
	 * @param reportId The DB report ID
	 * @return List of supplements that are of type SupplementType.AUDIO.
	 */
	List<Supplement> getAudioSupplements(int reportId);

	/**
	 * Getter.
	 * @param reportId The DB report ID
	 * @return List of supplements that are of type SupplementType.VIDEO.
	 */
	List<Supplement> getVideoSupplements(int reportId);

	/**
	 * Retrieve the last time the database was synced.
	 * @return DateTime that represents the last time the database was synced.
	 */
	DateTime getLastSyncedTime();

	/**
	 * Set the time the database has been synced.
	 * @param time The DateTime the database has been synced.
	 */
	void setLastSyncedTime(DateTime time);

	/**
	 * Retrieve the number of draft reports in the database.
	 * @return count of reports that are a draft.
	 */
	int getDraftCount();

	/**
	 * Retrieve the number of synced reports.
	 * @return count of reports that have been synced.
	 */
	int getSyncedCount();

	/**
	 * Retrieve the number of un-synced reports.
	 * @return count of reports that have not been synced.
	 */
	int getUnSynedCount();
}
