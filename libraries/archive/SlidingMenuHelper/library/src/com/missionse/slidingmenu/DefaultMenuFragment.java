package com.missionse.slidingmenu;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Provides a basic Menu Fragment that uses a list of Strings to create the menu list.
 */
public class DefaultMenuFragment extends MenuFragment {

	private List<String> mMenuEntries;

	/**
	 * Sets the list of menu entries to be used when creating the view.
	 * @param entries list of Strings to populate the menu with
	 */
	public void setMenuEntries(final List<String> entries) {
		mMenuEntries = entries;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View contentView = super.onCreateView(inflater, container, savedInstanceState);

		setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.default_menu_entry, R.id.menu_text, mMenuEntries));
		getMenuList().setAdapter(getAdapter());

		return contentView;
	}
}
