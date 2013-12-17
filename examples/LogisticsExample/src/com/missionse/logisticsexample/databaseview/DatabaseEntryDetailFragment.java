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
	private TextView mEntryContentView;
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
	 * Sets the content text to be displayed.
	 * @param contentText the text to be displayed in the content slot
	 */
	public void setContent(final CharSequence contentText) {
		mEntryContentView.setText(contentText);
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

		mEntryContentView = (TextView) contentView.findViewById(R.id.entry_detail_content);

		mNothingSelectedView = (TextView) contentView.findViewById(R.id.nothing_selected);

		mEntryDetailContent = (LinearLayout) contentView.findViewById(R.id.entry_detail_container);

		return contentView;
	}
}
