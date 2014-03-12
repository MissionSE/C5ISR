package com.missionse.kestrelweather.reports.readings.impl;

import android.content.Context;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.preferences.UnitPrefs;
import com.missionse.kestrelweather.reports.readings.ReadingsListItem;
import com.missionse.kestrelweather.util.UnitConverter;

/**
 * A readings list item for Wind Direction.
 */
public class WindDirectionListItem implements ReadingsListItem {
	private final Context mContext;
	private final Report mReport;

	/**
	 * Constructor.
	 * @param context The current Context.
	 * @param report The report used to populate the data.
	 */
	public WindDirectionListItem(final Context context, final Report report) {
		mContext = context;
		mReport = report;
	}

	@Override
	public int getIcon() {
		return R.drawable.ic_wind_direction;
	}

	@Override
	public String getReading() {
		float windDirection = UnitPrefs.getPreferredWindDirection(mContext,
				mReport.getKestrelWeather().getWindDirection());
		String windDirectionCardinal = UnitConverter.degreeToCardinal(mContext,
				mReport.getKestrelWeather().getWindDirection());
		return String.format("%.1f (%s)", windDirection, windDirectionCardinal);
	}

	@Override
	public String getType() {
		return mContext.getString(R.string.kestrel_simulation_wind_dir);
	}

	@Override
	public String getUnits() {
		return UnitPrefs.getPreferredWindDirectionUnit(mContext);
	}
}
