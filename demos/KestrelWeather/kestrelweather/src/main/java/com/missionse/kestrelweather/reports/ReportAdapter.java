package com.missionse.kestrelweather.reports;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.model.tables.Report;

import java.util.List;

/**
 * Provides an adapter for a list of photos.
 */
public class ReportAdapter extends ArrayAdapter<Report> {
	private int mResource;

	/**
	 * Constructor.
	 * @param context The current context.
	 * @param resource The resource ID for a layout file containing a View to use when instantiating views.
	 * @param objects The initial objects in the adapter.
	 */
	public ReportAdapter(final Context context, final int resource, final List<Report> objects) {
		super(context, resource, objects);

		mResource = resource;
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
				reportTitle.setText(report.getUserName());
			}

			TextView reportTimestamp = (TextView) view.findViewById(R.id.report_detail_timestamp);
			if (reportTimestamp != null) {
				reportTimestamp.setText(report.getCreatedAt().toString());
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
}
