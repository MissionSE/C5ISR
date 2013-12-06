package com.missionse.uiextensions.navigationdrawer;

import android.widget.TextView;

/**
 * Represents a section header in the Navigation Drawer.
 */
public final class NavigationDrawerHeader extends NavigationDrawerItem {

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

	public static final NavigationDrawerItemType TYPE = NavigationDrawerItemType.HEADER;

	private String mLabel;

	private NavigationDrawerHeader() {
	}

	/**
	 * Creates a new Header.
	 * @param id the id of the header
	 * @param label the label to display
	 * @return a new NavigationDrawerHeader
	 */
	public static NavigationDrawerHeader create(final int id, final String label) {
		NavigationDrawerHeader header = new NavigationDrawerHeader();
		header.setId(id);
		header.setLabel(label);
		return header;
	}

	/**
	 * Retrieves the label for this Header.
	 * @return the label
	 */
	public String getLabel() {
		return mLabel;
	}

	/**
	 * Sets the label to the supplied String.
	 * @param label the label to display for this Header
	 */
	public void setLabel(final String label) {
		this.mLabel = label;
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
