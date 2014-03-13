package com.missionse.kestrelweather.reports.utils;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.missionse.kestrelweather.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides an implementation of a multiple choice mode listener for selecting lists of media.
 * @param <T> Class that extends ArrayAdapter
 * @param <K> Class that the ArrayAdapter contains.
 */
public class AuxiliaryDataMultiChoiceModeListener<T extends ArrayAdapter<K>, K> implements AbsListView.MultiChoiceModeListener {
	private Context mContext;
	private ListView mListView;
	private T mAdapter;
	private boolean mEditable;

	private ItemRemovedListener<K> mItemRemovedListener;
	private final String mSelectItemsText;
	private final String mItemsSelectedText;

	/**
	 * Constructor.
	 * @param context The current context.
	 * @param listView The view that contains the items.
	 * @param adapter The adapter that handles the item views.
	 * @param editable Whether the list is editable.
	 */
	public AuxiliaryDataMultiChoiceModeListener(final Context context, final ListView listView, final T adapter, final boolean editable) {
		mContext = context;
		mListView = listView;
		mAdapter = adapter;
		mEditable = editable;

		mSelectItemsText = context.getString(R.string.select_items);
		mItemsSelectedText = context.getString(R.string.items_selected);
	}

	/**
	 * Sets the ItemRemovedListener to be notified when an item is removed.
	 * @param itemRemovedListener The listener to be notified.
	 */
	public void setItemRemovedListener(final ItemRemovedListener<K> itemRemovedListener) {
		mItemRemovedListener = itemRemovedListener;
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
			if (mEditable) {
				menuInflater.inflate(R.menu.report_delete_item, menu);
			}
		}

		return true;
	}

	@Override
	public boolean onActionItemClicked(final ActionMode actionMode, final MenuItem menuItem) {
		int id = menuItem.getItemId();
		if (id == R.id.action_delete_selected) {
			SparseBooleanArray checkedItems = mListView.getCheckedItemPositions();
			if (checkedItems != null) {
				List<K> selectedItems = new ArrayList<K>();
				for (int index = checkedItems.size() - 1; index >= 0; index--) {
					int position = checkedItems.keyAt(index);
					if (checkedItems.valueAt(position)) {
						selectedItems.add(mAdapter.getItem(position));
						mListView.setItemChecked(position, false);
					}
				}

				for (K item : selectedItems) {
					mAdapter.remove(item);
					if (mItemRemovedListener != null) {
						mItemRemovedListener.itemRemoved(item);
					}
				}

				actionMode.finish();
			}
		}

		return true;
	}

	@Override
	public void onDestroyActionMode(final ActionMode actionMode) {
	}

	protected Context getContext() {
		return mContext;
	}

	protected ListView getListView() {
		return mListView;
	}

	protected T getAdapter() {
		return mAdapter;
	}
}
