package com.missionse.kestrelweather.util;

import com.missionse.kestrelweather.KestrelWeatherActivity;
import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.model.SupplementType;
import com.missionse.kestrelweather.database.model.tables.KestrelWeather;
import com.missionse.kestrelweather.database.model.tables.OpenWeather;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.database.model.tables.Supplement;
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
	public static int buildReport(final DatabaseAccessor databaseAccessor,
			final KestrelWeather kestrelWeather, final OpenWeather openWeather,
			final double latitude, final double longitude) {
		ReportTable reportTable = databaseAccessor.getReportTable();
		Report report = reportTable.newReport();
		report.setKestrelWeather(kestrelWeather);
		report.setOpenWeather(openWeather);
		report.setUserName(databaseAccessor.getUserName());
		report.setLatitude(latitude);
		report.setLongitude(longitude);

		KestrelWeatherTable weatherTable = databaseAccessor.getKestrelWeatherTable();
		weatherTable.create(kestrelWeather);

		OpenWeatherTable openWeatherTable = databaseAccessor.getOpenWeatherTable();
		openWeatherTable.create(openWeather);

		reportTable.create(report);

		return report.getId();
	}

	/**
	 * Create a Supplement entry into the database.
	 * @param activity Instance of KestrelWeatherActivity.
	 * @param uri The string uri that points too the supplement.
	 * @param reportId The database report id associated with the supplement.
	 * @param type The supplementType.
	 * @return returns the database ID of the newly created supplement.
	 */
	public static int buildSupplement(KestrelWeatherActivity activity, String uri,
			int reportId, SupplementType type) {
		Supplement supp = new Supplement();
		supp.setType(type);
		supp.setUri(uri);
		supp.setReport(activity.getDatabaseAccessor().getReportById(reportId));
		activity.getDatabaseAccessor().getSupplementTable().create(supp);
		return supp.getId();
	}
}
