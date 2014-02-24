package com.missionse.kestrelweather.reports.auxiliary;

import android.app.FragmentManager;
import android.content.Context;

import com.missionse.kestrelweather.reports.auxiliary.impl.AudioListItem;
import com.missionse.kestrelweather.reports.auxiliary.impl.NotesListItem;
import com.missionse.kestrelweather.reports.auxiliary.impl.PhotosListItem;

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
	 * @param context The current context.
	 * @param fragmentManager The fragment manager.
	 * @return A list of AuxiliaryDataItems.
	 */
	public static List<AuxiliaryDataListItem> getListItems(final Context context, final FragmentManager fragmentManager) {
		List<AuxiliaryDataListItem> auxiliaryDataListItems = new ArrayList<AuxiliaryDataListItem>();
		auxiliaryDataListItems.add(new NotesListItem(context, fragmentManager));
		auxiliaryDataListItems.add(new PhotosListItem(context, fragmentManager));
		auxiliaryDataListItems.add(new AudioListItem(context, fragmentManager));

		return auxiliaryDataListItems;
	}
}
