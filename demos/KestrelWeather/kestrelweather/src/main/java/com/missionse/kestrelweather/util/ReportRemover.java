package com.missionse.kestrelweather.util;

import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.model.tables.KestrelWeather;
import com.missionse.kestrelweather.database.model.tables.OpenWeather;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.database.model.tables.Supplement;
import com.missionse.kestrelweather.database.model.tables.manipulators.KestrelWeatherTable;
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

		removeSupplements(databaseAccessor, reportId);

		return reportsRemoved;
	}

	/**
	 * Removes all supplements for a report.
	 * @param databaseAccessor An accessor to the database.
	 * @param reportId The report associated with the supplements.
	 * @return The number of items removed from the database.
	 */
	public static int removeSupplements(DatabaseAccessor databaseAccessor, int reportId) {
		int supplementsRemoved = 0;

		SupplementTable supplementTable = databaseAccessor.getSupplementTable();
		List<Supplement> supplementList = supplementTable.queryForAll();
		for (Supplement supplement : supplementList) {
			if (supplement.getReport().getId() == reportId) {
				supplementsRemoved = supplementTable.delete(supplement);
			}
		}

		return supplementsRemoved;
	}

	/**
	 * Removes all supplements for a report that match a specific URI.
	 * @param databaseAccessor An accessor to the database.
	 * @param uri The local uri of the supplements.
	 * @param reportId The report associated with the supplements.
	 * @return The number of items removed from the database.
	 */
	public static int removeSupplements(DatabaseAccessor databaseAccessor, String uri, int reportId) {
		int itemsRemoved = 0;

		SupplementTable supplementTable = databaseAccessor.getSupplementTable();
		List<Supplement> supplementList = supplementTable.queryForAll();
		for (Supplement supplement : supplementList) {
			if (supplement.getReport().getId() == reportId) {
				if (supplement.getUri().equals(uri)) {
					itemsRemoved = supplementTable.delete(supplement);
				}
			}
		}

		return itemsRemoved;
	}
}
