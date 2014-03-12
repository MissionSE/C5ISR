package com.missionse.kestrelweather.reports.readings.impl;

import android.content.Context;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.preferences.UnitPrefs;
import com.missionse.kestrelweather.reports.readings.ReadingsListItem;

/**
 * A readings list item for Temperature.
 */
public class TemperatureListItem implements ReadingsListItem {
	private final Context mContext;
	private final Report mReport;

	/**
	 * Constructor.
	 * @param context The current Context.
	 * @param report The report used to populate the data.
	 */
	public TemperatureListItem(final Context context, final Report report) {
		mContext = context;
		mReport = report;
	}

	@Override
	public int getIcon() {
		return R.drawable.ic_temperature;
	}

	@Override
	public String getReading() {
		float temperature = UnitPrefs.getPreferredTemperature(mContext,
				mReport.getKestrelWeather().getTemperature());
		return Float.toString(temperature);
	}

	@Override
	public String getType() {
		return mContext.getString(R.string.kestrel_simulation_temperature);
	}

	@Override
	public String getUnits() {
		return UnitPrefs.getPreferredTemperatureUnitAbbr(mContext);
	}
}
