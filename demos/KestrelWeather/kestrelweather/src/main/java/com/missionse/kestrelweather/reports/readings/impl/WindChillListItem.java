package com.missionse.kestrelweather.reports.readings.impl;

import android.content.Context;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.reports.readings.ReadingsListItem;

/**
 * A readings list item for Wind Chill.
 */
public class WindChillListItem implements ReadingsListItem {
	private Context mContext;

	/**
	 * Constructor.
	 * @param context The current Context.
	 */
	public WindChillListItem(final Context context) {
		mContext = context;
	}

	@Override
	public int getIcon() {
		return R.drawable.ic_wind_chill;
	}

	@Override
	public String getReading() {
		return "57";
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
