package com.missionse.kestrelweather.util;

import com.missionse.kestrelweather.database.DatabaseAccessor;
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

import java.util.List;

/**
 * Provides the functionality to remove a report.
 */
public final class ReportRemover {
	private ReportRemover() {
	}

	/**
	 * Removes a report with a specified id from the database.
	 * @param databaseAccessor An accessor to the database.
	 * @param reportId The id of the report to be removed.
	 * @return The number of reports removed from the database.
	 */
	public static int removeReport(final DatabaseAccessor databaseAccessor, int reportId) {
		int reportsRemoved = 0;

		ReportTable reportTable = databaseAccessor.getReportTable();
		Report report = reportTable.queryForId(reportId);
		if (report != null) {
			removeSupplements(databaseAccessor, reportId);

			KestrelWeather kestrelWeather = report.getKestrelWeather();
			if (kestrelWeather != null) {
				KestrelWeatherTable kestrelWeatherTable = databaseAccessor.getKestrelWeatherTable();
				kestrelWeatherTable.delete(kestrelWeather);
			}

			OpenWeather openWeather = report.getOpenWeather();
			if (openWeather != null) {
				OpenWeatherTable openWeatherTable = databaseAccessor.getOpenWeatherTable();
				openWeatherTable.delete(openWeather);
			}

			reportsRemoved = reportTable.delete(report);
		}

		return reportsRemoved;
	}

	/**
	 * Removes a single supplement from the database.
	 * @param databaseAccessor An accessor to the database.
	 * @param supplement The supplement to remove from the database.
	 * @return The number of items removed from the database.
	 */
	public static int removeSupplement(final DatabaseAccessor databaseAccessor, final Supplement supplement) {
		SupplementTable supplementTable = databaseAccessor.getSupplementTable();
		return supplementTable.delete(supplement);
	}

	/**
	 * Removes all supplements for a report.
	 * @param databaseAccessor An accessor to the database.
	 * @param reportId The report associated with the supplements.
	 * @return The number of items removed from the database.
	 */
	public static int removeSupplements(final DatabaseAccessor databaseAccessor, final int reportId) {
		int supplementsRemoved = 0;

		SupplementTable supplementTable = databaseAccessor.getSupplementTable();
		List<Supplement> supplementList = supplementTable.queryForAll();
		for (Supplement supplement : supplementList) {
			if (supplement.getReport() == null || supplement.getReport().getId() == reportId) {
				supplementsRemoved = supplementTable.delete(supplement);
			}
		}

		return supplementsRemoved;
	}

	/**
	 * Removes a single note from the database.
	 * @param databaseAccessor An accessor to the database.
	 * @param note The note to remove from the database.
	 * @return The number of items removed from the database.
	 */
	public static int removeNote(final DatabaseAccessor databaseAccessor, final Note note) {
		NoteTable noteTable = databaseAccessor.getNoteTable();
		return noteTable.delete(note);
	}
}
