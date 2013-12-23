package com.missionse.logisticsexample.database;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;

import com.missionse.logisticsexample.R;
import com.missionse.logisticsexample.model.InventoryItem;
import com.missionse.logisticsexample.model.ItemName;
import com.missionse.logisticsexample.model.Order;
import com.missionse.logisticsexample.model.OrderItem;
import com.missionse.logisticsexample.model.Site;
import com.missionse.logisticsexample.model.mappings.OrderToOrderItem;
import com.missionse.logisticsexample.model.mappings.SiteToInventoryItem;
import com.missionse.logisticsexample.model.mappings.SiteToOrder;

/**
 * Provides a helper utility to manipulate the tables in the database.
 */
public class LocalDatabaseHelper {
	private LocalDatabaseAccessor mDatabaseAccessor;

	private String mSiteIdTag;
	private String mOrderIdTag;

	/**
	 * Constructor.
	 * @param context The context of the activity that owns the helper.
	 * @param databaseAccessor An accessor to the local database.
	 */
	public LocalDatabaseHelper(final Context context, final LocalDatabaseAccessor databaseAccessor) {
		mDatabaseAccessor = databaseAccessor;

		mSiteIdTag = context.getString(R.string.tag_site_id);
		mOrderIdTag = context.getString(R.string.tag_order_id);
	}

	/**
	 * Gets the list of all sites from the database.
	 * @return The list of all sites in the database.
	 */
	public List<Site> getSites() {
		return mDatabaseAccessor.fetchAll(Site.class);
	}

	/**
	 * Gets the list of all orders from the database.
	 * @return The list of all orders in the database.
	 */
	public List<Order> getOrders() {
		return mDatabaseAccessor.fetchAll(Order.class);
	}

	/**
	 * Gets the list of all itemnames from the database.
	 * 
	 * @return The list of all orders in the database.
	 */
	public List<ItemName> getItemNames() {
		return mDatabaseAccessor.fetchAll(ItemName.class);
	}

	/**
	 * Gets the parent of a site.
	 * @param site The site with a parent.
	 * @return The parent of the site or null if there is none.
	 */
	public Site getSiteParent(final Site site) {
		Site parent = null;
		try {
			parent = mDatabaseAccessor.getObjectDao(Site.class).queryForId(site.getParentId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return parent;
	}

	/**
	 * Gets a list of inventory items at a site.
	 * @param site The site that contains the items.
	 * @return The list of inventory items.
	 */
	public List<InventoryItem> getInventoryItems(final Site site) {
		List<InventoryItem> inventoryItems = new LinkedList<InventoryItem>();
		try {
			List<SiteToInventoryItem> siteToInvItem = mDatabaseAccessor.getObjectDao(SiteToInventoryItem.class)
					.queryBuilder().where().eq(mSiteIdTag, site.getId()).query();
			for (SiteToInventoryItem stii : siteToInvItem) {
				inventoryItems.add(mDatabaseAccessor.getObjectDao(InventoryItem.class).queryForId(stii
						.getItemId()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return inventoryItems;
	}

	/**
	 * Get a list of orders for an associated site.
	 * @param site The site that created the orders.
	 * @return The list of orders.
	 */
	public List<Order> getOrders(final Site site) {
		List<Order> orders = new LinkedList<Order>();
		try {
			List<SiteToOrder> siteToOrder = mDatabaseAccessor.getObjectDao(SiteToOrder.class).queryBuilder()
					.where().eq(mSiteIdTag, site.getId()).query();
			for (SiteToOrder sto : siteToOrder) {
				orders.add(mDatabaseAccessor.getObjectDao(Order.class).queryForId(sto.getOrderId()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orders;
	}

	/**
	 * Get a list of items in an order.
	 * @param order The order that contains the items.
	 * @return The list of items in the order.
	 */
	public List<OrderItem> getOrderItems(final Order order) {
		List<OrderItem> items = new LinkedList<OrderItem>();
		try {
			List<OrderToOrderItem> orderToOrderItems = mDatabaseAccessor.getObjectDao(OrderToOrderItem.class)
					.queryBuilder().where().eq(mOrderIdTag, order.getId())
					.query();
			for (OrderToOrderItem orderToOrderItem : orderToOrderItems) {
				items.add(mDatabaseAccessor.getObjectDao(OrderItem.class).queryForId(orderToOrderItem.getItemId()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}

	/**
	 * Create a new Order.
	 * @param order {@link Order}
	 */
	public void create(final Order order) {
	}

	/**
	 * Update an Order.
	 * @param order {@link Order}
	 */
	public void update(final Order order) {
	}

	/**
	 * Delete an order.
	 * @param order {@link Order}
	 */
	public void delete(final Order order) {
	}

	/**
	 * Create a new InventoryItem.
	 * @param inventoryItem {@link InventoryItem}
	 */
	public void create(final InventoryItem inventoryItem) {
	}

	/**
	 * Update an InventoryItem.
	 * @param inventoryItem {@link InventoryItem}
	 */
	public void update(final InventoryItem inventoryItem) {
	}

	/**
	 * Delete an InventoryItem.
	 * @param inventoryItem {@link InventoryItem}
	 */
	public void delete(final InventoryItem inventoryItem) {
	}

	/**
	 * Create a new OrderItem.
	 * @param orderItem {@link OrderItem}
	 */
	public void create(final OrderItem orderItem) {
	}

	/**
	 * Update an OrderItem.
	 * @param orderItem {@link OrderItem}
	 */
	public void update(final OrderItem orderItem) {
	}

	/**
	 * Delete an OrderItem.
	 * @param orderItem {@link OrderItem}
	 */
	public void delete(final OrderItem orderItem) {
	}
}
