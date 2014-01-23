package com.missionse.uiextensions.navigationdrawer.entry;

import android.widget.ImageView;
import android.widget.TextView;

import com.missionse.uiextensions.navigationdrawer.DrawerItem;
import com.missionse.uiextensions.navigationdrawer.DrawerItemType;

/**
 * Represents a basic menu item for the Navigation Drawer. Typically selects new fragments or activities. Includes an
 * icon, and text.
 */
public final class DrawerSimpleItem extends DrawerItem {

	/**
	 * The Tag to hold all widget information needed to display a DrawerSimpleItem.
	 */
	public static class Holder {
		private TextView mLabel;
		private ImageView mIcon;

		/**
		 * Gets the LabelView for this widget.
		 * @return the LabelView
		 */
		public TextView getTextView() {
			return mLabel;
		}

		/**
		 * Sets the LabelView for this widget.
		 * @param label the LabelView
		 */
		public void setTextView(final TextView label) {
			mLabel = label;
		}

		/**
		 * Gets the ImageView for this widget.
		 * @return the ImageView
		 */
		public ImageView getImageView() {
			return mIcon;
		}

		/**
		 * Sets the ImageView for this widget.
		 * @param icon the ImageView
		 */
		public void setImageView(final ImageView icon) {
			mIcon = icon;
		}
	}

	public static final DrawerItemType TYPE = DrawerItemType.SIMPLE;
	private String mLabel;
	private int mIcon;
	private boolean mWillChangeActionBarTitle;

	private DrawerSimpleItem() {
	}

	/**
	 * Creates a new DrawerSimpleItem.
	 * @param id the id of the new SimpleItem
	 * @param label the label to display
	 * @param iconResource the icon, if available, to display. Use 0 for no icon
	 * @param updateActionBarTitle whether or not this item should update the ActionBar title when selected
	 * @return a new DrawerSimpleItem
	 */
	public static DrawerSimpleItem create(final int id, final String label, final int iconResource,
			final boolean updateActionBarTitle) {
		DrawerSimpleItem item = new DrawerSimpleItem();
		item.setId(id);
		item.setLabel(label);
		item.setIcon(iconResource);
		item.setWillChangeActionBarTitle(updateActionBarTitle);
		return item;
	}

	/**
	 * Gets the displayed String of this item.
	 * @return the label
	 */
	public String getLabel() {
		return mLabel;
	}

	private void setLabel(final String label) {
		mLabel = label;
	}

	@Override
	public DrawerItemType getType() {
		return TYPE;
	}

	@Override
	public boolean isSelectable() {
		return true;
	}

	@Override
	public boolean willChangeActionBarTitle() {
		return mWillChangeActionBarTitle;
	}

	private void setWillChangeActionBarTitle(final boolean change) {
		mWillChangeActionBarTitle = change;
	}

	@Override
	public String getActionBarTitle() {
		return mLabel;
	}

	/**
	 * Returns the icon to be displayed for this item.
	 * @return the icon
	 */
	public int getIcon() {
		return mIcon;
	}

	private void setIcon(final int icon) {
		if (icon != 0) {
			mIcon = icon;
		}
	}
}
