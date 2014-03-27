package com.missionse.kestrelweather.reports.utils;

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
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.reports.filter.ReportGroupFilter;
import com.missionse.kestrelweather.reports.filter.SyncStatusFilter;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Provides an adapter for a list of report groups.
 */
public class ReportGroupAdapter extends ArrayAdapter<ReportGroup> implements StickyListHeadersAdapter, SectionIndexer {
	private int mResource;
	private DateTimeFormatter mDateFormatter;
	private List<ReportGroup> mOriginalReportGroupList;
	private ReportGroupFilter mReportListFilter;
	private String[] mSectionHeaders;

	/**
	 * Constructor.
	 * @param context The current context.
	 * @param resource The resource ID for a layout file containing a View to use when instantiating views.
	 */
	public ReportGroupAdapter(final Context context, final int resource) {
		super(context, resource);
		mDateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd [HH:mm:ss]");
		mResource = resource;

		mReportListFilter = new ReportGroupFilter(this);
		mOriginalReportGroupList = new ArrayList<ReportGroup>();
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
			ReportGroup reportGroup = getItem(position);
			Report report = reportGroup.getLatestReport();

			TextView reportTitle = (TextView) view.findViewById(R.id.report_detail_title);
			if (reportTitle != null) {
				String reportTitleInLowerCase = report.getTitle().toLowerCase(Locale.getDefault());
				SpannableString spannableReportTitle = new SpannableString(report.getTitle());
				if (isReportTitleBeingFiltered()) {
					if (reportTitleInLowerCase.contains(mReportListFilter.getReportTitleConstraint())) {
						int startOfFoundConstraint = reportTitleInLowerCase
								.indexOf(mReportListFilter.getReportTitleConstraint().toString());
						int endOfFoundConstraint = startOfFoundConstraint + mReportListFilter.getReportTitleConstraint().length();
						if (mReportListFilter.getReportTitleConstraint().length() > 0) {
							spannableReportTitle.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startOfFoundConstraint,
									endOfFoundConstraint, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							spannableReportTitle.setSpan(
									new ForegroundColorSpan(getContext().getResources().getColor(R.color.holo_blue_dark)),
									startOfFoundConstraint, endOfFoundConstraint, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						}
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
				if (reportGroup.isDirty()) {
					reportSyncStatus.setVisibility(View.VISIBLE);
				} else {
					reportSyncStatus.setVisibility(View.GONE);
				}
			}

			TextView reportGroupCount = (TextView) view.findViewById(R.id.report_detail_count);
			if (reportGroupCount != null) {
				int count = reportGroup.getCount();
				if (count > 1) {
					reportGroupCount.setVisibility(View.VISIBLE);
					reportGroupCount.setText(Integer.toString(count));
				} else {
					reportGroupCount.setVisibility(View.GONE);
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
			mReportListFilter = new ReportGroupFilter(this);
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
	public List<ReportGroup> getAllReportGroups() {
		return mOriginalReportGroupList;
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

	/**
	 * Sets the back-end data to be utilized by the adapter.
	 * @param collection the collection of data
	 */
	public void setData(Collection<? extends ReportGroup> collection) {
		clear();
		addAll(collection);
		setOriginalReportList(collection);
		determineSectionHeaders();
	}

	private void setOriginalReportList(Collection<? extends ReportGroup> collection) {
		mOriginalReportGroupList.clear();
		mOriginalReportGroupList.addAll(collection);
	}

	private void determineSectionHeaders() {
		String sectionHeadersRaw = "";
		for (int index = 0; index < getCount(); index++) {
			String firstLetter = getItem(index).getTitle().toUpperCase().substring(0, 1);
			if (!sectionHeadersRaw.contains(firstLetter)) {
				sectionHeadersRaw += firstLetter + " ";
			}
		}
		sectionHeadersRaw = sectionHeadersRaw.trim();

		mSectionHeaders = sectionHeadersRaw.split(" ");
	}

	@Override
	public Object[] getSections() {
		return mSectionHeaders;
	}

	@Override
	public int getPositionForSection(final int i) {
		int sectionIndex = i;
		while (true) {
			String sectionFirstLetter = (String) getSections()[sectionIndex];
			for (int index = 0; index < getCount(); index++) {
				String reportTitleFirstLetter = getItem(index).getTitle().toUpperCase().substring(0, 1);
				if (reportTitleFirstLetter.equals(sectionFirstLetter)) {
					return index;
				}
			}
			sectionIndex++;
			if (sectionIndex >= getSections().length) {
				return getCount() - 1;
			}
		}
	}

	@Override
	public int getSectionForPosition(final int i) {
		String firstLetter = getItem(i).getTitle().toUpperCase().substring(0, 1);
		for (int index = 0; index < getSections().length; index++) {
			String sectionString = (String) getSections()[index];
			if (sectionString.equals(firstLetter)) {
				return index;
			}
		}
		return getSections().length - 1;
	}
}
