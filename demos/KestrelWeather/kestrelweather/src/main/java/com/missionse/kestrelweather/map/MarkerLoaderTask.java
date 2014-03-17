package com.missionse.kestrelweather.map;

import android.os.AsyncTask;

import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.database.model.tables.manipulators.ReportTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a background task that loads reports into the marker adapter.
 */
public class MarkerLoaderTask extends AsyncTask<Void, Void, Void> {
	private DatabaseAccessor mDatabaseAccessor;
	private ObservationCalloutMarkersAdapter mMarkersAdapter;

	private List<Report> mReportList;

	/**
	 * Constructor.
	 * @param databaseAccessor An accessor to the database.
	 * @param markersAdapter The report adapter to populate.
	 */
	public MarkerLoaderTask(final DatabaseAccessor databaseAccessor, final ObservationCalloutMarkersAdapter markersAdapter) {
		mDatabaseAccessor = databaseAccessor;
		mMarkersAdapter = markersAdapter;

		mReportList = new ArrayList<Report>();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(final Void... parameters) {
		ReportTable reportTable = mDatabaseAccessor.getReportTable();
		if (reportTable != null) {
			List<Report> reports = reportTable.queryForAll();
			for (Report report : reports) {
				mReportList.add(report);
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(final Void parameter) {
		super.onPostExecute(parameter);
		mMarkersAdapter.setData(mReportList);
	}
}
