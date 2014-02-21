package com.missionse.kestrelweather.reports.readings.impl;

import android.content.Context;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.reports.readings.ReadingsListItem;

/**
 * A readings list item for Dew Point.
 */
public class DewPointListItem implements ReadingsListItem {
	private Context mContext;

	/**
	 * Constructor.
	 * @param context The current Context.
	 */
	public DewPointListItem(final Context context) {
		mContext = context;
	}

	@Override
	public int getIcon() {
		return R.drawable.ic_dew_point;
	}

	@Override
	public String getReading() {
		return "19.7";
	}

	@Override
	public String getType() {
		return mContext.getString(R.string.kestrel_simulation_dew_pt);
	}

	@Override
	public String getUnits() {
		return mContext.getString(R.string.celsius);
	}
}
