package com.missionse.kestrelweather.reports.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.model.tables.Supplement;

/**
 * Provides a multi choice mode listener for supplements.
 */
public class SupplementMultiChoiceModeListener extends AuxiliaryDataMultiChoiceModeListener<SupplementAdapter, Supplement> {
	/**
	 * Constructor.
	 * @param context The current context.
	 * @param listView The view that contains the items.
	 * @param adapter The adapter that handles the item views.
	 * @param editable Whether the list is editable.
	 */
	public SupplementMultiChoiceModeListener(final Context context, final ListView listView,
			final SupplementAdapter adapter, final boolean editable) {
		super(context, listView, adapter, editable);
	}

	@Override
	public boolean onCreateActionMode(final ActionMode actionMode, final Menu menu) {
		super.onCreateActionMode(actionMode, menu);

		MenuInflater menuInflater = actionMode.getMenuInflater();
		if (menuInflater != null) {
			menuInflater.inflate(R.menu.report_download_item, menu);
		}

		return true;
	}

	@Override
	public boolean onActionItemClicked(final ActionMode actionMode, final MenuItem menuItem) {
		super.onActionItemClicked(actionMode, menuItem);

		if (menuItem.getItemId() == R.id.action_download_selected) {
			SparseBooleanArray checkedItems = getListView().getCheckedItemPositions();
			if (checkedItems != null) {
				DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
				if (downloadManager != null) {
					Toast.makeText(getContext(),
							getContext().getString(R.string.download_toast, getListView().getCheckedItemCount()),
							Toast.LENGTH_SHORT).show();
					for (int index = checkedItems.size() - 1; index >= 0; index--) {
						int position = checkedItems.keyAt(index);
						if (checkedItems.valueAt(position)) {
							Supplement supplement = getAdapter().getItem(position);
							String remoteUri = supplement.getRemoteUri();
							if (remoteUri != null && remoteUri.length() > 0) {
								String remoteServer = getContext().getString(R.string.remote_server_development);
								DownloadManager.Request downloadRequest = new DownloadManager.Request(Uri.parse(remoteServer + remoteUri));
								downloadRequest.allowScanningByMediaScanner();
								downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
								downloadRequest.setVisibleInDownloadsUi(true);
								downloadRequest.setDestinationInExternalPublicDir(
										Environment.DIRECTORY_DOWNLOADS, supplement.getFileName());
								downloadManager.enqueue(downloadRequest);
							}
							getListView().setItemChecked(position, false);
						}
					}
				}

				actionMode.finish();
			}
		}

		return true;
	}
}
