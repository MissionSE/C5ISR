package com.missionse.kestrelweather.reports.readings.impl;

import android.content.Context;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.reports.readings.ReadingsListItem;

/**
 * A readings list item for Wind Chill.
 */
public class WindChillListItem implements ReadingsListItem {
	private final Context mContext;
	private final Report mReport;

	/**
	 * Constructor.
	 * @param context The current Context.
	 * @param report The report used to populate the data.
	 */
	public WindChillListItem(final Context context, final Report report) {
		mContext = context;
		mReport = report;
	}

	@Override
	public int getIcon() {
		return R.drawable.ic_wind_chill;
	}

	@Override
	public String getReading() {
		return Float.toString(mReport.getKestrelWeather().getWindChill());
	}

	@Override
	public String getType() {
		return mContext.getString(R.string.kestrel_simulation_wind_chill);
	}

	@Override
	public String getUnits() {
		return mContext.getString(R.string.celsius);
	}
}
