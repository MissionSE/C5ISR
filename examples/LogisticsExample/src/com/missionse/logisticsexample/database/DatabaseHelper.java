package com.missionse.logisticsexample.database;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.missionse.logisticsexample.model.InventoryItem;
import com.missionse.logisticsexample.model.ItemName;
import com.missionse.logisticsexample.model.Order;
import com.missionse.logisticsexample.model.OrderItem;
import com.missionse.logisticsexample.model.Site;
import com.missionse.logisticsexample.model.mappings.OrderToOrderItem;
import com.missionse.logisticsexample.model.mappings.SiteToInventoryItem;
import com.missionse.logisticsexample.model.mappings.SiteToOrder;
import com.missionse.logisticsexample.model.orm.SiteResponse;

/**
 * Represents the off-line database.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "mylocaldatabase.db";
	private static final int DATABASE_VERSION = 1;

	private Dao<Site, Integer> mSiteDao = null;
	private Dao<Order, Integer> mOrderDao = null;

	//private Dao<ItemName, Integer> mItemNameDao = null;
	private Dao<InventoryItem, Integer> mInventoryItemDao = null;
	private Dao<OrderItem, Integer> mOrderItemDao = null;

	private Dao<OrderToOrderItem, Integer> mOrderToOrderItemDao = null;
	private Dao<SiteToOrder, Integer> mSiteToOrderDao = null;
	private Dao<SiteToInventoryItem, Integer> mSiteToInventoryItemDao = null;

	private Context mContext;

	public static final Class<?>[] DB_CLASSES = { ItemName.class, InventoryItem.class, OrderItem.class, Order.class,
			Site.class, OrderToOrderItem.class, SiteToOrder.class, SiteToInventoryItem.class };

	/**
	 * Constructor.
	 * @param context - {@link android.app.Context}
	 */
	public DatabaseHelper(final Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		mContext = context;
	}

	@Override
	public void onCreate(final SQLiteDatabase db, final ConnectionSource connectionSource) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onCreate");
			for (Class<?> clazz : DB_CLASSES) {
				Log.d("DatabaseHelper", "Creating>: " + clazz.getSimpleName());
				TableUtils.createTable(connectionSource, clazz);
			}
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(final SQLiteDatabase db, final ConnectionSource connectionSource, final int oldVersion,
			final int newVersion) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
			for (Class<?> clazz : DB_CLASSES) {
				Log.d("DatabaseHelper", "Dropping>: " + clazz.getSimpleName());
				TableUtils.dropTable(connectionSource, clazz, true);
			}
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Perform necessary initialization procedures.
	 */
	public void initialize() {
		Log.d(DatabaseHelper.class.getName(), "Initialize");
		try {
			mSiteDao = getDao(Site.class);
			mOrderDao = getDao(Order.class);

			//mItemNameDao = getDao(ItemName.class);
			mInventoryItemDao = getDao(InventoryItem.class);
			mOrderItemDao = getDao(OrderItem.class);

			mOrderToOrderItemDao = getDao(OrderToOrderItem.class);
			mSiteToOrderDao = getDao(SiteToOrder.class);
			mSiteToInventoryItemDao = getDao(SiteToInventoryItem.class);
		} catch (Exception dao) {
			Log.e("DatabaseHelper", "INIT", dao);
		}
	}

	@Override
	public void close() {
		super.close();
		mSiteDao = null;
		mOrderDao = null;
		//mItemNameDao = null;
		mInventoryItemDao = null;
		mOrderItemDao = null;
		mOrderToOrderItemDao = null;
		mSiteToOrderDao = null;
		mSiteToInventoryItemDao = null;
	}

	/////////////////////////////////////////////////////////
	// Site Methods
	/////////////////////////////////////////////////////////

	/**
	 * Create a new supply site in the database.
	 * @param site - {@link Site}
	 */
	public void create(final Site site) {

	}

	/**
	 * Update the current supply site.
	 * @param site - {@link Site}
	 */
	public void update(final Site site) {

	}

	/**
	 * delete the current supply site.
	 * @param site - {@link Site}
	 */
	public void delete(final Site site) {

	}

	/**
	 * Get the parent of the current site.
	 * @param site - child site
	 * @return null if no parent is currently assigned
	 */
	public Site getSiteParent(final Site site) {
		Site parent = null;
		try {
			parent = mSiteDao.queryForId(site.getParentId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return parent;
	}

	/**
	 * Get a list of supplies current in this order.
	 * @param site - site that contains the supplies
	 * @return - list of supplies
	 */
	public List<InventoryItem> getSupplies(final Site site) {
		List<InventoryItem> inventoryItems = new LinkedList<InventoryItem>();
		try {
			List<SiteToInventoryItem> siteToInvItem = mSiteToInventoryItemDao.queryBuilder().where()
					.eq("site_id", site.getId()).query();
			for (SiteToInventoryItem stii : siteToInvItem) {
				inventoryItems.add(mInventoryItemDao.queryForId(stii.getItemId()));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return inventoryItems;
	}

	/**
	 * Get a list of supplies current in this order.
	 * @param site - site that contains the orders
	 * @return - list of supplies
	 */
	public List<Order> getOrders(final Site site) {
		List<Order> orders = new LinkedList<Order>();
		try {
			List<SiteToOrder> siteToOrder = mSiteToOrderDao.queryBuilder().where().eq("site_id", site.getId()).query();
			for (SiteToOrder sto : siteToOrder) {
				orders.add(mOrderDao.queryForId(sto.getOrderId()));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orders;
	}

	/////////////////////////////////////////////////////////
	// Order Methods
	/////////////////////////////////////////////////////////
	/**
	 * Create a new Order.
	 * @param myOrder - {@link Order}
	 */
	public void create(final Order myOrder) {

	}

	/**
	 * Update an Order.
	 * @param myOrder - {@link Order}
	 */
	public void update(final Order myOrder) {

	}

	/**
	 * Delete an order.
	 * @param myOrder - {@link Order}
	 */
	public void delete(final Order myOrder) {

	}

	/**
	 * Get a list of supplies current in this order.
	 * @param myOrder - order
	 * @return - list of order items
	 */
	public List<OrderItem> getSupplies(final Order myOrder) {
		List<OrderItem> items = new LinkedList<OrderItem>();
		try {
			List<OrderToOrderItem> orderToOrderItem = mOrderToOrderItemDao.queryBuilder().where()
					.eq("order_id", myOrder.getId()).query();
			for (OrderToOrderItem otoi : orderToOrderItem) {
				items.add(mOrderItemDao.queryForId(otoi.getItemId()));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}

	/////////////////////////////////////////////////////////
	// InventoryItem Methods
	/////////////////////////////////////////////////////////
	/**
	 * Create a new InventoryItem.
	 * @param supply - {@link InventoryItem}
	 */
	public void create(final InventoryItem supply) {

	}

	/**
	 * Update a InventoryItem.
	 * @param supply - {@link InventoryItem}
	 */
	public void update(final InventoryItem supply) {

	}

	/**
	 * Delete a InventoryItem.
	 * @param supply - {@link InventoryItem}
	 */
	public void delete(final InventoryItem supply) {

	}

	/////////////////////////////////////////////////////////
	// OrderItem Methods
	/////////////////////////////////////////////////////////
	/**
	 * Create a new OrderItem.
	 * @param supply - {@link OrderItem}
	 */
	public void create(final OrderItem supply) {

	}

	/**
	 * Update a OrderItem.
	 * @param supply - {@link OrderItem}
	 */
	public void update(final OrderItem supply) {

	}

	/**
	 * Delete a OrderItem.
	 * @param supply - {@link OrderItem}
	 */
	public void delete(final OrderItem supply) {

	}

	/**
	 * Temporary method to test ION.
	 */
	public void callDatabase() {
		Ion.with(mContext, "http://192.168.86.137/get_all_sites.php").as(new TypeToken<SiteResponse>() {
		}).setCallback(new FutureCallback<SiteResponse>() {
			@Override
			public void onCompleted(final Exception e, final SiteResponse site) {
				if (e == null) {
					Log.d("DatabaseHelper", "Succuess>: " + site.isSuccess());
					Log.d("DatabaseHelper", "Message>: " + site.getMessage());
					for (Site s : site.getSites()) {
						Log.d("DatabaseHelper", "Site Name>: " + s.getName());
					}
				} else {
					e.printStackTrace();
				}
			}
		});
	}

}
