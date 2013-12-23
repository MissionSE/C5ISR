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
import com.missionse.logisticsexample.model.mappings.SiteToInventoryItem;
import com.missionse.logisticsexample.model.orm.SiteToInventoryItemResponse;

/**
 * Provides an implementation of a database requestor for the SitesToInventoryItems table in the database.
 */
public class SiteToInventoryItemDatabaseRequestor implements DatabaseRequestor {
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
	public SiteToInventoryItemDatabaseRequestor(final Context context, final DatabaseConnector databaseConnector,
			final LocalDatabaseAccessor databaseAccessor) {
		mContext = context;
		mDatabaseConnector = databaseConnector;
		mDatabaseAccessor = databaseAccessor;

		mUrl = mContext.getString(R.string.get_all_sites_to_inventory_items);
		mName = mContext.getString(R.string.sites_to_inventory_items);
	}

	@Override
	public void fetchAll(final DatabaseRequestCompletedListener requestCompleteListener) {
		mUpdateComplete = false;
		mDatabaseConnector.postRequest(mUrl, new TypeToken<SiteToInventoryItemResponse>() { },
				new FutureCallback<SiteToInventoryItemResponse>() {
					@Override
					public void onCompleted(final Exception exception, final SiteToInventoryItemResponse response) {
						if (mDatabaseConnector.verifyException(exception, mName)) {
							if (mDatabaseConnector.verifyResponse(response, mName)) {
								List<SiteToInventoryItem> sitesToInventoryItems = response.getSitesToInventoryItems();
								for (SiteToInventoryItem siteToInventoryItem : sitesToInventoryItems) {
									try {
										if (mDatabaseAccessor.getObjectDao(SiteToInventoryItem.class)
												.idExists(siteToInventoryItem.getId())) {
											mDatabaseAccessor.getObjectDao(SiteToInventoryItem.class).update(siteToInventoryItem);
										} else {
											mDatabaseAccessor.getObjectDao(SiteToInventoryItem.class).create(siteToInventoryItem);
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
