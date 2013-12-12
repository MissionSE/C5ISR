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
import com.missionse.logisticsexample.model.Order;
import com.missionse.logisticsexample.model.InventorySupply;
import com.missionse.logisticsexample.model.SupplySite;
import com.missionse.logisticsexample.model.mappings.OrderToSupply;
import com.missionse.logisticsexample.model.mappings.SiteToOrder;
import com.missionse.logisticsexample.model.mappings.SiteToSupply;

/**
 * Represents the offline database. 
 * @author rvieras
 *
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	
	private static final String DATABASE_NAME = "mylocaldatabase.db";
	private static final int DATABASE_VERSION = 1;

	private Dao<SupplySite, Integer> mSiteDao = null;
	private Dao<Order, Integer> mOrderDao = null;
	private Dao<InventorySupply, Integer> mSupplyDao = null;
	
	private Dao<OrderToSupply, Integer> mOrderToSupplyDao = null;
	private Dao<SiteToOrder, Integer> mSiteToOrderDao = null;
	private Dao<SiteToSupply, Integer> mSiteToSupplyDao = null;
	
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
			TableUtils.createTable(connectionSource, InventorySupply.class);
			TableUtils.createTable(connectionSource, Order.class);
			TableUtils.createTable(connectionSource, SupplySite.class);
			TableUtils.createTable(connectionSource, OrderToSupply.class);
			TableUtils.createTable(connectionSource, SiteToOrder.class);
			TableUtils.createTable(connectionSource, SiteToSupply.class);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, InventorySupply.class, true);
			TableUtils.dropTable(connectionSource, Order.class, true);
			TableUtils.dropTable(connectionSource, SupplySite.class, true);
			TableUtils.dropTable(connectionSource, OrderToSupply.class, true);
			TableUtils.dropTable(connectionSource, SiteToOrder.class, true);
			TableUtils.dropTable(connectionSource, SiteToSupply.class, true);
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
		try {
			mSiteDao = getDao(SupplySite.class);
			mOrderDao = getDao(Order.class);
			mSupplyDao = getDao(InventorySupply.class);
			mOrderToSupplyDao = getDao(OrderToSupply.class);
			mSiteToOrderDao = getDao(SiteToOrder.class);
			mSiteToSupplyDao = getDao(SiteToSupply.class);
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
		mSupplyDao = null;
		mOrderToSupplyDao = null;
		mSiteToOrderDao = null;
		mSiteToSupplyDao = null;
	}
	
	/////////////////////////////////////////////////////////
	// InventorySupply Site Methods
	/////////////////////////////////////////////////////////
	
	/**
	 * Create a new supply site in the database. 
	 * @param site - {@link SupplySite}
	 */
	public void createNewSupplySite(SupplySite site) {
		
	}
	
	/**
	 * Update the current supply site. 
	 * @param site - {@link SupplySite}
	 */
	public void updateSupplySite(SupplySite site) {
		
	}
	
	/**
	 * delete the current supply site. 
	 * @param site - {@link SupplySite}
	 */
	public void deleteSupplySite(SupplySite site) {
		
	}
	
	/**
	 * Get the parent of the current site.
	 * @param site - child site
	 * @return null if no parent is currently assigned
	 */
	public SupplySite getSiteParent(SupplySite site) {
		return null;
	}
	
	/**
	 * Get a list of supplies current in this order.
	 * @param site - site that contains the supplies
	 * @return - list of supplies
	 */
	public List<InventorySupply> getSupplies(SupplySite site) {
		return Collections.emptyList();
	}
	
	/**
	 * Get a list of supplies current in this order.
	 * @param site  - site that contains the orders
	 * @return - list of supplies
	 */
	public List<Order> getOrders(SupplySite site) {
		return Collections.emptyList();
	}
	
	/////////////////////////////////////////////////////////
	// Order Methods
	/////////////////////////////////////////////////////////
	/**
	 * Create a new Order. 
	 * @param order - {@link Order}
	 */
	public void createNewOrder(Order order) {
		
	}
	
	/**
	 * Update an Order. 
	 * @param order - {@link Order}
	 */
	public void updateOrder(Order order) {
		
	}
	
	/**
	 * Delete an order. 
	 * @param order - {@link Order}
	 */
	public void deleteOrder(Order order) {
		
	}
	
	/**
	 * Get a list of supplies current in this order.
	 * @param order - order
	 * @return - list of supplies
	 */
	public List<InventorySupply> getSupplies(Order order) {
		return Collections.emptyList();
	}
	
	/////////////////////////////////////////////////////////
	// InventorySupply Methods
	/////////////////////////////////////////////////////////
	/**
	 * Create a new InventorySupply.
	 * @param supply - {@link InventorySupply}
	 */
	public void createNewSupply(InventorySupply supply) {
		
	}
	
	/**
	 * Update a InventorySupply.
	 * @param supply - {@link InventorySupply}
	 */
	public void updateSupply(InventorySupply supply) {
		
	}	
	
	/**
	 * Delete a InventorySupply.
	 * @param supply - {@link InventorySupply}
	 */
	public void deleteSupply(InventorySupply supply) {
		
	}

}
