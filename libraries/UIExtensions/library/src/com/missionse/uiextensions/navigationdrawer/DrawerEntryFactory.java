package com.missionse.uiextensions.navigationdrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.missionse.uiextensions.R;
import com.missionse.uiextensions.navigationdrawer.entry.DrawerComplexItem;
import com.missionse.uiextensions.navigationdrawer.entry.DrawerHeader;
import com.missionse.uiextensions.navigationdrawer.entry.DrawerSimpleItem;
import com.missionse.uiextensions.navigationdrawer.entry.DrawerSimpleNumberedItem;
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
		if (basicItem.getTextColorStateList() != Drawer.USE_DEFAULT) {
			holder.getTextView().setTextColor(basicItem.getContext().getResources().getColorStateList(
				basicItem.getTextColorStateList()));
		}
		if (basicItem.getIcon() != Drawer.NO_ICON) {
			holder.getImageView().setImageResource(basicItem.getIcon());
		} else {
			holder.getImageView().setVisibility(View.GONE);
		}

        if (basicItem.getBackgroundDrawable() != Drawer.USE_DEFAULT) {
            convertView.setBackground(
                basicItem.getContext().getResources().getDrawable(basicItem.getBackgroundDrawable()));
        }

		return convertView;
	}

	/**
	 * Creates the view to represent a DrawerSimpleItem.
	 * @param convertView the view to populate
	 * @param parentView the parent view
	 * @param navDrawerItem the item on which to base the created View
	 * @return a View to be displayed in the NavigationDrawer
	 */
	public View getSimpleNumberedItemView(View convertView, final ViewGroup parentView, final DrawerItem navDrawerItem) {
		DrawerSimpleNumberedItem basicItem = (DrawerSimpleNumberedItem) navDrawerItem;
		DrawerSimpleNumberedItem.Holder holder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.nav_drawer_basic_numbered, parentView, false);
			TextView labelView = (TextView) convertView.findViewById(R.id.navmenuitem_label);
			TextView numberView = (TextView) convertView.findViewById(R.id.navmenuitem_number);
			ImageView iconView = (ImageView) convertView.findViewById(R.id.navmenuitem_icon);

			holder = new DrawerSimpleNumberedItem.Holder();
			holder.setTextView(labelView);
			holder.setNumberView(numberView);
			holder.setImageView(iconView);

			convertView.setTag(holder);
		}

		if (holder == null) {
			holder = (DrawerSimpleNumberedItem.Holder) convertView.getTag();
		}

		holder.getTextView().setText(basicItem.getLabel());
		holder.getNumberView().setText(basicItem.getNumber());
		if (basicItem.getTextColorStateList() != Drawer.USE_DEFAULT) {
			holder.getTextView().setTextColor(basicItem.getContext().getResources().getColorStateList(
				basicItem.getTextColorStateList()));
			holder.getNumberView().setTextColor(basicItem.getContext().getResources().getColorStateList(
				basicItem.getTextColorStateList()));
		}
		if (basicItem.getIcon() != Drawer.NO_ICON) {
			holder.getImageView().setImageResource(basicItem.getIcon());
		} else {
			holder.getImageView().setVisibility(View.GONE);
		}

		if (basicItem.getBackgroundDrawable() != Drawer.USE_DEFAULT) {
			convertView.setBackground(
				basicItem.getContext().getResources().getDrawable(basicItem.getBackgroundDrawable()));
		}

		return convertView;
	}

	/**
	 * Creates the view to represent a DrawerSimpleItem.
	 * @param convertView the view to populate
	 * @param parentView the parent view
	 * @param navDrawerItem the item on which to base the created View
	 * @return a View to be displayed in the NavigationDrawer
	 */
	public View getComplexItemView(View convertView, final ViewGroup parentView, final DrawerItem navDrawerItem) {
		DrawerComplexItem complexItem = (DrawerComplexItem) navDrawerItem;
		DrawerComplexItem.Holder holder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.nav_drawer_complex, parentView, false);
			TextView labelView = (TextView) convertView.findViewById(R.id.navmenuitem_label);
			TextView subtitleView = (TextView) convertView.findViewById(R.id.navmenuitem_subtitle);
			ImageView iconView = (ImageView) convertView.findViewById(R.id.navmenuitem_icon);

			holder = new DrawerComplexItem.Holder();
			holder.setTextView(labelView);
			holder.setSubtitleTextView(subtitleView);
			holder.setImageView(iconView);

			convertView.setTag(holder);
		}

		if (holder == null) {
			holder = (DrawerComplexItem.Holder) convertView.getTag();
		}

		holder.getTextView().setText(complexItem.getLabel());
		holder.getSubtitleTextView().setText(complexItem.getSubtitle());
		if (complexItem.getIcon() != Drawer.NO_ICON) {
			holder.getImageView().setImageResource(complexItem.getIcon());
		} else {
			holder.getImageView().setVisibility(View.GONE);
		}

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
		if (navDrawerSpinner.getListener() != null) {
			holder.getSpinner().setOnItemSelectedListener(navDrawerSpinner.getListener());
		}

		return convertView;
	}

	/**
	 * Creates the view to represent a DrawerSpinner.
	 * @param convertView the view to populate
	 * @param parentView the parent view
	 * @param navDrawerItem the item on which to base the created View
	 * @return a View to be displayed in the NavigationDrawer
	 */
	public View getPaddedDividerView(View convertView, final ViewGroup parentView, final DrawerItem navDrawerItem) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.nav_drawer_padded_divider, parentView, false);
		}

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
