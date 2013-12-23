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
import com.missionse.logisticsexample.model.OrderItem;
import com.missionse.logisticsexample.model.orm.OrderItemResponse;

/**
 * Provides an implementation of a database requestor for the OrderItems table in the database.
 */
public class OrderItemDatabaseRequestor implements DatabaseRequestor {
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
	public OrderItemDatabaseRequestor(final Context context, final DatabaseConnector databaseConnector,
			final LocalDatabaseAccessor databaseAccessor) {
		mContext = context;
		mDatabaseConnector = databaseConnector;
		mDatabaseAccessor = databaseAccessor;

		mUrl = mContext.getString(R.string.get_all_order_items);
		mName = mContext.getString(R.string.order_items);
	}

	@Override
	public void fetchAll(final DatabaseRequestCompletedListener requestCompleteListener) {
		mUpdateComplete = false;
		mDatabaseConnector.postRequest(mUrl, new TypeToken<OrderItemResponse>() { },
				new FutureCallback<OrderItemResponse>() {
					@Override
					public void onCompleted(final Exception exception, final OrderItemResponse response) {
						if (mDatabaseConnector.verifyException(exception, mName)) {
							if (mDatabaseConnector.verifyResponse(response, mName)) {
								List<OrderItem> orderItems = response.getItems();
								for (OrderItem orderItem : orderItems) {
									try {
										if (mDatabaseAccessor.getObjectDao(OrderItem.class).idExists(orderItem.getId())) {
											mDatabaseAccessor.getObjectDao(OrderItem.class).update(orderItem);
										} else {
											mDatabaseAccessor.getObjectDao(OrderItem.class).create(orderItem);
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
