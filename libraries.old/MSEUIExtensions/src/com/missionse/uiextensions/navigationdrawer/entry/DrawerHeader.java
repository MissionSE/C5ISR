package com.missionse.uiextensions.navigationdrawer.entry;

import android.widget.TextView;

import com.missionse.uiextensions.navigationdrawer.DrawerItem;
import com.missionse.uiextensions.navigationdrawer.DrawerItemType;

/**
 * Represents a section header in the Navigation Drawer.
 */
public final class DrawerHeader extends DrawerItem {

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

	public static final DrawerItemType TYPE = DrawerItemType.HEADER;

	private String mLabel;

	private DrawerHeader() {
	}

	/**
	 * Creates a new Header.
	 * @param id the id of the header
	 * @param label the label to display
	 * @return a new DrawerHeader
	 */
	public static DrawerHeader create(final int id, final String label) {
		DrawerHeader header = new DrawerHeader();
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

	private void setLabel(final String label) {
		this.mLabel = label;
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
