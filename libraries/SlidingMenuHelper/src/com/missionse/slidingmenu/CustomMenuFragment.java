package com.missionse.slidingmenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Custom fragment that provides an array adapter to be used when creating the menu list.
 */
public class CustomMenuFragment extends MenuFragment {

	private ArrayAdapter<?> mArrayAdapter;

	/**
	 * Sets the array adapter to be used when creating the menu list.
	 * @param adapter the adapter to be used
	 */
	public void setCustomArrayAdapter(final ArrayAdapter<?> adapter) {
		mArrayAdapter = adapter;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View contentView = super.onCreateView(inflater, container, savedInstanceState);

		getMenuList().setAdapter(mArrayAdapter);

		return contentView;
	}
}
