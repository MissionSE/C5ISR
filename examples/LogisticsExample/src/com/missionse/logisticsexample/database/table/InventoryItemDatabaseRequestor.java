package com.missionse.logisticsexample.database.table;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.missionse.logisticsexample.R;
import com.missionse.logisticsexample.database.DatabaseConnector;
import com.missionse.logisticsexample.database.DatabaseRequestCompletedListener;
import com.missionse.logisticsexample.database.DatabaseRequestor;
import com.missionse.logisticsexample.database.LocalDatabaseAccessor;
import com.missionse.logisticsexample.model.InventoryItem;
import com.missionse.logisticsexample.model.orm.InventoryItemResponse;

/**
 * Provides an implementation of a database requestor for the InventoryItems table in the database.
 */
public class InventoryItemDatabaseRequestor implements DatabaseRequestor {
	private Context mContext;
	private DatabaseConnector mDatabaseConnector;
	private LocalDatabaseAccessor mDatabaseAccessor;

	private boolean mUpdateComplete;
	private String mUrl;
	private String mName;

	/**
	 * Constructor.
	 * @param context The context of the activity that owns this requestor.
	 * @param databaseConnector A utility class that is used to make remote database calls.
	 * @param databaseAccessor A utility class that is used to make local database calls.
	 */
	public InventoryItemDatabaseRequestor(final Context context, final DatabaseConnector databaseConnector,
			final LocalDatabaseAccessor databaseAccessor) {
		mContext = context;
		mDatabaseConnector = databaseConnector;
		mDatabaseAccessor = databaseAccessor;

		mUrl = mContext.getString(R.string.get_all_inventory_items);
		mName = mContext.getString(R.string.inventory_items);
	}

	@Override
	public void fetchAll(final DatabaseRequestCompletedListener requestCompleteListener) {
		mUpdateComplete = false;
		mDatabaseConnector.postRequest(mUrl, new TypeToken<InventoryItemResponse>() { },
				new FutureCallback<InventoryItemResponse>() {
					@Override
					public void onCompleted(final Exception exception, final InventoryItemResponse response) {
						if (mDatabaseConnector.verifyException(exception, mName)) {
							if (mDatabaseConnector.verifyResponse(response, mName)) {
								List<InventoryItem> inventoryItems = response.getItems();
								for (InventoryItem inventoryItem : inventoryItems) {
									try {
										if (mDatabaseAccessor.getObjectDao(InventoryItem.class).idExists(inventoryItem.getId())) {
											mDatabaseAccessor.getObjectDao(InventoryItem.class).update(inventoryItem);
										} else {
											mDatabaseAccessor.getObjectDao(InventoryItem.class).create(inventoryItem);
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
