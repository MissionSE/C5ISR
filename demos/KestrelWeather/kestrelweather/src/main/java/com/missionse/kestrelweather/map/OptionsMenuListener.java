package com.missionse.kestrelweather.map;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Provides the listener callbacks when creating and selecting a menu item.
 */
public interface OptionsMenuListener {
	/**
	 * Initializes the content of the options menu.
	 * @param menu The menu in which the items are placed.
	 * @param inflater The inflater used to inflate the xml file.
	 */
	void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater);

	/**
	 * Processes the selection of an item.
	 * @param item The item that was selected.
	 * @return Whether the processing is consumed.
	 */
	boolean onOptionsItemSelected(final MenuItem item);
}
