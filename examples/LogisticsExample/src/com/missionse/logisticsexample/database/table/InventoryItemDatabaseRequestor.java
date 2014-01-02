package com.missionse.logisticsexample.database.table;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.missionse.logisticsexample.R;
import com.missionse.logisticsexample.database.RemoteDatabaseAccessor;
import com.missionse.logisticsexample.database.DatabaseRequestCompletedListener;
import com.missionse.logisticsexample.database.RemoteDatabaseRequestor;
import com.missionse.logisticsexample.database.LocalDatabaseAccessor;
import com.missionse.logisticsexample.model.InventoryItem;
import com.missionse.logisticsexample.model.orm.InventoryItemResponse;

/**
 * Provides an implementation of a database requestor for the InventoryItems table in the database.
 */
public class InventoryItemDatabaseRequestor implements RemoteDatabaseRequestor {
	private Context mContext;
	private RemoteDatabaseAccessor mRemoteDatabaseAccessor;
	private LocalDatabaseAccessor mLocalDatabaseAccessor;

	private boolean mUpdateComplete;
	private String mUrl;
	private String mName;

	/**
	 * Constructor.
	 * @param context The context of the activity that owns this requestor.
	 * @param remoteDatabaseAccessor A utility class that is used to make remote database calls.
	 * @param localDatabaseAccessor A utility class that is used to make local database calls.
	 */
	public InventoryItemDatabaseRequestor(final Context context, final RemoteDatabaseAccessor remoteDatabaseAccessor,
			final LocalDatabaseAccessor localDatabaseAccessor) {
		mContext = context;
		mRemoteDatabaseAccessor = remoteDatabaseAccessor;
		mLocalDatabaseAccessor = localDatabaseAccessor;

		mUrl = mContext.getString(R.string.get_all_inventory_items);
		mName = mContext.getString(R.string.inventory_items);
	}

	@Override
	public void fetchAll(final DatabaseRequestCompletedListener requestCompleteListener) {
		mUpdateComplete = false;
		mRemoteDatabaseAccessor.postRequest(mUrl, new TypeToken<InventoryItemResponse>() { },
				new FutureCallback<InventoryItemResponse>() {
					@Override
					public void onCompleted(final Exception exception, final InventoryItemResponse response) {
						if (mRemoteDatabaseAccessor.verifyException(exception, mName)) {
							if (mRemoteDatabaseAccessor.verifyResponse(response, mName)) {
								List<InventoryItem> inventoryItems = response.getItems();
								for (InventoryItem inventoryItem : inventoryItems) {
									try {
										if (mLocalDatabaseAccessor.getObjectDao(InventoryItem.class).idExists(inventoryItem.getId())) {
											mLocalDatabaseAccessor.getObjectDao(InventoryItem.class).update(inventoryItem);
										} else {
											mLocalDatabaseAccessor.getObjectDao(InventoryItem.class).create(inventoryItem);
										}
									} catch (SQLException mysqlException) {
										mysqlException.printStackTrace();
									}
								}
							}
						}

						mUpdateComplete = true;
						requestCompleteListener.databaseRequestCompleted();
					}
				});
	}

	@Override
	public boolean hasCompleted() {
		return mUpdateComplete;
	}
}
