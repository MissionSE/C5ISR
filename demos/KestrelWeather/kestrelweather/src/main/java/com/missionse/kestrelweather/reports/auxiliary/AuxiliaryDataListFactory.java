package com.missionse.kestrelweather.reports.auxiliary;

import com.missionse.kestrelweather.KestrelWeatherActivity;
import com.missionse.kestrelweather.reports.auxiliary.impl.AudioListItem;
import com.missionse.kestrelweather.reports.auxiliary.impl.NotesListItem;
import com.missionse.kestrelweather.reports.auxiliary.impl.PhotosListItem;
import com.missionse.kestrelweather.reports.auxiliary.impl.VideoListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides functionality to get the list of items in the auxiliary data list.
 */
public final class AuxiliaryDataListFactory {
	private AuxiliaryDataListFactory() {
	}

	/**
	 * Creates an returns a list of auxiliary data items.
	 * @param activity Instance of KestrelWeatherActivity.
	 * @param reportId The id of the report.
	 * @return A list of AuxiliaryDataItems.
	 */
	public static List<AuxiliaryDataListItem> getListItems(final KestrelWeatherActivity activity, final int reportId) {
		List<AuxiliaryDataListItem> auxiliaryDataListItems = new ArrayList<AuxiliaryDataListItem>();
		auxiliaryDataListItems.add(new NotesListItem(activity, reportId));
		auxiliaryDataListItems.add(new PhotosListItem(activity, reportId));
		auxiliaryDataListItems.add(new AudioListItem(activity, reportId));
		auxiliaryDataListItems.add(new VideoListItem(activity, reportId));

		return auxiliaryDataListItems;
	}
}
