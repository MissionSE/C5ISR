package com.missionse.kestrelweather.reports.readings;

import android.content.Context;

import com.missionse.kestrelweather.reports.readings.impl.DewPointListItem;
import com.missionse.kestrelweather.reports.readings.impl.HeatIndexListItem;
import com.missionse.kestrelweather.reports.readings.impl.HumidityListItem;
import com.missionse.kestrelweather.reports.readings.impl.PressureListItem;
import com.missionse.kestrelweather.reports.readings.impl.PressureTrendListItem;
import com.missionse.kestrelweather.reports.readings.impl.TemperatureListItem;
import com.missionse.kestrelweather.reports.readings.impl.WindChillListItem;
import com.missionse.kestrelweather.reports.readings.impl.WindDirectionListItem;
import com.missionse.kestrelweather.reports.readings.impl.WindSpeedListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides functionality to get the list of items in the readings list.
 */
public final class ReadingsListFactory {
	private ReadingsListFactory() {
	}

	/**
	 * Creates an returns a list of ReadingsListItems.
	 * @param context The current context.
	 * @param reportId The id of the report.
	 * @return A list of ReadingsListItems.
	 */
	public static List<ReadingsListItem> getListItems(final Context context, final int reportId) {
		List<ReadingsListItem> readingsListItems = new ArrayList<ReadingsListItem>();
		readingsListItems.add(new DewPointListItem(context, reportId));
		readingsListItems.add(new TemperatureListItem(context, reportId));
		readingsListItems.add(new HumidityListItem(context, reportId));
		readingsListItems.add(new PressureListItem(context, reportId));
		readingsListItems.add(new PressureTrendListItem(context, reportId));
		readingsListItems.add(new HeatIndexListItem(context, reportId));
		readingsListItems.add(new WindSpeedListItem(context, reportId));
		readingsListItems.add(new WindDirectionListItem(context, reportId));
		readingsListItems.add(new WindChillListItem(context, reportId));

		return readingsListItems;
	}
}
