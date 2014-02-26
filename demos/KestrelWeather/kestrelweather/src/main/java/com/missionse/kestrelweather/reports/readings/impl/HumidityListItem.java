package com.missionse.kestrelweather.reports.readings.impl;

import android.content.Context;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.reports.readings.ReadingsListItem;

/**
 * A readings list item for Humidity.
 */
public class HumidityListItem implements ReadingsListItem {
	private final Context mContext;
	private final Report mReport;

	/**
	 * Constructor.
	 * @param context The current Context.
	 * @param report The report used to populate the data.
	 */
	public HumidityListItem(final Context context, final Report report) {
		mContext = context;
		mReport = report;
	}

	@Override
	public int getIcon() {
		return R.drawable.ic_humidity;
	}

	@Override
	public String getReading() {
		return Integer.toString(mReport.getKestrelWeather().getHumidity());
	}

	@Override
	public String getType() {
		return mContext.getString(R.string.kestrel_simulation_humidity);
	}

	@Override
	public String getUnits() {
		return "%";
	}
}
