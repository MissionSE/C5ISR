package com.missionse.logisticsexample.database;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.missionse.logisticsexample.model.InventoryItem;
import com.missionse.logisticsexample.model.ItemName;
import com.missionse.logisticsexample.model.MyOrder;
import com.missionse.logisticsexample.model.OrderItem;
import com.missionse.logisticsexample.model.Site;
import com.missionse.logisticsexample.model.mappings.OrderToOrderItem;
import com.missionse.logisticsexample.model.mappings.SiteToInventoryItem;
import com.missionse.logisticsexample.model.mappings.SiteToOrder;

/**
 * Represents the offline database. 
 * @author rvieras
 *
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	
	private static final String DATABASE_NAME = "mylocaldatabase.db";
	private static final int DATABASE_VERSION = 1;

	private Dao<Site, Integer> mSiteDao = null;
	private Dao<MyOrder, Integer> mOrderDao = null;
	
	private Dao<ItemName, Integer> mItemNameDao = null;
	private Dao<InventoryItem, Integer> mInventoryItemDao = null;
	private Dao<OrderItem, Integer> mOrderItemDao = null;
	
	private Dao<OrderToOrderItem, Integer> mOrderToOrderItemDao = null;
	private Dao<SiteToOrder, Integer> mSiteToOrderDao = null;
	private Dao<SiteToInventoryItem, Integer> mSiteToInventoryItemDao = null;
	
	public static final Class<?>[] DB_CLASSES = {
		ItemName.class,
		InventoryItem.class,
		OrderItem.class,
		MyOrder.class,
		Site.class,
		OrderToOrderItem.class,
		SiteToOrder.class,
		SiteToInventoryItem.class
	};
	
	private boolean mInit = false;

	/**
	 * Constructor.
	 * @param context - {@link android.app.Context}
	 */
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
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
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
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
			mOrderDao = getDao(MyOrder.class);
			
			mItemNameDao = getDao(ItemName.class);
			mInventoryItemDao = getDao(InventoryItem.class);
			mOrderItemDao = getDao(OrderItem.class);
			
			mOrderToOrderItemDao = getDao(OrderToOrderItem.class);
			mSiteToOrderDao = getDao(SiteToOrder.class);
			mSiteToInventoryItemDao = getDao(SiteToInventoryItem.class);
			mInit = true;
		} catch (Exception dao) {
			Log.e("DatabaseHelper", "INIT", dao);
			mInit = false;
		}
	}
	
	@Override
	public void close() {
		super.close();
		mSiteDao = null;
		mOrderDao = null;
		mItemNameDao = null;
		mInventoryItemDao = null;
		mOrderItemDao = null;
		mOrderToOrderItemDao = null;
		mSiteToOrderDao = null;
		mSiteToInventoryItemDao = null;
		mInit = false;
	}
	
	/////////////////////////////////////////////////////////
	// Site Methods
	/////////////////////////////////////////////////////////
	
	/**
	 * Create a new supply site in the database. 
	 * @param site - {@link Site}
	 */
	public void create(Site site) {
		
	}
	
	/**
	 * Update the current supply site. 
	 * @param site - {@link Site}
	 */
	public void update(Site site) {
		
	}
	
	/**
	 * delete the current supply site. 
	 * @param site - {@link Site}
	 */
	public void delete(Site site) {
		
	}
	
	/**
	 * Get the parent of the current site.
	 * @param site - child site
	 * @return null if no parent is currently assigned
	 */
	public Site getSiteParent(Site site) {
		return null;
	}
	
	/**
	 * Get a list of supplies current in this order.
	 * @param site - site that contains the supplies
	 * @return - list of supplies
	 */
	public List<InventoryItem> getSupplies(Site site) {
		return Collections.emptyList();
	}
	
	/**
	 * Get a list of supplies current in this order.
	 * @param site  - site that contains the orders
	 * @return - list of supplies
	 */
	public List<MyOrder> getOrders(Site site) {
		return Collections.emptyList();
	}
	
	/////////////////////////////////////////////////////////
	// MyOrder Methods
	/////////////////////////////////////////////////////////
	/**
	 * Create a new MyOrder. 
	 * @param myOrder - {@link MyOrder}
	 */
	public void create(MyOrder myOrder) {
		
	}
	
	/**
	 * Update an MyOrder. 
	 * @param myOrder - {@link MyOrder}
	 */
	public void update(MyOrder myOrder) {
		
	}
	
	/**
	 * Delete an order. 
	 * @param myOrder - {@link MyOrder}
	 */
	public void delete(MyOrder myOrder) {
		
	}
	
	/**
	 * Get a list of supplies current in this order.
	 * @param myOrder - order
	 * @return - list of supplies
	 */
	public List<InventoryItem> getSupplies(MyOrder myOrder) {
		return Collections.emptyList();
	}
	
	/////////////////////////////////////////////////////////
	// InventoryItem Methods
	/////////////////////////////////////////////////////////
	/**
	 * Create a new InventoryItem.
	 * @param supply - {@link InventoryItem}
	 */
	public void create(InventoryItem supply) {
		
	}
	
	/**
	 * Update a InventoryItem.
	 * @param supply - {@link InventoryItem}
	 */
	public void update(InventoryItem supply) {
		
	}	
	
	/**
	 * Delete a InventoryItem.
	 * @param supply - {@link InventoryItem}
	 */
	public void delete(InventoryItem supply) {
		
	}
	
	/////////////////////////////////////////////////////////
	// OrderItem Methods
	/////////////////////////////////////////////////////////
	/**
	* Create a new OrderItem.
	* @param supply - {@link OrderItem}
	*/
	public void create(OrderItem supply) {
	
	}
	
	/**
	* Update a OrderItem.
	* @param supply - {@link OrderItem}
	*/
	public void update(OrderItem supply) {
	
	}	
	
	/**
	* Delete a OrderItem.
	* @param supply - {@link OrderItem}
	*/
	public void delete(OrderItem supply) {
	
	}
	

}
