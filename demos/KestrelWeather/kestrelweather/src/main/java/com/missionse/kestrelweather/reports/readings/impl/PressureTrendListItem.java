package com.missionse.kestrelweather.reports.readings.impl;

import android.content.Context;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.reports.readings.ReadingsListItem;

/**
 * A readings list item for Pressure Trend.
 */
public class PressureTrendListItem implements ReadingsListItem {
	private final Context mContext;
	private final Report mReport;

	/**
	 * Constructor.
	 * @param context The current Context.
	 * @param report The report used to populate the data.
	 */
	public PressureTrendListItem(final Context context, final Report report) {
		mContext = context;
		mReport = report;
	}

	@Override
	public int getIcon() {
		return R.drawable.ic_pressure_trend;
	}

	@Override
	public String getReading() {
		String pressureTrend;
		switch (mReport.getKestrelWeather().getPressureTrend()) {
			case 0:
				pressureTrend = "Up";
				break;
			case 1:
				pressureTrend = "Down";
				break;
			default:
				pressureTrend = "Unknown";
		}
		return pressureTrend;
	}

	@Override
	public String getType() {
		return mContext.getString(R.string.kestrel_simulation_pressure_trend);
	}

	@Override
	public String getUnits() {
		return "";
	}
}
