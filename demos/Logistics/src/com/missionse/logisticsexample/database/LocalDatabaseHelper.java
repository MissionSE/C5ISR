package com.missionse.logisticsexample.database;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.missionse.logisticsexample.R;
import com.missionse.logisticsexample.model.InventoryItem;
import com.missionse.logisticsexample.model.ItemName;
import com.missionse.logisticsexample.model.Order;
import com.missionse.logisticsexample.model.OrderItem;
import com.missionse.logisticsexample.model.SeverityName;
import com.missionse.logisticsexample.model.Site;
import com.missionse.logisticsexample.model.StatusName;
import com.missionse.logisticsexample.model.mappings.OrderToOrderItem;
import com.missionse.logisticsexample.model.mappings.SiteToInventoryItem;
import com.missionse.logisticsexample.model.mappings.SiteToOrder;
import com.missionse.logisticsexample.model.orm.CreateResponse;

/**
 * Provides a helper utility to manipulate the tables in the database.
 */
public class LocalDatabaseHelper {
	private static final String LOG_TAG = LocalDatabaseHelper.class.getSimpleName();

	private LocalDatabaseAccessor mLocalDatabaseAccessor;
	private RemoteDatabaseAccessor mRemoteDatabaseAccessor;

	private String mSiteIdTag;
	private String mOrderIdTag;

	/**
	 * Constructor.
	 *
	 * @param context
	 *            The context of the activity that owns the helper.
	 * @param databaseAccessor
	 *            An accessor to the local database.
	 */
	public LocalDatabaseHelper(final Context context, final LocalDatabaseAccessor databaseAccessor) {
		mLocalDatabaseAccessor = databaseAccessor;
		mRemoteDatabaseAccessor = new RemoteDatabaseAccessor(context);
		mSiteIdTag = context.getString(R.string.tag_site_id);
		mOrderIdTag = context.getString(R.string.tag_order_id);
	}

	/**
	 * Gets the list of all sites from the database.
	 *
	 * @return The list of all sites in the database.
	 */
	public List<Site> getSites() {
		return mLocalDatabaseAccessor.fetchAll(Site.class);
	}

	/**
	 * Gets the list of all orders from the database.
	 *
	 * @return The list of all orders in the database.
	 */
	public List<Order> getOrders() {
		return mLocalDatabaseAccessor.fetchAll(Order.class);
	}

	/**
	 * Gets the list of all itemnames from the database.
	 *
	 * @return The list of all item names in the database.
	 */
	public List<ItemName> getItemNames() {
		return mLocalDatabaseAccessor.fetchAll(ItemName.class);
	}

	/**
	 * Gets the list of all severity names from the database.
	 *
	 * @return The list of all severity names in the database.
	 */
	public List<SeverityName> getSeverityNames() {
		return mLocalDatabaseAccessor.fetchAll(SeverityName.class);
	}

	/**
	 * Gets the list of all status names from the database.
	 *
	 * @return The list of all status names in the database.
	 */
	public List<StatusName> getStatusNames() {
		return mLocalDatabaseAccessor.fetchAll(StatusName.class);
	}

	/**
	 * @param order
	 *            The order that is associated with a Site.
	 * @return The Site associated with the Order.
	 */
	public Site getSiteAssociatedWith(final Order order) {
		Site site = null;
		try {
			List<SiteToOrder> siteToOrder = mLocalDatabaseAccessor.getObjectDao(SiteToOrder.class).queryBuilder().where()
					.eq(mOrderIdTag, order.getId()).query();
			site = mLocalDatabaseAccessor.getObjectDao(Site.class).queryForId(siteToOrder.get(0).getSiteId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return site;
	}

	/**
	 * @param nameId
	 *            the ID of the name
	 * @return the ItemName associated with the id. Null if name_id could not be
	 *         found.
	 */
	public ItemName getItemName(final int nameId) {
		ItemName itemName = null;
		try {
			itemName = mLocalDatabaseAccessor.getObjectDao(ItemName.class).queryForId(nameId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return itemName;
	}

	/**
	 * @param nameId
	 *            the ID of the name
	 * @return the StatusName associated with the id. Null if name_id could not
	 *         be found.
	 */
	public StatusName getStatusName(final int nameId) {
		StatusName statusName = null;
		try {
			statusName = mLocalDatabaseAccessor.getObjectDao(StatusName.class).queryForId(nameId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return statusName;
	}

	/**
	 * @param nameId
	 *            the ID of the name
	 * @return the SeverityName associated with the id. Null if name_id could
	 *         not be found.
	 */
	public SeverityName getSeverityName(final int nameId) {
		SeverityName severityName = null;
		try {
			severityName = mLocalDatabaseAccessor.getObjectDao(SeverityName.class).queryForId(nameId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return severityName;
	}

	/**
	 * Gets the parent of a site.
	 *
	 * @param site
	 *            The site with a parent.
	 * @return The parent of the site or null if there is none.
	 */
	public Site getSiteParent(final Site site) {
		Site parent = null;
		try {
			parent = mLocalDatabaseAccessor.getObjectDao(Site.class).queryForId(site.getParentId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return parent;
	}

	/**
	 * Gets a list of inventory items at a site.
	 *
	 * @param site
	 *            The site that contains the items.
	 * @return The list of inventory items.
	 */
	public List<InventoryItem> getInventoryItems(final Site site) {
		List<InventoryItem> inventoryItems = new LinkedList<InventoryItem>();
		try {
			List<SiteToInventoryItem> siteToInvItem = mLocalDatabaseAccessor.getObjectDao(SiteToInventoryItem.class).queryBuilder().where()
					.eq(mSiteIdTag, site.getId()).query();
			for (SiteToInventoryItem stii : siteToInvItem) {
				inventoryItems.add(mLocalDatabaseAccessor.getObjectDao(InventoryItem.class).queryForId(stii.getItemId()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return inventoryItems;
	}

	/**
	 * Get a list of orders for an associated site.
	 *
	 * @param site
	 *            The site that created the orders.
	 * @return The list of orders.
	 */
	public List<Order> getOrders(final Site site) {
		List<Order> orders = new LinkedList<Order>();
		try {
			List<SiteToOrder> siteToOrder = mLocalDatabaseAccessor.getObjectDao(SiteToOrder.class).queryBuilder().where()
					.eq(mSiteIdTag, site.getId()).query();
			for (SiteToOrder sto : siteToOrder) {
				orders.add(mLocalDatabaseAccessor.getObjectDao(Order.class).queryForId(sto.getOrderId()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orders;
	}

	/**
	 * Get a list of items in an order.
	 *
	 * @param order
	 *            The order that contains the items.
	 * @return The list of items in the order.
	 */
	public List<OrderItem> getOrderItems(final Order order) {
		return getOrderItems(order.getId());
	}

	/**
	 * Get a list of items in an order.
	 *
	 * @param orderId
	 *            The order it to search for items.
	 * @return The list of items in the order.
	 */
	public List<OrderItem> getOrderItems(final int orderId) {
		List<OrderItem> items = new LinkedList<OrderItem>();
		try {
			List<OrderToOrderItem> orderToOrderItems = mLocalDatabaseAccessor.getObjectDao(OrderToOrderItem.class).queryBuilder().where()
					.eq(mOrderIdTag, orderId).query();
			for (OrderToOrderItem orderToOrderItem : orderToOrderItems) {
				items.add(mLocalDatabaseAccessor.getObjectDao(OrderItem.class).queryForId(orderToOrderItem.getItemId()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}

	/**
	 * Create a new Order.
	 *
	 * @param order
	 *            {@link Order}
	 */
	public void create(final Order order) {
		mRemoteDatabaseAccessor.postRequest("create_order.php", new TypeToken<CreateResponse>() {
		}, order.toMap(), new FutureCallback<CreateResponse>() {
			@Override
			public void onCompleted(final Exception exception, final CreateResponse response) {
				if (exception == null) {
					Log.d(LOG_TAG, "Created an order with ID>: " + response.getId() + " and got a message saying " + response.getMessage());
				} else {
					Log.e(LOG_TAG, "Error occured when posting request", exception);
				}
			}
		});
	}

	/**
	 * Create a new Order.
	 *
	 * @param order
	 *            {@link Order}
	 * @param callback
	 *            {@link FutureCallback<CreateResponse>}
	 */
	public void create(final Order order, final FutureCallback<CreateResponse> callback) {
		mRemoteDatabaseAccessor.postRequest("create_order.php", new TypeToken<CreateResponse>() {
		}, order.toMap(), callback);
	}

	/**
	 * Create a new SiteToOrder.
	 *
	 * @param siteToOrder
	 *            {@link SiteToOrder}
	 * @param callback
	 *            {@link FutureCallback<CreateResponse>}
	 */
	public void create(final SiteToOrder siteToOrder, final FutureCallback<CreateResponse> callback) {
		mRemoteDatabaseAccessor.postRequest("create_site_to_order.php", new TypeToken<CreateResponse>() {
		}, siteToOrder.toMap(), callback);
	}

	/**
	 * Create a new OrderItem.
	 *
	 * @param orderItem
	 *            {@link OrderItem}
	 * @param callback
	 *            {@link FutureCallback<CreateResponse>}
	 */
	public void create(final OrderItem orderItem, final FutureCallback<CreateResponse> callback) {
		mRemoteDatabaseAccessor.postRequest("create_order_item.php", new TypeToken<CreateResponse>() {
		}, orderItem.toMap(), callback);
	}

	/**
	 * Create a new OrderToOrderItem.
	 *
	 * @param orderToOrderItem
	 *            {@link OrderToOrderItem}
	 * @param callback
	 *            {@link FutureCallback<CreateResponse>}
	 */
	public void create(final OrderToOrderItem orderToOrderItem, final FutureCallback<CreateResponse> callback) {
		mRemoteDatabaseAccessor.postRequest("create_order_to_order_item.php", new TypeToken<CreateResponse>() {
		}, orderToOrderItem.toMap(), callback);
	}

	/**
	 * Create a new Site.
	 *
	 * @param site
	 *            {@link Site}
	 */
	public void create(final Site site) {
		mRemoteDatabaseAccessor.postRequest("create_site.php", new TypeToken<CreateResponse>() {
		}, site.toMap(), new FutureCallback<CreateResponse>() {
			@Override
			public void onCompleted(final Exception exception, final CreateResponse response) {
				if (exception == null) {
					Log.d(LOG_TAG, "Created an order with ID>: " + response.getId() + " and got a message saying " + response.getMessage());
				} else {
					Log.e(LOG_TAG, "Error occured when posting request", exception);
				}
			}
		});
	}

	/**
	 * Update an Order.
	 *
	 * @param order
	 *            {@link Order}
	 */
	public void update(final Order order) {
	}

	/**
	 * Delete an order.
	 *
	 * @param order
	 *            {@link Order}
	 */
	public void delete(final Order order) {
	}

	/**
	 * Create a new InventoryItem.
	 *
	 * @param inventoryItem
	 *            {@link InventoryItem}
	 */
	public void create(final InventoryItem inventoryItem) {
	}

	/**
	 * Update an InventoryItem.
	 *
	 * @param inventoryItem
	 *            {@link InventoryItem}
	 */
	public void update(final InventoryItem inventoryItem) {
	}

	/**
	 * Delete an InventoryItem.
	 *
	 * @param inventoryItem
	 *            {@link InventoryItem}
	 */
	public void delete(final InventoryItem inventoryItem) {
	}

	/**
	 * Create a new OrderItem.
	 *
	 * @param orderItem
	 *            {@link OrderItem}
	 */
	public void create(final OrderItem orderItem) {
	}

	/**
	 * Update an OrderItem.
	 *
	 * @param orderItem
	 *            {@link OrderItem}
	 */
	public void update(final OrderItem orderItem) {
	}

	/**
	 * Delete an OrderItem.
	 *
	 * @param orderItem
	 *            {@link OrderItem}
	 */
	public void delete(final OrderItem orderItem) {
	}
}
