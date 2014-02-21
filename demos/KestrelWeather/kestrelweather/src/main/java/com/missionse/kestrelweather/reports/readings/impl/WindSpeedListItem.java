package com.missionse.kestrelweather.reports.readings.impl;

import android.content.Context;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.reports.readings.ReadingsListItem;

/**
 * A readings list item for Wind Speed.
 */
public class WindSpeedListItem implements ReadingsListItem {
	private Context mContext;

	/**
	 * Constructor.
	 * @param context The current Context.
	 */
	public WindSpeedListItem(final Context context) {
		mContext = context;
	}

	@Override
	public int getIcon() {
		return R.drawable.ic_wind_speed;
	}

	@Override
	public String getReading() {
		return "12";
	}

	@Override
	public String getType() {
		return mContext.getString(R.string.kestrel_simulation_wind_speed);
	}

	@Override
	public String getUnits() {
		return "mph";
	}
}
