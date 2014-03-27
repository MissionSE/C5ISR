package com.missionse.kestrelweather.reports.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.model.tables.Report;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Provides an adapter for a list of reports.
 */
public class ReportAdapter extends ArrayAdapter<Report> {
	private int mResource;
	private DateTimeFormatter mDateFormatter;

	/**
	 * Constructor.
	 * @param context The current context.
	 * @param resource The resource ID for a layout file containing a View to use when instantiating views.
	 */
	public ReportAdapter(final Context context, final int resource) {
		super(context, resource);
		mDateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd [HH:mm:ss]");
		mResource = resource;
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {
		return getCustomView(position, view);
	}

	@Override
	public View getDropDownView(final int position, View view, ViewGroup viewGroup) {
		return getCustomView(position, view);
	}

	private View getCustomView(final int position, View view) {
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(mResource, null);
		}

		if (view != null) {
			Report report = getItem(position);

			TextView reportTitle = (TextView) view.findViewById(R.id.report_detail_title);
			if (reportTitle != null) {
				reportTitle.setText(report.getTitle());
			}

			TextView reportTimestamp = (TextView) view.findViewById(R.id.report_detail_timestamp);
			if (reportTimestamp != null) {
				reportTimestamp.setText(mDateFormatter.print(report.getCreatedAt()));
			}

			ImageView reportSyncStatus = (ImageView) view.findViewById(R.id.report_detail_sync_status_icon);
			if (reportSyncStatus != null) {
				if (report.isDirty()) {
					reportSyncStatus.setVisibility(View.VISIBLE);
				} else {
					reportSyncStatus.setVisibility(View.GONE);
				}
			}
		}

		return view;
	}
}
