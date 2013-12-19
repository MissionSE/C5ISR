package com.missionse.logisticsexample.databaseview;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.missionse.logisticsexample.R;

/**
 * A generic fragment that instantiates and changes views displaying details about an entry.
 */
public abstract class DatabaseEntryDetailFragment extends Fragment {

	private TextView mEntryTitleView;
	private TextView mEntryTitleIdView;

	private TextView mEntryContentBasicView;
	private TextView mEntryContentParentView;
	private TextView mEntryContentInventoryView;
	private TextView mEntryContentOrdersView;

	private TextView mNothingSelectedView;

	private LinearLayout mEntryDetailContent;

	/**
	 * Sets the title text to be displayed.
	 * @param titleText the text to be displayed in the title slot
	 */
	public void setTitleText(final CharSequence titleText) {
		mEntryTitleView.setText(titleText);
	}

	/**
	 * Sets the id to appear in the title.
	 * @param idText the id to be displayed
	 */
	public void setTitleId(final CharSequence idText) {
		mEntryTitleIdView.setText(idText);
	}

	/**
	 * Sets the basic content text to be displayed.
	 * @param basicContentText the text to be displayed in the content slot
	 */
	public void setBasicContent(final CharSequence basicContentText) {
		mEntryContentBasicView.setText(basicContentText);
	}

	/**
	 * Sets the parent content text to be displayed.
	 * @param parentContentText the text to be displayed in the content slot
	 */
	public void setParentContent(final CharSequence parentContentText) {
		mEntryContentParentView.setText(parentContentText);
	}

	/**
	 * Sets the inventory content text to be displayed.
	 * @param inventoryContentText the text to be displayed in the content slot
	 */
	public void setInventoryContent(final CharSequence inventoryContentText) {
		mEntryContentInventoryView.setText(inventoryContentText);
	}

	/**
	 * Sets the order content text to be displayed.
	 * @param orderContentText the text to be displayed in the content slot
	 */
	public void setOrdersContent(final CharSequence orderContentText) {
		mEntryContentOrdersView.setText(orderContentText);
	}

	/**
	 * Displays the view for when nothing has been selected (i.e., no details are being shown).
	 */
	public void showNothingSelectedView() {
		mEntryDetailContent.setVisibility(View.GONE);
		mNothingSelectedView.setVisibility(View.VISIBLE);
	}

	/**
	 * Displays the views associated with displaying details.
	 */
	public void showContentViews() {
		mEntryDetailContent.setVisibility(View.VISIBLE);
		mNothingSelectedView.setVisibility(View.GONE);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup parent, final Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_database_entry_detail, parent, false);

		mEntryTitleView = (TextView) contentView.findViewById(R.id.entry_detail_title);
		mEntryTitleIdView = (TextView) contentView.findViewById(R.id.entry_detail_id);

		mEntryContentBasicView = (TextView) contentView.findViewById(R.id.entry_detail_basic_info);
		mEntryContentParentView = (TextView) contentView.findViewById(R.id.entry_detail_parent_info);
		mEntryContentInventoryView = (TextView) contentView.findViewById(R.id.entry_detail_inventory);
		mEntryContentOrdersView = (TextView) contentView.findViewById(R.id.entry_detail_orders);

		mNothingSelectedView = (TextView) contentView.findViewById(R.id.nothing_selected);

		mEntryDetailContent = (LinearLayout) contentView.findViewById(R.id.entry_detail_container);

		return contentView;
	}
}
