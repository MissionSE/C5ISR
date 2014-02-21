package com.missionse.kestrelweather.reports.readings.impl;

import android.content.Context;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.reports.readings.ReadingsListItem;

/**
 * A readings list item for Pressure.
 */
public class PressureListItem implements ReadingsListItem {
	private Context mContext;

	/**
	 * Constructor.
	 * @param context The current Context.
	 */
	public PressureListItem(final Context context) {
		mContext = context;
	}

	@Override
	public int getIcon() {
		return R.drawable.ic_pressure;
	}

	@Override
	public String getReading() {
		return "31.59";
	}

	@Override
	public String getType() {
		return mContext.getString(R.string.kestrel_simulation_pressure);
	}

	@Override
	public String getUnits() {
		return "Hg";
	}
}
