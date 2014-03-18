package com.missionse.kestrelweather.database.util;

import android.util.Log;

import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.model.tables.Note;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.database.model.tables.Supplement;

import java.util.List;

/**
 * Util class to help log the contents of the database.
 */
public final class DatabaseLogger {
	private static final String TAG = DatabaseLogger.class.getSimpleName();

	private DatabaseLogger() {
	}

	/**
	 * Log the contents of the report table.
	 * @param accessor Instance of DatabaseAccessor.
	 */
	public static void logReportTable(DatabaseAccessor accessor) {
		Log.d(TAG, "Dumping Report Table....");
		List<Report> reports = accessor.getReportTable().queryForAll();
		for (Report report : reports) {
			Log.d(TAG, "Display Report info:\n------------------------------------------");
			Log.d(TAG, "id=" + report.getId());
			Log.d(TAG, "remoteId=" + report.getRemoteId());
			Log.d(TAG, "dirty=" + report.isDirty());
			Log.d(TAG, "Listing Notes:");
			for (Note note : report.getNotes()) {
				Log.d(TAG, "  (" + note.getId() + ") Title:" + note.getTitle() + "  Content:"
						+ note.getContent());
			}
			Log.d(TAG, "Listing Supplements:");
			for (Supplement supplement : report.getSupplements()) {
				Log.d(TAG, "  (" + supplement.getId() + ") Uri:" + supplement.getUri() + "  RemoteUri: "
						+ supplement.getRemoteUri() + "  Type:" + supplement.getType());
			}
		}
	}

	/**
	 * Log the contents of the Note table.
	 * @param accessor Instance of DatabaseAccessor.
	 */
	public static void logNoteTable(DatabaseAccessor accessor) {
		Log.d(TAG, "Dumping notes table...");
		for (Note note : accessor.getNoteTable().queryForAll()) {
			String noteLog = "\n";
			noteLog += "ReportId = ";
			if (note.getReport() != null) {
				noteLog += note.getReport().getId();
			} else {
				noteLog += "-1";
			}
			noteLog += "Id = " + note.getId() + "\n";
			noteLog += "Title = " + note.getTitle() + ", Content = " + note.getContent();
			Log.d(TAG, noteLog);
		}
	}

	/**
	 * Log the contents of the supplement table.
	 * @param accessor Instance of DatabaseAccessor.
	 */
	public static void logSupplementTable(DatabaseAccessor accessor) {
		Log.d(TAG, "Dumping supplement table...");
		for (Supplement supplement : accessor.getSupplementTable().queryForAll()) {
			Log.d(TAG, "  (" + supplement.getId() + ") Uri:" + supplement.getUri() + "  RemoteUri: "
					+ supplement.getRemoteUri() + "  Type:" + supplement.getType());
		}
	}
}
