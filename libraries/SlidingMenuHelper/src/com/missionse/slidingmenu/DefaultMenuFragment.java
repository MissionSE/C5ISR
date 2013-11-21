package com.missionse.slidingmenu;

import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class DefaultMenuFragment extends MenuFragment {

	private List<String> menuEntries;

	public void setMenuEntries(final List<String> entries) {
		menuEntries = entries;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View contentView = super.onCreateView(inflater, container, savedInstanceState);

		Log.e(DefaultMenuFragment.class.getSimpleName(), "Adding menu entries/adapter");
		menuList.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.default_menu_entry, R.id.menu_text,
				menuEntries));

		return contentView;
	}

}
