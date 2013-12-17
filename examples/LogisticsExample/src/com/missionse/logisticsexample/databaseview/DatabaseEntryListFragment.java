package com.missionse.logisticsexample.databaseview;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.missionse.logisticsexample.R;

/**
 * Generic fragment to display a list of items, with search field callbacks, given a supplied adapter.
 */
public abstract class DatabaseEntryListFragment extends Fragment {

	private EditText mSearchField;
	private StickyListHeadersListView mEntryList;

	/**
	 * Retrieves the created search field.
	 * @return the edit text widget
	 */
	public EditText getSearchField() {
		return mSearchField;
	}

	/**
	 * Retrieves the created entry list.
	 * @return the list view widget
	 */
	public StickyListHeadersListView getEntryList() {
		return mEntryList;
	}

	/**
	 * Retrieves the adapter used to populate the entry list. To be used by implementing classes to provider the proper
	 * adapter.
	 * @return the adapter used to populate the entry list
	 */
	public abstract StickyListHeadersAdapter getEntryAdapter();

	/**
	 * Retrieves the Text Watcher to be used on text entry changes in the search field.
	 * @return the text watcher
	 */
	public abstract TextWatcher getTextWatcher();

	/**
	 * Callback denoting when an item in the entry list has been selected.
	 * @param position the position of the item that was selected
	 */
	public abstract void onItemSelected(int position);

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup parent, final Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_database_entry_list, parent, false);
		mSearchField = (EditText) contentView.findViewById(R.id.search_field);
		mSearchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(final TextView v, final int actionId, final KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(
							Context.INPUT_METHOD_SERVICE);
					inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
					return true;
				}
				return false;
			}
		});

		mSearchField.addTextChangedListener(getTextWatcher());

		mEntryList = (StickyListHeadersListView) contentView.findViewById(R.id.entry_list);
		mEntryList.setAdapter(getEntryAdapter());
		mEntryList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
				mEntryList.setItemChecked(position, true);
				onItemSelected(position);
			}
		});

		return contentView;
	}
}
