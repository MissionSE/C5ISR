package com.missionse.kestrelweather.reports.filter;

import com.missionse.kestrelweather.reports.utils.ReportGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Adapter filter that compares constraints to sync status.
 */
public final class SyncStatusFilter {

	public static final String SYNCED = "synced";
	public static final String UNSYNCED = "unsynced";
	public static final String DEFAULT = SYNCED + "," + UNSYNCED;

	private SyncStatusFilter() {
	}

	/**
	 * Filters a given list's sync statuses against a constraint.
	 * @param originals the original list of reports
	 * @param constraint the constraint by which to filter
	 * @return a new list of filtered reports
	 */
	public static List<ReportGroup> performFiltering(final List<ReportGroup> originals, CharSequence constraint) {
		ArrayList<ReportGroup> filteredReports = new ArrayList<ReportGroup>();
		if (constraint != null) {
			constraint = constraint.toString().toLowerCase(Locale.getDefault());

			boolean showSyncedReports = false;
			boolean showUnsyncedReports = false;

			String[] parsedConstraint = constraint.toString().split(",");
			for (final String parsedValue : parsedConstraint) {
				if (parsedValue.equals(SYNCED)) {
					showSyncedReports = true;
				} else if (parsedValue.equals(UNSYNCED)) {
					showUnsyncedReports = true;
				}
			}

			for (final ReportGroup reportGroup : originals) {
				if (reportGroup.isDirty() && showUnsyncedReports) {
					filteredReports.add(reportGroup);
				} else if (!reportGroup.isDirty() && showSyncedReports) {
					filteredReports.add(reportGroup);
				}
			}
		} else {
			filteredReports.addAll(originals);
		}
		return filteredReports;
	}
}
