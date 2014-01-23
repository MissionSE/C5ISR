package com.missionse.uiextensions.navigationdrawer.entry;

import android.widget.TextView;

import com.missionse.uiextensions.navigationdrawer.DrawerItem;
import com.missionse.uiextensions.navigationdrawer.DrawerItemType;

/**
 * Represents a section header in the Navigation Drawer.
 */
public final class DrawerDivider extends DrawerItem {

	/**
	 * Tag object holding all widgets necessary to display this item in a NavigationDrawer.
	 */
	public static class Holder {
		private TextView mLabel;

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
	}

	public static final DrawerItemType TYPE = DrawerItemType.DIVIDER;

	private DrawerDivider() {
	}

	/**
	 * Creates a new Divider.
	 * @param id the id of the divider
	 * @return a new DrawerPaddedDivider
	 */
	public static DrawerDivider create(final int id) {
		DrawerDivider divider = new DrawerDivider();
		divider.setId(id);
		return divider;
	}

	@Override
	public DrawerItemType getType() {
		return TYPE;
	}

	@Override
	public boolean isSelectable() {
		return false;
	}

	@Override
	public boolean willChangeActionBarTitle() {
		return false;
	}

	@Override
	public String getActionBarTitle() {
		return null;
	}
}
