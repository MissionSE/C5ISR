package com.missionse.logisticsexample.database;

import java.sql.SQLException;
import java.util.List;
import java.util.TimerTask;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.missionse.logisticsexample.R;
import com.missionse.logisticsexample.model.InventoryItem;
import com.missionse.logisticsexample.model.ItemName;
import com.missionse.logisticsexample.model.Order;
import com.missionse.logisticsexample.model.OrderItem;
import com.missionse.logisticsexample.model.Site;
import com.missionse.logisticsexample.model.mappings.OrderToOrderItem;
import com.missionse.logisticsexample.model.mappings.SiteToInventoryItem;
import com.missionse.logisticsexample.model.mappings.SiteToOrder;
import com.missionse.logisticsexample.model.orm.InventoryItemResponse;
import com.missionse.logisticsexample.model.orm.ItemNameResponse;
import com.missionse.logisticsexample.model.orm.OrderItemResponse;
import com.missionse.logisticsexample.model.orm.OrderResponse;
import com.missionse.logisticsexample.model.orm.OrderToOrderItemResponse;
import com.missionse.logisticsexample.model.orm.Response;
import com.missionse.logisticsexample.model.orm.SiteResponse;
import com.missionse.logisticsexample.model.orm.SiteToInventoryItemResponse;
import com.missionse.logisticsexample.model.orm.SiteToOrderResponse;

/**
 * Task that will run on a periodic and sync the remote database with the local
 * one.
 */
public class DatabaseUpdateThread extends TimerTask {

	private static final String LOG_TAG = "DatabaseUpadteThread"; 
	
	private OnDatabaseUpdate mUpdater;
	private DatabaseHelper mHelper;

	private String mDatabaseUrl;

	// may be easier/cleaner to use a boolean array
	private boolean mIsSiteComplete = false;
	private boolean mIsOrderComplete = false;
	private boolean mIsNameIdComplete = false;
	private boolean mIsOrderItemComplete = false;
	private boolean mIsInventoryItemComplete = false;
	private boolean mIsSiteToOrderComplete = false;
	private boolean mIsSiteToInventoryItemComplete = false;
	private boolean mIsOrderToOrderItemComplete = false;

	/**
	 * Constructor.
	 * 
	 * @param updater
	 *            - activity the implements the OnDatabaseUpdate
	 * @param helper
	 *            - the DatabaseHelper.
	 */
	public DatabaseUpdateThread(OnDatabaseUpdate updater, DatabaseHelper helper) {
		mUpdater = updater;
		mHelper = helper;
		mDatabaseUrl = updater.getApplicationContext().getString(
				R.string.remote_db_path);
	}

	@Override
	public void run() {
		updateSite();
		updateOrder();
		updateNameId();
		updateOrderItem();
		updateInventoryItem();
		updateSiteToOrder();
		updateSiteToInventoryItem();
		updateOrderToOrderItem();
	}

	private synchronized <T> void requestRemote(String script,
			TypeToken<T> token, FutureCallback<T> callback) {
		Ion.with(mUpdater.getApplicationContext(), mDatabaseUrl + script)
				.as(token).setCallback(callback);
	}
	
	private synchronized boolean argumentsCheck(Exception exception, Response reponse, String tag) {
		if (exception == null && reponse != null) {
			return true;
		} else {
			Log.d(LOG_TAG, "An null reponse recieved by " + tag, exception);
			return false;
		}
		
	}
	
	private synchronized boolean responseCheck(Response response, String tag) {
		if (response.isSuccess()) {
			return true;
		} else {
			Log.d(LOG_TAG, "Update " + tag + " received false success." + " ServerMessage>: " + response.getMessage());
			return false;
		}
	}

	private void updateSite() {
		requestRemote("get_all_sites.php", new TypeToken<SiteResponse>() {
		}, new FutureCallback<SiteResponse>() {
			@Override
			public void onCompleted(Exception exception, SiteResponse response) {
				if (argumentsCheck(exception, response, "Site")) {
					if (responseCheck(response, "Site")) {
						List<Site> sites = response.getSites();
						for (Site site : sites) {
							try {
								if (mHelper.getSiteDao().idExists(site.getId())) {
									mHelper.getSiteDao().update(site);
								} else {
									mHelper.getSiteDao().create(site);
								}
							} catch (SQLException mysqlexception) { mysqlexception.printStackTrace(); }
						}
						
					}
				}
				mIsSiteComplete = true;
				update();
			}
		});
	}

	private void updateOrder() {
		requestRemote("get_all_orders.php", new TypeToken<OrderResponse>() {
		}, new FutureCallback<OrderResponse>() {
			@Override
			public void onCompleted(Exception exception, OrderResponse response) {
				if (argumentsCheck(exception, response, "Order")) {
					if (responseCheck(response, "Order")) {
						List<Order> orders = response.getOrder();
						for (Order order : orders) {
							try {
								if (mHelper.getOrderDao().idExists(order.getId())) {
									mHelper.getOrderDao().update(order);
								} else {
									mHelper.getOrderDao().create(order);
								}
							} catch (SQLException mysqlexception) { mysqlexception.printStackTrace(); }
						}
					}
				}
				mIsOrderComplete = true;
				update();
			}
		});
	}

	private void updateNameId() {
		requestRemote("get_all_item_names.php", new TypeToken<ItemNameResponse>() {
		}, new FutureCallback<ItemNameResponse>() {
			@Override
			public void onCompleted(Exception exception, ItemNameResponse response) {
				if (argumentsCheck(exception, response, "ItemName")) {
					if (responseCheck(response, "ItemName")) {
						List<ItemName> itemNames = response.getItemNames();
						for (ItemName itemName : itemNames) {
							try {
								if (mHelper.getItemNameDao().idExists(itemName.getId())) {
									mHelper.getItemNameDao().update(itemName);
								} else {
									mHelper.getItemNameDao().create(itemName);
								}
							} catch (SQLException mysqlexception) { mysqlexception.printStackTrace(); }
						}
					}
				}
				mIsNameIdComplete = true;
				update();
			}
		});
	}

	private void updateOrderItem() {
		requestRemote("get_all_order_items.php", new TypeToken<OrderItemResponse>() {
		}, new FutureCallback<OrderItemResponse>() {
			@Override
			public void onCompleted(Exception exception, OrderItemResponse response) {
				if (argumentsCheck(exception, response, "OrderItem")) {
					if (responseCheck(response, "OrderItem")) {
						List<OrderItem> items = response.getItems();
						for (OrderItem item : items) {
							try {
								if (mHelper.getOrderItemDao().idExists(item.getId())) {
									mHelper.getOrderItemDao().update(item);
								} else {
									mHelper.getOrderItemDao().create(item);
								}
							} catch (SQLException mysqlexception) { mysqlexception.printStackTrace(); }
						}
					}
				}
				mIsOrderItemComplete = true;
				update();
			}
		});
	}

	private void updateInventoryItem() {
		requestRemote("get_all_inventory_items.php", new TypeToken<InventoryItemResponse>() {
		}, new FutureCallback<InventoryItemResponse>() {
			@Override
			public void onCompleted(Exception exception, InventoryItemResponse response) {
				if (argumentsCheck(exception, response, "InventoryItem")) {
					if (responseCheck(response, "InventoryItem")) {
						List<InventoryItem> items = response.getItems();
						for (InventoryItem item : items) {
							try {
								if (mHelper.getInventoryItemDao().idExists(item.getId())) {
									mHelper.getInventoryItemDao().update(item);
								} else {
									mHelper.getInventoryItemDao().create(item);
								}
							} catch (SQLException mysqlexception) { mysqlexception.printStackTrace(); }
						}
					}
				}
				mIsInventoryItemComplete = true;
				update();
			}
		});
	}

	private void updateSiteToOrder() {
		requestRemote("get_all_sites_to_orders.php", new TypeToken<SiteToOrderResponse>() {
		}, new FutureCallback<SiteToOrderResponse>() {
			@Override
			public void onCompleted(Exception exception, SiteToOrderResponse response) {
				if (argumentsCheck(exception, response, "SiteToOrder")) {
					if (responseCheck(response, "SiteToOrder")) {
						List<SiteToOrder> orders = response.getSiteToOrders();
						for (SiteToOrder order : orders) {
							try {
								if (mHelper.getSiteToOrderDao().idExists(order.getId())) {
									mHelper.getSiteToOrderDao().update(order);
								} else {
									mHelper.getSiteToOrderDao().create(order);
								}
							} catch (SQLException mysqlexception) { mysqlexception.printStackTrace(); }
						}
					}
				}
				mIsSiteToOrderComplete = true;
				update();
			}
		});
	}

	private void updateSiteToInventoryItem() {
		requestRemote("get_all_sites_to_inventory_items.php", new TypeToken<SiteToInventoryItemResponse>() {
		}, new FutureCallback<SiteToInventoryItemResponse>() {
			@Override
			public void onCompleted(Exception exception, SiteToInventoryItemResponse response) {
				if (argumentsCheck(exception, response, "SiteToInventoryItem")) {
					if (responseCheck(response, "SiteToInventoryItem")) {
						List<SiteToInventoryItem> items = response.getSiteToInventoryItem();
						for (SiteToInventoryItem item : items) {
							try {
								if (mHelper.getSiteToInventoryItemDao().idExists(item.getId())) {
									mHelper.getSiteToInventoryItemDao().update(item);
								} else {
									mHelper.getSiteToInventoryItemDao().create(item);
								}
							} catch (SQLException mysqlexception) { mysqlexception.printStackTrace(); }
						}
					}
				}
				mIsSiteToInventoryItemComplete = true;
				update();
			}
		});
	}

	private void updateOrderToOrderItem() {
		requestRemote("get_all_orders_to_order_items.php", new TypeToken<OrderToOrderItemResponse>() {
		}, new FutureCallback<OrderToOrderItemResponse>() {
			@Override
			public void onCompleted(Exception exception, OrderToOrderItemResponse response) {
				if (argumentsCheck(exception, response, "OrderToOrderItem")) {
					if (responseCheck(response, "OrderToOrderItem")) {
						List<OrderToOrderItem> items = response.getOrderItems();
						for (OrderToOrderItem item : items) {
							try {
								if (mHelper.getOrderToOrderItemDao().idExists(item.getId())) {
									mHelper.getOrderToOrderItemDao().update(item);
								} else {
									mHelper.getOrderToOrderItemDao().create(item);
								}
							} catch (SQLException mysqlexception) { mysqlexception.printStackTrace(); }
						}
					}
				}
				mIsOrderToOrderItemComplete = true;
				update();
			}
		});
	}

	private synchronized void update() {
		if (mIsSiteComplete && mIsOrderComplete && mIsNameIdComplete
				&& mIsOrderItemComplete && mIsInventoryItemComplete
				&& mIsSiteToOrderComplete && mIsSiteToInventoryItemComplete
				&& mIsOrderToOrderItemComplete) {
			Log.d("DatabaseUpdateThread", "UpdateComplete");
			mUpdater.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mUpdater.onDatabaseUpdate(mHelper);
				}
			});
			mIsSiteComplete = false;
			mIsOrderComplete = false;
			mIsNameIdComplete = false;
			mIsOrderItemComplete = false;
			mIsInventoryItemComplete = false;
			mIsSiteToOrderComplete = false;
			mIsSiteToInventoryItemComplete = false;
			mIsOrderToOrderItemComplete = false;
		}
	}

}
