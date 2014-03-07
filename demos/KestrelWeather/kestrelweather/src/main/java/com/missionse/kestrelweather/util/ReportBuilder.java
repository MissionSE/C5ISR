package com.missionse.kestrelweather.util;

import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.model.tables.KestrelWeather;
import com.missionse.kestrelweather.database.model.tables.OpenWeather;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.database.model.tables.manipulators.KestrelWeatherTable;
import com.missionse.kestrelweather.database.model.tables.manipulators.OpenWeatherTable;
import com.missionse.kestrelweather.database.model.tables.manipulators.ReportTable;

/**
 * Provides the functionality to build a report.
 */
public final class ReportBuilder {
	private ReportBuilder() {
	}

	/**
	 * Builds a report in the database and returns the id.
	 * @param databaseAccessor Used to access the database.
	 * @param kestrelWeather The kestrel weather data.
	 * @param openWeather The open weather data.
	 * @param latitude The latitude of the location where the report is being generated.
	 * @param longitude The longitude of the location where the report is being generated.
	 * @return The id of the report created.
	 */
	public static Report buildReport(final DatabaseAccessor databaseAccessor,
			final KestrelWeather kestrelWeather, final OpenWeather openWeather,
			final double latitude, final double longitude) {
		ReportTable reportTable = databaseAccessor.getReportTable();
		Report report = reportTable.newReport();
		report.setKestrelWeather(kestrelWeather);
		report.setOpenWeather(openWeather);
		report.setUserName(databaseAccessor.getUserName());
		report.setLatitude(latitude);
		report.setLongitude(longitude);
		report.setDraft(true);
		report.setRead(true);

		String title = String.format("%s, %s", openWeather.getName(), openWeather.getCountry());
		report.setTitle(title);

		KestrelWeatherTable weatherTable = databaseAccessor.getKestrelWeatherTable();
		weatherTable.create(kestrelWeather);

		OpenWeatherTable openWeatherTable = databaseAccessor.getOpenWeatherTable();
		openWeatherTable.create(openWeather);

		reportTable.create(report);

		return report;
	}
}
