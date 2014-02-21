package com.missionse.kestrelweather.reports.readings.impl;

import android.content.Context;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.reports.readings.ReadingsListItem;

/**
 * A readings list item for Wind Direction.
 */
public class WindDirectionListItem implements ReadingsListItem {
	private Context mContext;

	/**
	 * Constructor.
	 * @param context The current Context.
	 */
	public WindDirectionListItem(final Context context) {
		mContext = context;
	}

	@Override
	public int getIcon() {
		return R.drawable.ic_wind_direction;
	}

	@Override
	public String getReading() {
		return "45";
	}

	@Override
	public String getType() {
		return mContext.getString(R.string.kestrel_simulation_wind_dir);
	}

	@Override
	public String getUnits() {
		return "degrees";
	}
}
