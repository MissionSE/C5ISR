package com.missionse.kestrelweather.reports.filter;

import com.missionse.kestrelweather.database.model.tables.Report;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Adapter filter that compares constraints to Report titles.
 */
public final class ReportTitleFilter {

	private ReportTitleFilter() {
	}

	/**
	 * Filters a given list's report titles against a constraint.
	 * @param originals the original list of reports
	 * @param constraint the constraint by which to filter
	 * @return a new list of filtered reports
	 */
	public static List<Report> performFiltering(final List<Report> originals, CharSequence constraint) {
		ArrayList<Report> filteredReports = new ArrayList<Report>();
		if (constraint != null && constraint.toString().length() > 0) {
			constraint = constraint.toString().toLowerCase(Locale.getDefault());

			for (Report report : originals) {
				if (report.getTitle().toLowerCase(Locale.getDefault()).contains(constraint)) {
					filteredReports.add(report);
				}
			}
		} else {
			filteredReports.addAll(originals);
		}
		return filteredReports;
	}
}
