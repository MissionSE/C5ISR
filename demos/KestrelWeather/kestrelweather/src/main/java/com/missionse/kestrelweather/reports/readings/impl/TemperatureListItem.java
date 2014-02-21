package com.missionse.kestrelweather.reports.readings.impl;

import android.content.Context;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.reports.readings.ReadingsListItem;

/**
 * A readings list item for Temperature.
 */
public class TemperatureListItem implements ReadingsListItem {
	private Context mContext;

	/**
	 * Constructor.
	 * @param context The current Context.
	 */
	public TemperatureListItem(final Context context) {
		mContext = context;
	}

	@Override
	public int getIcon() {
		return R.drawable.ic_temperature;
	}

	@Override
	public String getReading() {
		return "73";
	}

	@Override
	public String getType() {
		return mContext.getString(R.string.kestrel_simulation_temperature);
	}

	@Override
	public String getUnits() {
		return mContext.getString(R.string.celsius);
	}
}
