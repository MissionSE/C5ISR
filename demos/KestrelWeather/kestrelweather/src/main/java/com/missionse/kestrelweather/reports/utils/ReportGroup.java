package com.missionse.kestrelweather.reports.utils;

import com.missionse.kestrelweather.database.model.tables.Report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Provides a grouping of reports.
 */
public class ReportGroup implements Comparable<ReportGroup> {
	private List<Report> mReports;
	private Report mLatestReport;
	private String mTitle;

	/**
	 * Constructor.
	 */
	public ReportGroup() {
		mReports = new ArrayList<Report>();
		mTitle = "";
	}

	/**
	 * Adds a report to the group.
	 * @param report The report to be added.
	 */
	public void add(final Report report) {
		mReports.add(report);
		if (mLatestReport == null || mLatestReport.getCreatedAt().isBefore(report.getCreatedAt())) {
			mLatestReport = report;
			mTitle = mLatestReport.getTitle();
		}

		Collections.sort(mReports, new Comparator<Report>() {
			@Override
			public int compare(final Report report, final Report report2) {
				return -report.getCreatedAt().compareTo(report2.getCreatedAt());
			}
		});
	}

	/**
	 * Removes a report from the group.
	 * @param report The report to be removed.
	 */
	public void remove(final Report report) {
		if (mReports.contains(report)) {
			mReports.remove(report);

			if (mLatestReport.equals(report)) {
				mLatestReport = null;
				mTitle = "";
				for (Report currentReport : mReports) {
					if (mLatestReport == null || mLatestReport.getCreatedAt().isBefore(report.getCreatedAt())) {
						mLatestReport = currentReport;
						mTitle = mLatestReport.getTitle();
					}
				}
			}
		}
	}

	/**
	 * Gets the number of reports in the group.
	 * @return The number reports in the group.
	 */
	public int getCount() {
		return mReports.size();
	}

	/**
	 * Gets the report with the most current timestamp in the group.
	 * @return The latest report.
	 */
	public Report getLatestReport() {
		return mLatestReport;
	}

	/**
	 * Gets the reports in the group.
	 * @return The reports in the group.
	 */
	public List<Report> getReports() {
		return mReports;
	}

	/**
	 * Gets the title of the report group, which is the title of the latest report.
	 * @return The title of the report group.
	 */
	public String getTitle() {
		return mTitle;
	}

	/**
	 * Determines whether the report group is dirty. A group is dirty if any report
	 * in the group is dirty.
	 * @return Whether the report group is dirty.
	 */
	public boolean isDirty() {
		boolean dirty = false;
		for (final Report report : mReports) {
			if (report.isDirty()) {
				dirty = true;
				break;
			}
		}

		return dirty;
	}

	@Override
	public int compareTo(final ReportGroup reportGroup) {
		return mLatestReport.compareTo(reportGroup.getLatestReport());
	}
}
