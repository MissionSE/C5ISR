package com.missionse.kestrelweather.reports.readings.impl;

import android.content.Context;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.reports.readings.ReadingsListItem;

/**
 * A readings list item for Pressure Trend.
 */
public class PressureTrendListItem implements ReadingsListItem {
	private final Context mContext;
	private final int mReportId;

	/**
	 * Constructor.
	 * @param context The current Context.
	 * @param reportId The id of the report.
	 */
	public PressureTrendListItem(final Context context, final int reportId) {
		mContext = context;
		mReportId = reportId;
	}

	@Override
	public int getIcon() {
		return R.drawable.ic_pressure_trend;
	}

	@Override
	public String getReading() {
		return "Up";
	}

	@Override
	public String getType() {
		return mContext.getString(R.string.kestrel_simulation_pressure_trend);
	}

	@Override
	public String getUnits() {
		return "N/A";
	}
}
