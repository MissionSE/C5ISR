package com.missionse.uiextensions.navigationdrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.missionse.uiextensions.R;
import com.missionse.uiextensions.navigationdrawer.entry.NavigationDrawerHeader;
import com.missionse.uiextensions.navigationdrawer.entry.NavigationDrawerSimpleItem;
import com.missionse.uiextensions.navigationdrawer.entry.NavigationDrawerSpinner;

/**
 * Creates widgets used to populate the NavigationDrawer.
 */
public class NavigationDrawerEntryFactory {

	private LayoutInflater mInflater;

	/**
	 * Creates a new NavigationDrawerEntryFactory.
	 * @param context context of the parent activity/application
	 */
	public NavigationDrawerEntryFactory(final Context context) {
		mInflater = LayoutInflater.from(context);
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
		holder.getSpinner().setOnItemSelectedListener(navDrawerSpinner.getListener());

		return convertView;
	}

	/**
	 * Creates the view to represent a NavigationDrawerSpinner.
	 * @param convertView the view to populate
	 * @param parentView the parent view
	 * @param navDrawerItem the item on which to base the created View
	 * @return a View to be displayed in the NavigationDrawer
	 */
	public View getDividerView(View convertView, final ViewGroup parentView, final NavigationDrawerItem navDrawerItem) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.nav_drawer_divider, parentView, false);
		}

		return convertView;
	}
}
