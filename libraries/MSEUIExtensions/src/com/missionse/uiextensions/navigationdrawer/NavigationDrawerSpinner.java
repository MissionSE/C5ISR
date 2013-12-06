package com.missionse.uiextensions.navigationdrawer;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Represents a Spinner widget in a Navigation Drawer.
 */
public final class NavigationDrawerSpinner extends NavigationDrawerItem {

	/**
	 * Tag object holding all necessary widgets to display this NavigationDrawerSpinner.
	 */
	public static class Holder {
		private Spinner mSpinner;

		/**
		 * Gets the Spinner for this widget.
		 * @return the Spinner
		 */
		public Spinner getSpinner() {
			return mSpinner;
		}

		/**
		 * Sets the Spinner for this widget.
		 * @param spinner the Spinner
		 */
		public void setSpinner(final Spinner spinner) {
			mSpinner = spinner;
		}
	}

	public static final NavigationDrawerItemType TYPE = NavigationDrawerItemType.DROPDOWN;
	private ArrayAdapter<String> mAdapter;

	private NavigationDrawerSpinner() {
	}

	/**
	 * Creates a new NavigationDrawerSpinner.
	 * @param id the id of this item
	 * @param adapter the adapter that will determine what items to show in the spinner
	 * @return a new NavigationDrawerSpinner
	 */
	public static NavigationDrawerSpinner create(final int id, final ArrayAdapter<String> adapter) {
		NavigationDrawerSpinner dropdown = new NavigationDrawerSpinner();
		dropdown.setId(id);
		dropdown.setAdapter(adapter);
		return dropdown;
	}

	/**
	 * Retrieves the adapter used to back this Spinner.
	 * @return the adapter
	 */
	public ArrayAdapter<String> getAdapter() {
		return mAdapter;
	}

	/**
	 * Sets the adapter to be used to back this Spinner.
	 * @param adapter the adapter to be used
	 */
	public void setAdapter(final ArrayAdapter<String> adapter) {
		mAdapter = adapter;
	}

	@Override
	public NavigationDrawerItemType getType() {
		return TYPE;
	}

	@Override
	public boolean isSelectable() {
		return true;
	}

	@Override
	public boolean willChangeActionBarTitle() {
		return false;
	}

	@Override
	public String getActionBarTitle() {
		return null;
	}
}
