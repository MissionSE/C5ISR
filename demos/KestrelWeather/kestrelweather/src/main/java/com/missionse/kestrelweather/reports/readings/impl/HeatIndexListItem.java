package com.missionse.kestrelweather.reports.readings.impl;

import android.content.Context;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.preferences.UnitPrefs;
import com.missionse.kestrelweather.reports.readings.ReadingsListItem;

/**
 * A readings list item for Heat Index.
 */
public class HeatIndexListItem implements ReadingsListItem {
	private final Context mContext;
	private final Report mReport;

	/**
	 * Constructor.
	 * @param context The current Context.
	 * @param report The report used to populate the data.
	 */
	public HeatIndexListItem(final Context context, final Report report) {
		mContext = context;
		mReport = report;
	}

	@Override
	public int getIcon() {
		return R.drawable.ic_heat_index;
	}

	@Override
	public String getReading() {
		float heatIndex = UnitPrefs.getPreferredTemperature(mContext,
				mReport.getKestrelWeather().getHeatIndex());
		return Float.toString(heatIndex);
	}

	@Override
	public String getType() {
		return mContext.getString(R.string.kestrel_simulation_heat_index);
	}

	@Override
	public String getUnits() {
		return UnitPrefs.getPreferredTemperatureUnitAbbr(mContext);
	}
}
