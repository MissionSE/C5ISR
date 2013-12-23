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
import com.missionse.logisticsexample.model.mappings.SiteToOrder;
import com.missionse.logisticsexample.model.orm.SiteToOrderResponse;

/**
 * Provides an implementation of a database requestor for the SitesToOrders table in the database.
 */
public class SiteToOrderDatabaseRequestor implements DatabaseRequestor {
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
	public SiteToOrderDatabaseRequestor(final Context context, final DatabaseConnector databaseConnector,
			final LocalDatabaseAccessor databaseAccessor) {
		mContext = context;
		mDatabaseConnector = databaseConnector;
		mDatabaseAccessor = databaseAccessor;

		mUrl = mContext.getString(R.string.get_all_sites_to_orders);
		mName = mContext.getString(R.string.sites_to_orders);
	}

	@Override
	public void fetchAll(final DatabaseRequestCompletedListener requestCompleteListener) {
		mUpdateComplete = false;
		mDatabaseConnector.postRequest(mUrl, new TypeToken<SiteToOrderResponse>() { },
				new FutureCallback<SiteToOrderResponse>() {
					@Override
					public void onCompleted(final Exception exception, final SiteToOrderResponse response) {
						if (mDatabaseConnector.verifyException(exception, mName)) {
							if (mDatabaseConnector.verifyResponse(response, mName)) {
								List<SiteToOrder> sitesToOrders = response.getSitesToOrders();
								for (SiteToOrder siteToOrder : sitesToOrders) {
									try {
										if (mDatabaseAccessor.getObjectDao(SiteToOrder.class).idExists(siteToOrder.getId())) {
											mDatabaseAccessor.getObjectDao(SiteToOrder.class).update(siteToOrder);
										} else {
											mDatabaseAccessor.getObjectDao(SiteToOrder.class).create(siteToOrder);
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
