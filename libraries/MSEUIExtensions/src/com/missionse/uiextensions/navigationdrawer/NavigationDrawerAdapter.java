package com.missionse.uiextensions.navigationdrawer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Adapter that converts NavigationDrawerItem constructs to Views to be displayed.
 */
public class NavigationDrawerAdapter extends ArrayAdapter<NavigationDrawerItem> {

	private NavigationDrawerEntryFactory mEntryFactory;

	/**
	 * Creates a new NavigationDrawerAdapter.
	 * @param context the parent activity's context
	 * @param resource NOT USED, should be 0
	 * @param objects the items to display
	 */
	public NavigationDrawerAdapter(final Context context, final int resource, final NavigationDrawerItem[] objects) {
		super(context, resource, objects);
		mEntryFactory = new NavigationDrawerEntryFactory(context);
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		View view = null;
		NavigationDrawerItem menuItem = getItem(position);
		if (menuItem.getType() == NavigationDrawerItemType.SIMPLE) {
			view = mEntryFactory.getSimpleItemView(convertView, parent, menuItem);
		} else if (menuItem.getType() == NavigationDrawerItemType.HEADER) {
			view = mEntryFactory.getHeaderView(convertView, parent, menuItem);
		} else if (menuItem.getType() == NavigationDrawerItemType.DROPDOWN) {
			view = mEntryFactory.getSpinnerView(convertView, parent, menuItem);
		} else if (menuItem.getType() == NavigationDrawerItemType.DIVIDER) {
			view = mEntryFactory.getDividerView(convertView, parent, menuItem);
		}
		return view;
	}

	@Override
	public int getViewTypeCount() {
		return NavigationDrawerItemType.getNumberOfTypes();
	}

	@Override
	public int getItemViewType(final int position) {
		return getItem(position).getType().ordinal();
	}

	@Override
	public boolean isEnabled(final int position) {
		return getItem(position).isSelectable();
	}
}
