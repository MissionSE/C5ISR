package com.missionse.kestrelweather.reports.utils;

import android.content.Context;
import android.net.Uri;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

import com.missionse.kestrelweather.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides an implementation of a multiple choice mode listener for selecting lists of media.
 */
public class MediaMultiChoiceModeListener implements AbsListView.MultiChoiceModeListener {
	private ListView mListView;
	private UriAdapter mAdapter;

	private final String mSelectItemsText;
	private final String mItemsSelectedText;

	/**
	 * Constructor.
	 * @param context The current context.
	 * @param listView The view that contains the items.
	 * @param adapter The adapter that handles the item views.
	 */
	public MediaMultiChoiceModeListener(final Context context, final ListView listView, final UriAdapter adapter) {
		mListView = listView;
		mAdapter = adapter;

		mSelectItemsText = context.getString(R.string.select_items);
		mItemsSelectedText = context.getString(R.string.items_selected);
	}

	@Override
	public void onItemCheckedStateChanged(final ActionMode actionMode, final int position, final long id, final boolean checked) {
		int selectedItemCount = mListView.getCheckedItemCount();
		actionMode.setSubtitle(selectedItemCount + " " + mItemsSelectedText);
	}

	@Override
	public boolean onPrepareActionMode(final ActionMode actionMode, final Menu menu) {
		return true;
	}

	@Override
	public boolean onCreateActionMode(final ActionMode actionMode, final Menu menu) {
		actionMode.setTitle(mSelectItemsText);
		actionMode.setSubtitle("1 " + mItemsSelectedText);

		MenuInflater menuInflater = actionMode.getMenuInflater();
		if (menuInflater != null) {
			menuInflater.inflate(R.menu.report_action_bar, menu);
		}

		return true;
	}

	@Override
	public boolean onActionItemClicked(final ActionMode actionMode, final MenuItem menuItem) {
		if (menuItem.getItemId() == R.id.action_delete_selected) {
			SparseBooleanArray checkedItems = mListView.getCheckedItemPositions();
			if (checkedItems != null) {
				List<Uri> selectedUris = new ArrayList<Uri>();
				for (int index = checkedItems.size() - 1; index >= 0; index--) {
					int position = checkedItems.keyAt(index);
					if (checkedItems.valueAt(position)) {
						selectedUris.add(mAdapter.getItem(position));
						mListView.setItemChecked(position, false);
					}
				}

				for (Uri uri : selectedUris) {
					mAdapter.remove(uri);
				}

				actionMode.finish();
			}
		}
		return true;
	}

	@Override
	public void onDestroyActionMode(final ActionMode actionMode) {
	}
}