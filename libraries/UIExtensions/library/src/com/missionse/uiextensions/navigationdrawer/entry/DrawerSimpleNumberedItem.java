package com.missionse.uiextensions.navigationdrawer.entry;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.missionse.uiextensions.navigationdrawer.Drawer;
import com.missionse.uiextensions.navigationdrawer.DrawerItem;
import com.missionse.uiextensions.navigationdrawer.DrawerItemType;

/**
 * Represents a basic menu item for the Navigation Drawer. Typically selects new fragments or activities. Includes an
 * icon, and text.
 */
public final class DrawerSimpleNumberedItem extends DrawerItem {

	/**
	 * The Tag to hold all widget information needed to display a DrawerSimpleItem.
	 */
	public static class Holder {
		private TextView mLabel;
		private TextView mNumber;
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
		 * Gets the NumberView for this widget.
		 * @return the NumberView
		 */
		public TextView getNumberView() {
			return mNumber;
		}

		/**
		 * Sets the NumberView for this widget.
		 * @param number the NumberView
		 */
		public void setNumberView(final TextView number) {
			mNumber = number;
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

	public static final DrawerItemType TYPE = DrawerItemType.SIMPLENUMBERED;
	private String mLabel;
	private String mNumber;
	private int mIcon = Drawer.NO_ICON;
	private boolean mWillChangeActionBarTitle;

    private int mBackgroundDrawable = Drawer.USE_DEFAULT;
	private int mTextColorStateList = Drawer.USE_DEFAULT;
    private Context mContext;

	private DrawerSimpleNumberedItem() {
	}

	/**
	 * Creates a new DrawerSimpleItem.
	 * @param id the id of the new SimpleItem
	 * @param label the label to display
	 * @param number the number to display
	 * @param iconResource the icon, if available, to display. Use 0 for no icon
	 * @param updateActionBarTitle whether or not this item should update the ActionBar title when selected
	 * @return a new DrawerSimpleItem
	 */
	public static DrawerSimpleNumberedItem create(final int id, final String label, final String number,
			final int iconResource, final boolean updateActionBarTitle) {
		DrawerSimpleNumberedItem item = new DrawerSimpleNumberedItem();
		item.setId(id);
		item.setLabel(label);
		item.setNumber(number);
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
	 * Gets the displayed number of this item.
	 * @return the number
	 */
	public String getNumber() {
		return mNumber;
	}

	/**
	 * Sets the displayed number of this item.
	 * @param number the number to display
	 */
	public void setNumber(final String number) {
		mNumber = number;
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

    /**
     * Returns the resource ID to be used for drawing the background of this item.
     * @return the resource ID
     */
    public int getBackgroundDrawable() {
        return mBackgroundDrawable;
    }

    /**
     * Sets the resource ID to be used when drawing the background of the item.
     * @param resourceID the resource to be used
     * @param context the context in which the resourceID can be found
     */
    public void setBackgroundDrawable(final int resourceID, final Context context) {
        if (resourceID != Drawer.USE_DEFAULT && context != null) {
            mBackgroundDrawable = resourceID;
            mContext = context;
        }
    }

	/**
	 * Returns the Color State List to use when drawing text for this item.
	 * @return the resource ID
	 */
	public int getTextColorStateList() {
		return mTextColorStateList;
	}

	/**
	 * Sets the resource ID to be used when drawing the text of the item.
	 * @param resourceID the resource to be used
	 * @param context the context in which the resourceID can be found
	 */
	public void setTextColorStateList(final int resourceID, final Context context) {
		if (resourceID != Drawer.USE_DEFAULT && context != null) {
			mTextColorStateList = resourceID;
			mContext = context;
		}
	}

    /**
     * Returns the context to use for drawable resource ID resolution.
     * @return the context
     */
    public Context getContext() {
        return mContext;
    }
}
