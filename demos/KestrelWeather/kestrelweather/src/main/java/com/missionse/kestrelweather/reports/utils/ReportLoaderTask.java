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
 * Provides a background task that loads reports into an adapter.
 * Execute takes a boolean parameter: Whether to only add drafts.
 */
public class ReportLoaderTask extends AsyncTask<Boolean, Void, Void> {
	private static final String TAG = ReportLoaderTask.class.getSimpleName();
	private DatabaseAccessor mDatabaseAccessor;
	private ReportAdapter mReportAdapter;
	private ProgressBar mProgressBar;
	private ReportListLoadedListener mReportListLoadedListener;

	private List<Report> mReportList;

	/**
	 * Constructor.
	 * @param databaseAccessor An accessor to the database.
	 * @param reportAdapter The report adapter to populate.
	 * @param progressBar The progress bar used to display progress.
	 */
	public ReportLoaderTask(final DatabaseAccessor databaseAccessor, final ReportAdapter reportAdapter,
			final ProgressBar progressBar) {
		this(databaseAccessor, reportAdapter, progressBar, null);
	}

	/**
	 * Constructor.
	 * @param databaseAccessor An accessor to the database.
	 * @param reportAdapter The report adapter to populate.
	 * @param progressBar The progress bar used to display progress.
	 * @param reportListLoadedListener The listener to be notified upon completion of the task.
	 */
	public ReportLoaderTask(final DatabaseAccessor databaseAccessor, final ReportAdapter reportAdapter,
			final ProgressBar progressBar, final ReportListLoadedListener reportListLoadedListener) {
		mDatabaseAccessor = databaseAccessor;
		mReportAdapter = reportAdapter;
		mProgressBar = progressBar;
		mReportListLoadedListener = reportListLoadedListener;

		mReportList = new ArrayList<Report>();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		if (mProgressBar != null) {
			mProgressBar.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected Void doInBackground(final Boolean... parameters) {
		if (parameters.length == 1) {
			final boolean draftsOnly = parameters[0];
			ReportTable reportTable = mDatabaseAccessor.getReportTable();
			if (reportTable != null) {
				for (Report report : reportTable.queryForAll()) {
					if (draftsOnly && report.isDraft()) {
						mReportList.add(report);
					} else if (!draftsOnly && !report.isDraft()) {
						mReportList.add(report);
					}
				}

				Collections.sort(mReportList);
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

		if (mProgressBar != null) {
			mProgressBar.setVisibility(View.GONE);
		}

		if (mReportListLoadedListener != null) {
			mReportListLoadedListener.reportListLoaded();
		}
	}
}
