package com.missionse.kestrelweather.database.remote;

import android.content.Context;

import com.missionse.kestrelweather.database.SqlLiteOpenHelper;
import com.missionse.kestrelweather.database.model.tables.KestrelWeather;
import com.missionse.kestrelweather.database.model.tables.Note;
import com.missionse.kestrelweather.database.model.tables.OpenWeather;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.database.model.tables.Supplement;

import java.util.LinkedList;
import java.util.List;

/**
 * Remote database helper.
 */
public class RemoteDatabaseHelper extends SqlLiteOpenHelper {
	private static final String DATABASE_NAME = "remote.db";
	private static final int DATABASE_VERSION = 1;

	/**
	 * Constructor.
	 * @param context The application context.
	 */
	public RemoteDatabaseHelper(Context context) {
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
		return ret;
	}
}
