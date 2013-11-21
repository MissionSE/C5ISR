package com.missionse.slidingmenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class CustomMenuFragment extends MenuFragment {

	private ArrayAdapter<?> arrayAdapter;

	public void setCustomArrayAdapter(final ArrayAdapter<?> adapter) {
		arrayAdapter = adapter;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View contentView = super.onCreateView(inflater, container, savedInstanceState);

		menuList.setAdapter(arrayAdapter);

		return contentView;
	}
}
