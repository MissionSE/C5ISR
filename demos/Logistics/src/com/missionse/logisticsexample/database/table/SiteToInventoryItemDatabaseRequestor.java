package com.missionse.logisticsexample.database.table;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.missionse.logisticsexample.R;
import com.missionse.logisticsexample.database.DatabaseRequestCompletedListener;
import com.missionse.logisticsexample.database.RemoteDatabaseRequestor;
import com.missionse.logisticsexample.database.LocalDatabaseAccessor;
import com.missionse.logisticsexample.database.RemoteDatabaseAccessor;
import com.missionse.logisticsexample.model.mappings.SiteToInventoryItem;
import com.missionse.logisticsexample.model.orm.SiteToInventoryItemResponse;

/**
 * Provides an implementation of a database requestor for the SitesToInventoryItems table in the database.
 */
public class SiteToInventoryItemDatabaseRequestor implements RemoteDatabaseRequestor {
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
	public SiteToInventoryItemDatabaseRequestor(final Context context, final RemoteDatabaseAccessor remoteDatabaseAccessor,
			final LocalDatabaseAccessor localDatabaseAccessor) {
		mContext = context;
		mRemoteDatabaseAccessor = remoteDatabaseAccessor;
		mLocalDatabaseAccessor = localDatabaseAccessor;

		mUrl = mContext.getString(R.string.get_all_sites_to_inventory_items);
		mName = mContext.getString(R.string.sites_to_inventory_items);
	}

	@Override
	public void fetchAll(final DatabaseRequestCompletedListener requestCompleteListener) {
		mUpdateComplete = false;
		mRemoteDatabaseAccessor.postRequest(mUrl, new TypeToken<SiteToInventoryItemResponse>() { },
				new FutureCallback<SiteToInventoryItemResponse>() {
					@Override
					public void onCompleted(final Exception exception, final SiteToInventoryItemResponse response) {
						if (mRemoteDatabaseAccessor.verifyException(exception, mName)) {
							if (mRemoteDatabaseAccessor.verifyResponse(response, mName)) {
								List<SiteToInventoryItem> sitesToInventoryItems = response.getSitesToInventoryItems();
								for (SiteToInventoryItem siteToInventoryItem : sitesToInventoryItems) {
									try {
										if (mLocalDatabaseAccessor.getObjectDao(SiteToInventoryItem.class)
												.idExists(siteToInventoryItem.getId())) {
											mLocalDatabaseAccessor.getObjectDao(SiteToInventoryItem.class).update(siteToInventoryItem);
										} else {
											mLocalDatabaseAccessor.getObjectDao(SiteToInventoryItem.class).create(siteToInventoryItem);
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
