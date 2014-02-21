package com.missionse.kestrelweather.reports.readings.impl;

import android.content.Context;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.reports.readings.ReadingsListItem;

/**
 * A readings list item for Humidity.
 */
public class HumidityListItem implements ReadingsListItem {
	private Context mContext;

	/**
	 * Constructor.
	 * @param context The current Context.
	 */
	public HumidityListItem(final Context context) {
		mContext = context;
	}

	@Override
	public int getIcon() {
		return R.drawable.ic_humidity;
	}

	@Override
	public String getReading() {
		return "31.9";
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
