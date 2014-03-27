package com.missionse.kestrelweather.reports.filter;

import com.missionse.kestrelweather.reports.utils.ReportGroup;

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
	public static List<ReportGroup> performFiltering(final List<ReportGroup> originals, CharSequence constraint) {
		ArrayList<ReportGroup> filteredReports = new ArrayList<ReportGroup>();
		if (constraint != null && constraint.toString().length() > 0) {
			constraint = constraint.toString().toLowerCase(Locale.getDefault());

			for (final ReportGroup reportGroup : originals) {
				if (reportGroup.getTitle().toLowerCase(Locale.getDefault()).contains(constraint)) {
					filteredReports.add(reportGroup);
				}
			}
		} else {
			filteredReports.addAll(originals);
		}
		return filteredReports;
	}
}
