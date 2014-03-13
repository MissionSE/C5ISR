package com.missionse.kestrelweather.reports.filter;

import android.widget.Filter;

import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.reports.ReportAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Filter wrapper class, meant to filter on the Report Database by deferring the actual filtering to other classes.
 */
public class ReportListFilter extends Filter {

	private List<Runnable> mOnFilterRunnables;
	private ReportAdapter mReportAdapter;

	private CharSequence mReportTitleConstraint;
	private CharSequence mSyncStatusConstraint;

	/**
	 * Creates a new ReportListFilter.
	 * @param adapter the adapter who owns the data set that will be filtered
	 */
	public ReportListFilter(final ReportAdapter adapter) {
		super();

		mReportAdapter = adapter;
		mOnFilterRunnables = new ArrayList<Runnable>();

		mReportTitleConstraint = "";
		mSyncStatusConstraint = "synced,unsynced";
	}

	/**
	 * Helper function to wrap filter(), as this is now a pseudo-filter.
	 */
	public void filter() {
		super.filter("");
	}

	/**
	 * Adds a runnable to be executed when the list is actively filtered.
	 * @param runnable runnable to be executed
	 */
	public void addOnFilterRunnable(final Runnable runnable) {
		mOnFilterRunnables.add(runnable);
	}

	/**
	 * Sets the constraint to be used when filtering report titles.
	 * @param constraint the constraint
	 */
	public void setReportTitleConstraint(final CharSequence constraint) {
		mReportTitleConstraint = constraint;
	}

	/**
	 * Retrieves the report title constraint.
	 * @return the constraint
	 */
	public CharSequence getReportTitleConstraint() {
		return mReportTitleConstraint;
	}

	/**
	 * Sets the constraint to be used when filtering sync status.
	 * @param constraint the constraint
	 */
	public void setSyncStatusConstraint(final CharSequence constraint) {
		mSyncStatusConstraint = constraint;
	}

	/**
	 * Retrieves the sync status constraint.
	 * @return the constraint
	 */
	public CharSequence getSyncStatusConstraint() {
		return mSyncStatusConstraint;
	}

	@Override
	protected FilterResults performFiltering(final CharSequence constraint) {
		FilterResults results = new FilterResults();

		ArrayList<Report> originalReports = new ArrayList<Report>();
		ArrayList<Report> allReports = mReportAdapter.getAllReports();
		for (final Report report : allReports) {
			if (!report.isDraft()) {
				originalReports.add(report);
			}
		}

		List<Report> filteredReports = ReportTitleFilter.performFiltering(originalReports, mReportTitleConstraint);
		filteredReports = SyncStatusFilter.performFiltering(filteredReports, mSyncStatusConstraint);

		results.count = filteredReports.size();
		results.values = filteredReports;
		return results;
	}

	@Override
	protected void publishResults(final CharSequence charSequence, final FilterResults filterResults) {
		@SuppressWarnings("unchecked")
		ArrayList<Report> filteredReports = (ArrayList<Report>) filterResults.values;
		mReportAdapter.notifyDataSetChanged();
		mReportAdapter.clear();
		if (filteredReports != null) {
			for (final Report report : filteredReports) {
				mReportAdapter.add(report);
			}
			mReportAdapter.notifyDataSetInvalidated();
		}

		for (final Runnable runnable : mOnFilterRunnables) {
			runnable.run();
		}
	}
}
