package com.missionse.kestrelweather.reports.readings;

import android.content.Context;

import com.missionse.kestrelweather.database.model.tables.Report;
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
	 * @param report The report used to get the readings.
	 * @return A list of ReadingsListItems.
	 */
	public static List<ReadingsListItem> getListItems(final Context context, final Report report) {
		List<ReadingsListItem> readingsListItems = new ArrayList<ReadingsListItem>();
		readingsListItems.add(new DewPointListItem(context, report));
		readingsListItems.add(new TemperatureListItem(context, report));
		readingsListItems.add(new HumidityListItem(context, report));
		readingsListItems.add(new PressureListItem(context, report));
		readingsListItems.add(new PressureTrendListItem(context, report));
		readingsListItems.add(new HeatIndexListItem(context, report));
		readingsListItems.add(new WindSpeedListItem(context, report));
		readingsListItems.add(new WindDirectionListItem(context, report));
		readingsListItems.add(new WindChillListItem(context, report));

		return readingsListItems;
	}
}
