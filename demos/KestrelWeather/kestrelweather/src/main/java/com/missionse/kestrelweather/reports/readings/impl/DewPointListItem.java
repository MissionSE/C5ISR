package com.missionse.kestrelweather.reports.readings.impl;

import android.content.Context;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.preferences.UnitPrefs;
import com.missionse.kestrelweather.reports.readings.ReadingsListItem;

/**
 * A readings list item for Dew Point.
 */
public class DewPointListItem implements ReadingsListItem {
	private final Context mContext;
	private final Report mReport;

	/**
	 * Constructor.
	 * @param context The current Context.
	 * @param report The report used to populate the data.
	 */
	public DewPointListItem(final Context context, final Report report) {
		mContext = context;
		mReport = report;
	}

	@Override
	public int getIcon() {
		return R.drawable.ic_dew_point;
	}

	@Override
	public String getReading() {
		float dewPoint = UnitPrefs.getPreferredTemperature(mContext,
				mReport.getKestrelWeather().getDewPoint());
		return Float.toString(dewPoint);
	}

	@Override
	public String getType() {
		return mContext.getString(R.string.kestrel_simulation_dew_pt);
	}

	@Override
	public String getUnits() {
		return UnitPrefs.getPreferredTemperatureUnitAbbr(mContext);
	}
}
