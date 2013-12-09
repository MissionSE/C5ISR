package com.missionse.uiextensions.navigationdrawer.entry;

import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.missionse.uiextensions.navigationdrawer.NavigationDrawerItem;
import com.missionse.uiextensions.navigationdrawer.NavigationDrawerItemType;

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
	private OnItemSelectedListener mListener;

	private NavigationDrawerSpinner() {
	}

	/**
	 * Creates a new NavigationDrawerSpinner.
	 * @param id the id of this item
	 * @param adapter the adapter that will determine what items to show in the spinner
	 * @param listener the listener notified of item selection in the spinner
	 * @return a new NavigationDrawerSpinner
	 */
	public static NavigationDrawerSpinner create(final int id, final ArrayAdapter<String> adapter,
			final OnItemSelectedListener listener) {
		NavigationDrawerSpinner spinner = new NavigationDrawerSpinner();
		spinner.setId(id);
		spinner.setAdapter(adapter);
		spinner.setListener(listener);
		return spinner;
	}

	/**
	 * Retrieves the adapter used to back this Spinner.
	 * @return the adapter
	 */
	public ArrayAdapter<String> getAdapter() {
		return mAdapter;
	}

	private void setAdapter(final ArrayAdapter<String> adapter) {
		mAdapter = adapter;
	}

	/**
	 * Retrieves the listener to be notified of item selection.
	 * @return the listener
	 */
	public OnItemSelectedListener getListener() {
		return mListener;
	}

	private void setListener(final OnItemSelectedListener listener) {
		mListener = listener;
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
