package com.missionse.slidingmenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Custom fragment that provides an array adapter to be used when creating the menu list.
 */
public class CustomMenuFragment extends MenuFragment {
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View contentView = super.onCreateView(inflater, container, savedInstanceState);

		getMenuList().setAdapter(getAdapter());

		return contentView;
	}
}
