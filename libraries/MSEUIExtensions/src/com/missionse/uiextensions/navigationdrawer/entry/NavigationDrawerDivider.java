package com.missionse.uiextensions.navigationdrawer.entry;

import android.widget.TextView;

import com.missionse.uiextensions.navigationdrawer.NavigationDrawerItem;
import com.missionse.uiextensions.navigationdrawer.NavigationDrawerItemType;

/**
 * Represents a section header in the Navigation Drawer.
 */
public final class NavigationDrawerDivider extends NavigationDrawerItem {

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

	public static final NavigationDrawerItemType TYPE = NavigationDrawerItemType.DIVIDER;

	private NavigationDrawerDivider() {
	}

	/**
	 * Creates a new Divider.
	 * @param id the id of the divider
	 * @return a new NavigationDrawerDivider
	 */
	public static NavigationDrawerDivider create(final int id) {
		NavigationDrawerDivider divider = new NavigationDrawerDivider();
		divider.setId(id);
		return divider;
	}

	@Override
	public NavigationDrawerItemType getType() {
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
