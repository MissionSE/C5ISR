package com.missionse.kestrelweather.reports;

import android.os.AsyncTask;

import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.database.model.tables.manipulators.ReportTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a background task that loads reports into an adapter.
 * Execute takes two boolean parameters:
 * 1. Whether to only add drafts.
 */
public class ReportLoaderTask extends AsyncTask<Boolean, Void, Void> {
	private DatabaseAccessor mDatabaseAccessor;
	private ReportAdapter mReportAdapter;
	private List<Report> mReportList;

	/**
	 * Constructor.
	 * @param databaseAccessor An accessor to the database.
	 * @param reportAdapter The report adapter to populate.
	 */
	public ReportLoaderTask(final DatabaseAccessor databaseAccessor, final ReportAdapter reportAdapter) {
		mDatabaseAccessor = databaseAccessor;
		mReportAdapter = reportAdapter;
		mReportList = new ArrayList<Report>();
	}

	@Override
	protected Void doInBackground(final Boolean... parameters) {
		if (parameters.length == 1) {
			final boolean draftsOnly = parameters[0];

			ReportTable reportTable = mDatabaseAccessor.getReportTable();
			for (Report report : reportTable.queryForAll()) {
				if (draftsOnly && report.isDraft()) {
					mReportList.add(report);
				} else if (!draftsOnly) {
					mReportList.add(report);
				}
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(final Void parameter) {
		super.onPostExecute(parameter);
		mReportAdapter.clear();
		mReportAdapter.addAll(mReportList);
		mReportAdapter.notifyDataSetChanged();
	}
}
