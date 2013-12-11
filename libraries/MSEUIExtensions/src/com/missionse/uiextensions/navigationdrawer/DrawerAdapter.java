package com.missionse.uiextensions.navigationdrawer;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Adapter that converts DrawerItem constructs to Views to be displayed.
 */
public class DrawerAdapter extends ArrayAdapter<DrawerItem> {

	private DrawerEntryFactory mEntryFactory;

	/**
	 * Creates a new DrawerAdapter.
	 * @param context the parent activity's context
	 */
	public DrawerAdapter(final Context context) {
		super(context, 0, new ArrayList<DrawerItem>());
		mEntryFactory = new DrawerEntryFactory(context);
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		View view = null;
		DrawerItem menuItem = getItem(position);
		if (menuItem.getType() == DrawerItemType.SIMPLE) {
			view = mEntryFactory.getSimpleItemView(convertView, parent, menuItem);
		} else if (menuItem.getType() == DrawerItemType.HEADER) {
			view = mEntryFactory.getHeaderView(convertView, parent, menuItem);
		} else if (menuItem.getType() == DrawerItemType.DROPDOWN) {
			view = mEntryFactory.getSpinnerView(convertView, parent, menuItem);
		} else if (menuItem.getType() == DrawerItemType.DIVIDER) {
			view = mEntryFactory.getDividerView(convertView, parent, menuItem);
		}
		return view;
	}

	@Override
	public int getViewTypeCount() {
		return DrawerItemType.getNumberOfTypes();
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
