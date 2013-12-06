package com.missionse.logisticsexample.navdrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.missionse.logisticsexample.R;

public class NavigationDrawerAdapter extends ArrayAdapter<NavigationDrawerItem> {

	private LayoutInflater inflater;

	public NavigationDrawerAdapter(final Context context, final int resource, final NavigationDrawerItem[] objects) {
		super(context, resource, objects);
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		View view = null;
		NavigationDrawerItem menuItem = getItem(position);
		if (menuItem.getType() == NavigationDrawerItemType.BASIC) {
			view = getBasicView(convertView, parent, menuItem);
		} else if (menuItem.getType() == NavigationDrawerItemType.HEADER) {
			view = getHeaderView(convertView, parent, menuItem);
		} else if (menuItem.getType() == NavigationDrawerItemType.DROPDOWN) {
			view = getDropdownView(convertView, parent, menuItem);
		}
		return view;
	}

	public View getBasicView(View convertView, final ViewGroup parentView, final NavigationDrawerItem navDrawerItem) {
		NavMenuItemHolder navMenuItemHolder = null;

		NavigationDrawerMenuBasic basicItem = (NavigationDrawerMenuBasic) navDrawerItem;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.nav_drawer_item, parentView, false);
			TextView labelView = (TextView) convertView.findViewById(R.id.navmenuitem_label);
			ImageView iconView = (ImageView) convertView.findViewById(R.id.navmenuitem_icon);

			navMenuItemHolder = new NavMenuItemHolder();
			navMenuItemHolder.labelView = labelView;
			navMenuItemHolder.iconView = iconView;

			convertView.setTag(navMenuItemHolder);
		}

		if (navMenuItemHolder == null) {
			navMenuItemHolder = (NavMenuItemHolder) convertView.getTag();
		}

		navMenuItemHolder.labelView.setText(basicItem.getLabel());
		navMenuItemHolder.iconView.setImageResource(basicItem.getIcon());

		return convertView;
	}

	public View getHeaderView(View convertView, final ViewGroup parentView, final NavigationDrawerItem navDrawerItem) {
		NavigationDrawerMenuHeader header = (NavigationDrawerMenuHeader) navDrawerItem;
		NavMenuSectionHolder navMenuItemHolder = null;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.nav_drawer_header, parentView, false);
			TextView labelView = (TextView) convertView.findViewById(R.id.navheader_label);

			navMenuItemHolder = new NavMenuSectionHolder();
			navMenuItemHolder.labelView = labelView;
			convertView.setTag(navMenuItemHolder);
		}

		if (navMenuItemHolder == null) {
			navMenuItemHolder = (NavMenuSectionHolder) convertView.getTag();
		}

		navMenuItemHolder.labelView.setText(header.getLabel());

		return convertView;
	}

	public View getDropdownView(View convertView, final ViewGroup parentView, final NavigationDrawerItem navDrawerItem) {

		NavigationDrawerMenuDropdown dropdown = (NavigationDrawerMenuDropdown) navDrawerItem;
		NavMenuDropdownHolder navDropdownHolder = null;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.nav_drawer_spinner, parentView, false);
			Spinner spinner = (Spinner) convertView.findViewById(R.id.spinner);

			navDropdownHolder = new NavMenuDropdownHolder();
			navDropdownHolder.spinner = spinner;
			convertView.setTag(navDropdownHolder);
		}

		if (navDropdownHolder == null) {
			navDropdownHolder = (NavMenuDropdownHolder) convertView.getTag();
		}
		navDropdownHolder.spinner.setAdapter(dropdown.getAdapter());

		return convertView;
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public int getItemViewType(final int position) {
		return getItem(position).getType().ordinal();
	}

	@Override
	public boolean isEnabled(final int position) {
		return getItem(position).isEnabled();
	}

	private class NavMenuItemHolder {
		private TextView labelView;
		private ImageView iconView;
	}

	private class NavMenuSectionHolder {
		private TextView labelView;
	}

	private class NavMenuDropdownHolder {
		private Spinner spinner;
	}
}
