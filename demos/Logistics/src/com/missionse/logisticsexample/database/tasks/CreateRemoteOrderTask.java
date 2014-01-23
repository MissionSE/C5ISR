package com.missionse.logisticsexample.database.tasks;

import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

import com.koushikdutta.async.future.FutureCallback;
import com.missionse.logisticsexample.database.LocalDatabaseHelper;
import com.missionse.logisticsexample.model.Order;
import com.missionse.logisticsexample.model.OrderItem;
import com.missionse.logisticsexample.model.mappings.OrderToOrderItem;
import com.missionse.logisticsexample.model.mappings.SiteToOrder;
import com.missionse.logisticsexample.model.orm.CreateResponse;

/**
 * Task to create an Order in the remote database.
 */
public class CreateRemoteOrderTask extends AsyncTask<Void, Void, Void> {
	private static final String LOG_TAG = CreateRemoteOrderTask.class.getSimpleName();
	
	private Order mOrder;
	private SiteToOrder mSiteToOrder;
	private List<OrderItem> mOrderItems;
	private LocalDatabaseHelper mDatabase;

	/**
	 * Constructor.
	 * 
	 * @param order
	 *            The {@link Order} to create.
	 * @param siteToOrder
	 *            The {@link SiteToOrder} to create.
	 * @param orderItems
	 *            The list of {@link OrderItem}s to create.
	 * @param database
	 *            The database accessor.
	 */
	public CreateRemoteOrderTask(Order order, SiteToOrder siteToOrder, List<OrderItem> orderItems, LocalDatabaseHelper database) {
		mOrder = order;
		mSiteToOrder = siteToOrder;
		mOrderItems = orderItems;
		mDatabase = database;
	}

	@Override
	protected Void doInBackground(Void... args) {
		createOrder();
		return null;
	}
	
	/*
	 * OrderItems and SiteToOrder require the orderId, so I obtain the orderId
	 * first then kick of the tasks to create the SiteToOrder and OrderItems.
	 */
	private void createOrder() {
		mDatabase.create(mOrder, new FutureCallback<CreateResponse>() {
			@Override
			public void onCompleted(Exception exception, CreateResponse response) {
				if (exception == null) {
					if ((response != null) && (response.getId() > 0)) {
						mOrder.setId(response.getId());
						mSiteToOrder.setOrderId(response.getId());
						createSiteToOrder();
						createOrderItems();
					}
				} else {
					Log.e(LOG_TAG, "Error occured when posting request(Order)", exception);
				}
			}
		});
	}
	
	private void createSiteToOrder() {
		mDatabase.create(mSiteToOrder, new FutureCallback<CreateResponse>() {
			@Override
			public void onCompleted(Exception exception, CreateResponse response) {
				if (exception == null) {
					if ((response != null) && (response.getId() > 0)) {
						mSiteToOrder.setId(response.getId());
					}
				} else {
					Log.e(LOG_TAG, "Error occured when posting request(siteToOrder)", exception);
				}
			}
		});
	}

	private void createOrderItems() {
		for (OrderItem item : mOrderItems) {
			mDatabase.create(item, new FutureCallback<CreateResponse>() {
				@Override
				public void onCompleted(Exception exception, CreateResponse response) {
					if (exception == null) {
						if ((response != null) && (response.getId() > 0)) {
							createOrderToOrderItem(response.getId());
						}
					} else {
						Log.e(LOG_TAG, "Error occured when posting request(orderItem)", exception);
					}
				}
			});
		}
	}

	/*
	 * I am using synchronized here because this method will be called by
	 * different AsyncTasks.
	 */
	private synchronized void createOrderToOrderItem(int itemId) {
		OrderToOrderItem orderToOrderItem = new OrderToOrderItem();
		orderToOrderItem.setItemId(itemId);
		orderToOrderItem.setOrderId(mOrder.getId());
		mDatabase.create(orderToOrderItem, new FutureCallback<CreateResponse>() {
			@Override
			public void onCompleted(Exception exception, CreateResponse response) {
				if (exception != null) {
					Log.e(LOG_TAG, "Error occured when posting request(orderToOrderItem)", exception);
				}
			}
		});
	}
}
