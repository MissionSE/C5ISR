package com.missionse.kestrelweather.reports.readings.impl;

import android.content.Context;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.reports.readings.ReadingsListItem;

/**
 * A readings list item for Heat Index.
 */
public class HeatIndexListItem implements ReadingsListItem {
	private final Context mContext;
	private final int mReportId;

	/**
	 * Constructor.
	 * @param context The current Context.
	 * @param reportId The id of the report.
	 */
	public HeatIndexListItem(final Context context, final int reportId) {
		mContext = context;
		mReportId = reportId;
	}

	@Override
	public int getIcon() {
		return R.drawable.ic_heat_index;
	}

	@Override
	public String getReading() {
		return "87";
	}

	@Override
	public String getType() {
		return mContext.getString(R.string.kestrel_simulation_heat_index);
	}

	@Override
	public String getUnits() {
		return mContext.getString(R.string.celsius);
	}
}
