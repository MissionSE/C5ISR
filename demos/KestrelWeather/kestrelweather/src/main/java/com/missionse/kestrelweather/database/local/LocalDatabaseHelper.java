package com.missionse.kestrelweather.database.local;

import android.content.Context;

import com.missionse.kestrelweather.database.SqlLiteOpenHelper;
import com.missionse.kestrelweather.database.model.tables.KestrelWeather;
import com.missionse.kestrelweather.database.model.tables.Note;
import com.missionse.kestrelweather.database.model.tables.OpenWeather;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.database.model.tables.Supplement;
import com.missionse.kestrelweather.database.model.tables.UserSettings;
import com.missionse.kestrelweather.database.model.tables.manipulators.KestrelWeatherTable;
import com.missionse.kestrelweather.database.model.tables.manipulators.NoteTable;
import com.missionse.kestrelweather.database.model.tables.manipulators.OpenWeatherTable;
import com.missionse.kestrelweather.database.model.tables.manipulators.ReportTable;
import com.missionse.kestrelweather.database.model.tables.manipulators.SupplementTable;

import java.util.LinkedList;
import java.util.List;

/**
 * Local database helper.
 */
public class LocalDatabaseHelper extends SqlLiteOpenHelper {
	private static final String DATABASE_NAME = "local.db";
	private static final int DATABASE_VERSION = 1;

	/**
	 * Default constructor.
	 * @param context The context associated with this application.
	 */
	public LocalDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public List<Class> getSupportedClasses() {
		List<Class> ret = new LinkedList<Class>();
		ret.add(Note.class);
		ret.add(OpenWeather.class);
		ret.add(KestrelWeather.class);
		ret.add(Supplement.class);
		ret.add(Report.class);
		ret.add(UserSettings.class);
		return ret;
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
