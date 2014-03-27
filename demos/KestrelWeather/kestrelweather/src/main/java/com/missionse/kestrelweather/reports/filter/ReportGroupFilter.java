package com.missionse.kestrelweather.reports.filter;

import android.widget.Filter;

import com.missionse.kestrelweather.reports.utils.ReportGroup;
import com.missionse.kestrelweather.reports.utils.ReportGroupAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Filter wrapper class, meant to filter on the Report Database by deferring the actual filtering to other classes.
 */
public class ReportGroupFilter extends Filter {

	private List<Runnable> mOnFilterRunnables;
	private ReportGroupAdapter mReportAdapter;

	private CharSequence mReportTitleConstraint;
	private CharSequence mSyncStatusConstraint;

	/**
	 * Creates a new ReportGroupFilter.
	 * @param adapter the adapter who owns the data set that will be filtered
	 */
	public ReportGroupFilter(final ReportGroupAdapter adapter) {
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

		ArrayList<ReportGroup> originalReportGroups = new ArrayList<ReportGroup>();
		List<ReportGroup> allReportGroups = mReportAdapter.getAllReportGroups();
		for (final ReportGroup reportGroup : allReportGroups) {
			if (!reportGroup.getLatestReport().isDraft()) {
				originalReportGroups.add(reportGroup);
			}
		}

		List<ReportGroup> filteredReports = ReportTitleFilter.performFiltering(originalReportGroups, mReportTitleConstraint);
		filteredReports = SyncStatusFilter.performFiltering(filteredReports, mSyncStatusConstraint);

		results.count = filteredReports.size();
		results.values = filteredReports;
		return results;
	}

	@Override
	protected void publishResults(final CharSequence charSequence, final FilterResults filterResults) {
		@SuppressWarnings("unchecked")
		ArrayList<ReportGroup> filteredReports = (ArrayList<ReportGroup>) filterResults.values;
		mReportAdapter.clear();
		if (filteredReports != null) {
			for (final ReportGroup reportGroup : filteredReports) {
				mReportAdapter.add(reportGroup);
			}
			mReportAdapter.notifyDataSetChanged();
		} else {
			mReportAdapter.notifyDataSetInvalidated();
		}

		for (final Runnable runnable : mOnFilterRunnables) {
			runnable.run();
		}
	}
}
