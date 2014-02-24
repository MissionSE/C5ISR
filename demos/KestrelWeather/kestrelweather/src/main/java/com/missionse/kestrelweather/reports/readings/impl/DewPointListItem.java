package com.missionse.kestrelweather.reports.readings.impl;

import android.content.Context;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.reports.readings.ReadingsListItem;

/**
 * A readings list item for Dew Point.
 */
public class DewPointListItem implements ReadingsListItem {
	private final Context mContext;
	private final int mReportId;

	/**
	 * Constructor.
	 * @param context The current Context.
	 * @param reportId The id of the report.
	 */
	public DewPointListItem(final Context context, final int reportId) {
		mContext = context;
		mReportId = reportId;
	}

	@Override
	public int getIcon() {
		return R.drawable.ic_dew_point;
	}

	@Override
	public String getReading() {
		return "19.7";
	}

	@Override
	public String getType() {
		return mContext.getString(R.string.kestrel_simulation_dew_pt);
	}

	@Override
	public String getUnits() {
		return mContext.getString(R.string.celsius);
	}
}
