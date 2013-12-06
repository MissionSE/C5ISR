package com.missionse.uiextensions.navigationdrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.missionse.uiextensions.R;

/**
 * Adapter that converts NavigationDrawerItem constructs to Views to be displayed.
 */
public class NavigationDrawerAdapter extends ArrayAdapter<NavigationDrawerItem> {

	private LayoutInflater mInflater;

	/**
	 * Creates a new NavigationDrawerAdapter.
	 * @param context the parent activity's context
	 * @param resource NOT USED, should be 0
	 * @param objects the items to display
	 */
	public NavigationDrawerAdapter(final Context context, final int resource, final NavigationDrawerItem[] objects) {
		super(context, resource, objects);
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		View view = null;
		NavigationDrawerItem menuItem = getItem(position);
		if (menuItem.getType() == NavigationDrawerItemType.SIMPLE) {
			view = getSimpleItemView(convertView, parent, menuItem);
		} else if (menuItem.getType() == NavigationDrawerItemType.HEADER) {
			view = getHeaderView(convertView, parent, menuItem);
		} else if (menuItem.getType() == NavigationDrawerItemType.DROPDOWN) {
			view = getSpinnerView(convertView, parent, menuItem);
		}
		return view;
	}

	/**
	 * Creates the view to represent a NavigationDrawerSimpleItem.
	 * @param convertView the view to populate
	 * @param parentView the parent view
	 * @param navDrawerItem the item on which to base the created View
	 * @return a View to be displayed in the NavigationDrawer
	 */
	public View getSimpleItemView(View convertView, final ViewGroup parentView, final NavigationDrawerItem navDrawerItem) {
		NavigationDrawerSimpleItem basicItem = (NavigationDrawerSimpleItem) navDrawerItem;
		NavigationDrawerSimpleItem.Holder holder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.nav_drawer_basic, parentView, false);
			TextView labelView = (TextView) convertView.findViewById(R.id.navmenuitem_label);
			ImageView iconView = (ImageView) convertView.findViewById(R.id.navmenuitem_icon);

			holder = new NavigationDrawerSimpleItem.Holder();
			holder.setTextView(labelView);
			holder.setImageView(iconView);

			convertView.setTag(holder);
		}

		if (holder == null) {
			holder = (NavigationDrawerSimpleItem.Holder) convertView.getTag();
		}

		holder.getTextView().setText(basicItem.getLabel());
		holder.getImageView().setImageResource(basicItem.getIcon());

		return convertView;
	}

	/**
	 * Creates the view to represent a NavigationDrawerHeader.
	 * @param convertView the view to populate
	 * @param parentView the parent view
	 * @param navDrawerItem the item on which to base the created View
	 * @return a View to be displayed in the NavigationDrawer
	 */
	public View getHeaderView(View convertView, final ViewGroup parentView, final NavigationDrawerItem navDrawerItem) {
		NavigationDrawerHeader header = (NavigationDrawerHeader) navDrawerItem;
		NavigationDrawerHeader.Holder holder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.nav_drawer_header, parentView, false);
			TextView labelView = (TextView) convertView.findViewById(R.id.navheader_label);

			holder = new NavigationDrawerHeader.Holder();
			holder.setTextView(labelView);
			convertView.setTag(holder);
		}

		if (holder == null) {
			holder = (NavigationDrawerHeader.Holder) convertView.getTag();
		}

		holder.getTextView().setText(header.getLabel());

		return convertView;
	}

	/**
	 * Creates the view to represent a NavigationDrawerSpinner.
	 * @param convertView the view to populate
	 * @param parentView the parent view
	 * @param navDrawerItem the item on which to base the created View
	 * @return a View to be displayed in the NavigationDrawer
	 */
	public View getSpinnerView(View convertView, final ViewGroup parentView, final NavigationDrawerItem navDrawerItem) {
		NavigationDrawerSpinner navDrawerSpinner = (NavigationDrawerSpinner) navDrawerItem;
		NavigationDrawerSpinner.Holder holder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.nav_drawer_spinner, parentView, false);
			Spinner spinner = (Spinner) convertView.findViewById(R.id.spinner);

			holder = new NavigationDrawerSpinner.Holder();
			holder.setSpinner(spinner);
			convertView.setTag(holder);
		}

		if (holder == null) {
			holder = (NavigationDrawerSpinner.Holder) convertView.getTag();
		}
		holder.getSpinner().setAdapter(navDrawerSpinner.getAdapter());

		return convertView;
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
