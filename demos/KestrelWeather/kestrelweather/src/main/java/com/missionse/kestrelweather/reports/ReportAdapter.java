package com.missionse.kestrelweather.reports;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.reports.filter.ReportListFilter;
import com.missionse.kestrelweather.reports.filter.SyncStatusFilter;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Provides an adapter for a list of photos.
 */
public class ReportAdapter extends ArrayAdapter<Report> implements StickyListHeadersAdapter {
	private int mResource;
	private DateTimeFormatter mDateFormatter;

	private ReportListFilter mReportListFilter;

	/**
	 * Constructor.
	 * @param context The current context.
	 * @param resource The resource ID for a layout file containing a View to use when instantiating views.
	 * @param objects The initial objects in the adapter.
	 */
	public ReportAdapter(final Context context, final int resource, final List<Report> objects) {
		super(context, resource, objects);
		mDateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd [HH:mm:ss]");
		mResource = resource;

		mReportListFilter = new ReportListFilter(this);
	}

	/**
	 * Adds a runnable to be executed when the list is actively filtered.
	 * @param runnable runnable to be executed
	 */
	public void addOnFilterRunnable(final Runnable runnable) {
		mReportListFilter.addOnFilterRunnable(runnable);
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(mResource, null);
		}

		if (view != null) {
			Report report = getItem(position);

			TextView reportTitle = (TextView) view.findViewById(R.id.report_detail_title);
			if (reportTitle != null) {

				String reportTitleInLowerCase = report.getTitle().toLowerCase(Locale.getDefault());
				SpannableString spannableReportTitle = new SpannableString(report.getTitle());
				if (isReportTitleBeingFiltered()) {
					if (reportTitleInLowerCase.contains(mReportListFilter.getReportTitleConstraint())) {
						int startOfFoundConstraint = reportTitleInLowerCase
								.indexOf(mReportListFilter.getReportTitleConstraint().toString());
						int endOfFoundConstraint = startOfFoundConstraint + mReportListFilter.getReportTitleConstraint().length();
						spannableReportTitle.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startOfFoundConstraint,
								endOfFoundConstraint, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						spannableReportTitle.setSpan(
								new ForegroundColorSpan(getContext().getResources().getColor(R.color.holo_blue_dark)),
								startOfFoundConstraint, endOfFoundConstraint, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				}
				reportTitle.setText(spannableReportTitle);
			}

			TextView reportTimestamp = (TextView) view.findViewById(R.id.report_detail_timestamp);
			if (reportTimestamp != null) {
				reportTimestamp.setText(mDateFormatter.print(report.getCreatedAt()));
			}

			ImageView reportSyncStatus = (ImageView) view.findViewById(R.id.report_detail_sync_status_icon);
			if (reportSyncStatus != null) {
				if (report.isDirty()) {
					reportSyncStatus.setImageResource(R.drawable.report_status_not_synced);
				} else {
					reportSyncStatus.setImageResource(R.drawable.report_status_synced);
				}
			}
		}

		return view;
	}

	/**
	 * Sets the constraint for report titles.
	 * @param constraint the constraint
	 */
	public void setReportTitleConstraint(final CharSequence constraint) {
		mReportListFilter.setReportTitleConstraint(constraint);
	}

	/**
	 * Sets the constraint for sync statuses.
	 * @param constraint the constraint
	 */
	public void setSyncStatusConstraint(final CharSequence constraint) {
		mReportListFilter.setSyncStatusConstraint(constraint);
	}

	/**
	 * Filters the adapter's underlying structure.
	 */
	public void filter() {
		if (mReportListFilter == null) {
			mReportListFilter = new ReportListFilter(this);
		}

		mReportListFilter.filter();
	}

	/**
	 * Returns whether the report adapter's underlying structure is currently being filtered on report title.
	 * @return whether or not the adapter is currently being filtered
	 */
	public boolean isReportTitleBeingFiltered() {
		return (mReportListFilter.getReportTitleConstraint() != null && mReportListFilter.getReportTitleConstraint().length() > 0);
	}

	/**
	 * Returns whether or not the report adapter's underlying structure is currently being filtered on sync status.
	 * @return whether or not the adapter is currently being filtered
	 */
	public boolean isSyncStatusBeingFiltered() {
		return (!mReportListFilter.getSyncStatusConstraint().equals(SyncStatusFilter.DEFAULT));
	}

	/**
	 * Retrieves all reports held by this adapter.
	 * @return an array list of all reports
	 */
	public ArrayList<Report> getAllReports() {
		ArrayList<Report> allReports = new ArrayList<Report>();
		for (int index = 0; index < getCount(); index++) {
			allReports.add(getItem(index));
		}
		return allReports;
	}

	@Override
	public View getHeaderView(final int position, View convertView, final ViewGroup viewGroup) {
		View headerView;
		LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		headerView = layoutInflater.inflate(R.layout.fragment_report_database_header_view, null);

		if (headerView != null) {
			TextView headerText = (TextView) headerView.findViewById(R.id.fragment_report_database_header);
			if (headerText != null) {
				headerText.setText("" + getItem(position).getTitle().toUpperCase().charAt(0));
			}
		}

		return headerView;
	}

	@Override
	public long getHeaderId(final int position) {
		return getItem(position).getTitle().toUpperCase().charAt(0);
	}
}
