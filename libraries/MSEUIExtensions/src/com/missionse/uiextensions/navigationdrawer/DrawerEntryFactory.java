package com.missionse.uiextensions.navigationdrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.missionse.uiextensions.R;
import com.missionse.uiextensions.navigationdrawer.entry.DrawerHeader;
import com.missionse.uiextensions.navigationdrawer.entry.DrawerSimpleItem;
import com.missionse.uiextensions.navigationdrawer.entry.DrawerSpinner;

/**
 * Creates widgets used to populate the NavigationDrawer.
 */
public class DrawerEntryFactory {

	private LayoutInflater mInflater;

	/**
	 * Creates a new DrawerEntryFactory.
	 * @param context context of the parent activity/application
	 */
	public DrawerEntryFactory(final Context context) {
		mInflater = LayoutInflater.from(context);
	}

	/**
	 * Creates the view to represent a DrawerSimpleItem.
	 * @param convertView the view to populate
	 * @param parentView the parent view
	 * @param navDrawerItem the item on which to base the created View
	 * @return a View to be displayed in the NavigationDrawer
	 */
	public View getSimpleItemView(View convertView, final ViewGroup parentView, final DrawerItem navDrawerItem) {
		DrawerSimpleItem basicItem = (DrawerSimpleItem) navDrawerItem;
		DrawerSimpleItem.Holder holder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.nav_drawer_basic, parentView, false);
			TextView labelView = (TextView) convertView.findViewById(R.id.navmenuitem_label);
			ImageView iconView = (ImageView) convertView.findViewById(R.id.navmenuitem_icon);

			holder = new DrawerSimpleItem.Holder();
			holder.setTextView(labelView);
			holder.setImageView(iconView);

			convertView.setTag(holder);
		}

		if (holder == null) {
			holder = (DrawerSimpleItem.Holder) convertView.getTag();
		}

		holder.getTextView().setText(basicItem.getLabel());
		holder.getImageView().setImageResource(basicItem.getIcon());

		return convertView;
	}

	/**
	 * Creates the view to represent a DrawerHeader.
	 * @param convertView the view to populate
	 * @param parentView the parent view
	 * @param navDrawerItem the item on which to base the created View
	 * @return a View to be displayed in the NavigationDrawer
	 */
	public View getHeaderView(View convertView, final ViewGroup parentView, final DrawerItem navDrawerItem) {
		DrawerHeader header = (DrawerHeader) navDrawerItem;
		DrawerHeader.Holder holder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.nav_drawer_header, parentView, false);
			TextView labelView = (TextView) convertView.findViewById(R.id.navheader_label);

			holder = new DrawerHeader.Holder();
			holder.setTextView(labelView);
			convertView.setTag(holder);
		}

		if (holder == null) {
			holder = (DrawerHeader.Holder) convertView.getTag();
		}

		holder.getTextView().setText(header.getLabel());

		return convertView;
	}

	/**
	 * Creates the view to represent a DrawerSpinner.
	 * @param convertView the view to populate
	 * @param parentView the parent view
	 * @param navDrawerItem the item on which to base the created View
	 * @return a View to be displayed in the NavigationDrawer
	 */
	public View getSpinnerView(View convertView, final ViewGroup parentView, final DrawerItem navDrawerItem) {
		DrawerSpinner navDrawerSpinner = (DrawerSpinner) navDrawerItem;
		DrawerSpinner.Holder holder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.nav_drawer_spinner, parentView, false);
			Spinner spinner = (Spinner) convertView.findViewById(R.id.spinner);

			holder = new DrawerSpinner.Holder();
			holder.setSpinner(spinner);
			convertView.setTag(holder);
		}

		if (holder == null) {
			holder = (DrawerSpinner.Holder) convertView.getTag();
		}
		holder.getSpinner().setAdapter(navDrawerSpinner.getAdapter());
		holder.getSpinner().setOnItemSelectedListener(navDrawerSpinner.getListener());

		return convertView;
	}

	/**
	 * Creates the view to represent a DrawerSpinner.
	 * @param convertView the view to populate
	 * @param parentView the parent view
	 * @param navDrawerItem the item on which to base the created View
	 * @return a View to be displayed in the NavigationDrawer
	 */
	public View getDividerView(View convertView, final ViewGroup parentView, final DrawerItem navDrawerItem) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.nav_drawer_divider, parentView, false);
		}

		return convertView;
	}
}
