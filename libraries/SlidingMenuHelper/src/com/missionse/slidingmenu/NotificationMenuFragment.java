package com.missionse.slidingmenu;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * A menu in which items are dismissable as well as selectable.
 */
public class NotificationMenuFragment extends MenuFragment {
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

		SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(getMenuList(),
				new SwipeDismissListViewTouchListener.DismissCallbacks() {
					@Override
					public boolean canDismiss(final int position) {
						return true;
					}

					@SuppressWarnings("unchecked")
					@Override
					public void onDismiss(final ListView listView, final int[] reverseSortedPositions) {
						for (int position : reverseSortedPositions) {
							getAdapter().remove(getAdapter().getItem(position));
						}
						getAdapter().notifyDataSetChanged();
					}
				});

		getMenuList().setOnTouchListener(touchListener);

		getMenuList().setOnScrollListener(touchListener.makeScrollListener());

		return contentView;
	}
}
