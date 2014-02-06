package com.missionse.uiextensions.navigationdrawer.entry;

import android.widget.ImageView;
import android.widget.TextView;

import com.missionse.uiextensions.navigationdrawer.Drawer;
import com.missionse.uiextensions.navigationdrawer.DrawerItem;
import com.missionse.uiextensions.navigationdrawer.DrawerItemType;

/**
 * Represents a basic menu item for the Navigation Drawer. Typically selects new fragments or activities. Includes an
 * icon, and text.
 */
public final class DrawerComplexItem extends DrawerItem {

	/**
	 * The Tag to hold all widget information needed to display a DrawerSimpleItem.
	 */
	public static class Holder {
		private TextView mLabel;
		private TextView mSubtitle;
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
		 * Gets the LabelView for this widget.
		 * @return the LabelView
		 */
		public TextView getSubtitleTextView() {
			return mSubtitle;
		}

		/**
		 * Sets the LabelView for this widget.
		 * @param subtitle the LabelView
		 */
		public void setSubtitleTextView(final TextView subtitle) {
			mSubtitle = subtitle;
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

	public static final DrawerItemType TYPE = DrawerItemType.COMPLEX;
	private String mLabel;
	private String mSubtitle;
	private int mIcon = Drawer.NO_ICON;
	private boolean mWillChangeActionBarTitle;

	private DrawerComplexItem() {
	}

	/**
	 * Creates a new DrawerSimpleItem.
	 * @param id the id of the new SimpleItem
	 * @param label the label to display
	 * @param subtitle the subtitle to display
	 * @param iconResource the icon, if available, to display. Use 0 for no icon
	 * @param updateActionBarTitle whether or not this item should update the ActionBar title when selected
	 * @return a new DrawerSimpleItem
	 */
	public static DrawerComplexItem create(final int id, final String label, final String subtitle,
			final int iconResource, final boolean updateActionBarTitle) {
		DrawerComplexItem item = new DrawerComplexItem();
		item.setId(id);
		item.setLabel(label);
		item.setSubtitle(subtitle);
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

	/**
	 * Gets the displayed subtitle of this item.
	 * @return the subtitle
	 */
	public String getSubtitle() {
		return mSubtitle;
	}

	private void setSubtitle(final String subtitle) {
		mSubtitle = subtitle;
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
		if (icon != Drawer.NO_ICON) {
			mIcon = icon;
		}
	}
}
