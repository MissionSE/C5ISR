package com.missionse.kestrelweather.database.model.tables.manipulators;

import android.util.Log;

import com.j256.ormlite.support.ConnectionSource;
import com.missionse.kestrelweather.database.model.tables.Report;

import java.sql.SQLException;

/**
 * The DAO associated with the Report table.
 */
public class ReportTable extends BaseTable<Report> {
	private static final String TAG = ReportTable.class.getSimpleName();

	/**
	 * Constructor.
	 * @param connectionSource The database source connection.
	 * @throws SQLException Thrown if any issues with connection.
	 */
	public ReportTable(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, Report.class);
	}

	/**
	 * Creates a new instance of a Report without adding it into the database.  Using this
	 * will prevent NullPointerException when trying to add Supplements.
	 * @return Instance of Report that has not been added to the database.
	 */
	public Report newReport() {
		Report report = new Report();
		try {
			assignEmptyForeignCollection(report, "mSupplements");
			assignEmptyForeignCollection(report, "mNotes");
		} catch (SQLException exp) {
			Log.e(TAG, "Issue creating new report.", exp);
		}
		return report;
	}
}
