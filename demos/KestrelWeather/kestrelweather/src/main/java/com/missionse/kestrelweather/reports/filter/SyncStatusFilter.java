package com.missionse.kestrelweather.reports.filter;

import com.missionse.kestrelweather.database.model.tables.Report;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Adapter filter that compares constraints to Report titles.
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
	public static List<Report> performFiltering(List<Report> originals, CharSequence constraint) {
		ArrayList<Report> filteredReports = new ArrayList<Report>();
		if (constraint != null && constraint.toString().length() > 0) {
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

			for (Report report : originals) {
				if (report.isDirty() && showUnsyncedReports) {
					filteredReports.add(report);
				} else if (!report.isDirty() && showSyncedReports) {
					filteredReports.add(report);
				}
			}
		} else {
			filteredReports.addAll(originals);
		}
		return filteredReports;
	}
}
