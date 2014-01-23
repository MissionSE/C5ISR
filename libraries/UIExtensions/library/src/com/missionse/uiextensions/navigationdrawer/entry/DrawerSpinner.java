package com.missionse.uiextensions.navigationdrawer.entry;

import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.missionse.uiextensions.navigationdrawer.DrawerItem;
import com.missionse.uiextensions.navigationdrawer.DrawerItemType;

/**
 * Represents a Spinner widget in a Navigation Drawer.
 */
public final class DrawerSpinner extends DrawerItem {

	/**
	 * Tag object holding all necessary widgets to display this DrawerSpinner.
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

	public static final DrawerItemType TYPE = DrawerItemType.DROPDOWN;
	private ArrayAdapter<String> mAdapter;
	private OnItemSelectedListener mListener;

	private DrawerSpinner() {
	}

	/**
	 * Creates a new DrawerSpinner.
	 * @param id the id of this item
	 * @param adapter the adapter that will determine what items to show in the spinner
	 * @param listener the listener notified of item selection in the spinner
	 * @return a new DrawerSpinner
	 */
	public static DrawerSpinner create(final int id, final ArrayAdapter<String> adapter,
			final OnItemSelectedListener listener) {
		DrawerSpinner spinner = new DrawerSpinner();
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
	public DrawerItemType getType() {
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
