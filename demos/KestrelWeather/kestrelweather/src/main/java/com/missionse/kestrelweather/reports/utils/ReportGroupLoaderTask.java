package com.missionse.kestrelweather.reports.utils;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.database.model.tables.manipulators.ReportTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides a background task that loads report groups into an adapter.
 */
public class ReportGroupLoaderTask extends AsyncTask<Void, Void, Void> {
	private static final String TAG = ReportGroupLoaderTask.class.getSimpleName();
	private DatabaseAccessor mDatabaseAccessor;
	private ReportGroupAdapter mReportGroupAdapter;
	private ProgressBar mProgressBar;
	private ReportListLoadedListener mReportListLoadedListener;

	private List<ReportGroup> mReportGroups;

	/**
	 * Constructor.
	 * @param databaseAccessor An accessor to the database.
	 * @param reportGroupAdapter The report adapter to populate.
	 * @param progressBar The progress bar used to display progress.
	 */
	public ReportGroupLoaderTask(final DatabaseAccessor databaseAccessor, final ReportGroupAdapter reportGroupAdapter,
			final ProgressBar progressBar) {
		this(databaseAccessor, reportGroupAdapter, progressBar, null);
	}

	/**
	 * Constructor.
	 * @param databaseAccessor An accessor to the database.
	 * @param reportGroupAdapter The report adapter to populate.
	 * @param progressBar The progress bar used to display progress.
	 * @param reportListLoadedListener The listener to be notified upon completion of the task.
	 */
	public ReportGroupLoaderTask(final DatabaseAccessor databaseAccessor, final ReportGroupAdapter reportGroupAdapter,
			final ProgressBar progressBar, final ReportListLoadedListener reportListLoadedListener) {
		mDatabaseAccessor = databaseAccessor;
		mReportGroupAdapter = reportGroupAdapter;
		mProgressBar = progressBar;
		mReportListLoadedListener = reportListLoadedListener;

		mReportGroups = new ArrayList<ReportGroup>();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		if (mProgressBar != null) {
			mProgressBar.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected Void doInBackground(final Void... parameters) {
		ReportTable reportTable = mDatabaseAccessor.getReportTable();
		if (reportTable != null) {
			for (Report report : reportTable.queryForAll()) {
				boolean reportFound = false;
				for (ReportGroup reportGroup : mReportGroups) {
					if (reportGroup.getTitle().equals(report.getTitle())) {
						reportGroup.add(report);
						reportFound = true;
						break;
					}
				}
				if (!reportFound) {
					ReportGroup reportGroup = new ReportGroup();
					reportGroup.add(report);
					mReportGroups.add(reportGroup);
				}
			}

			Collections.sort(mReportGroups);
		}
		return null;
	}

	@Override
	protected void onPostExecute(final Void parameter) {
		super.onPostExecute(parameter);
		mReportGroupAdapter.setData(mReportGroups);
		mReportGroupAdapter.notifyDataSetChanged();

		if (mProgressBar != null) {
			mProgressBar.setVisibility(View.GONE);
		}

		if (mReportListLoadedListener != null) {
			mReportListLoadedListener.reportListLoaded();
		}
	}
}
