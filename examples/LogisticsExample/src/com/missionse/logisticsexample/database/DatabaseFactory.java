package com.missionse.logisticsexample.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import android.content.Context;

import com.missionse.logisticsexample.database.table.InventoryItemDatabaseRequestor;
import com.missionse.logisticsexample.database.table.ItemNameDatabaseRequestor;
import com.missionse.logisticsexample.database.table.OrderDatabaseRequestor;
import com.missionse.logisticsexample.database.table.OrderItemDatabaseRequestor;
import com.missionse.logisticsexample.database.table.OrderToOrderItemDatabaseRequestor;
import com.missionse.logisticsexample.database.table.SiteDatabaseRequestor;
import com.missionse.logisticsexample.database.table.SiteToInventoryItemDatabaseRequestor;
import com.missionse.logisticsexample.database.table.SiteToOrderDatabaseRequestor;
import com.missionse.logisticsexample.model.DBEntity;
import com.missionse.logisticsexample.model.InventoryItem;
import com.missionse.logisticsexample.model.ItemName;
import com.missionse.logisticsexample.model.Order;
import com.missionse.logisticsexample.model.OrderItem;
import com.missionse.logisticsexample.model.Site;
import com.missionse.logisticsexample.model.mappings.OrderToOrderItem;
import com.missionse.logisticsexample.model.mappings.SiteToInventoryItem;
import com.missionse.logisticsexample.model.mappings.SiteToOrder;

/**
 * Provides functionality to set up the database objects.
 */
public final class DatabaseFactory {
	private static final long DELAY_BEFORE_FIRST_RUN_IN_MS = 500;
	private static final long INTERVAL_BETWEEN_RUNS_IN_MS = 4000;

	private DatabaseFactory() {
	}

	/**
	 * Creates the necessary classes to manage the local and remote databases.
	 * @param context The context of the activity.
	 * @param updateCompleteListener The listener to receive database callbacks.
	 * @return A database helper to access the local database.
	 */
	public static LocalDatabaseHelper initializeDatabase(final Context context,
			final DatabaseUpdateCompleteListener updateCompleteListener) {
		LocalDatabaseHelper databaseHelper = null;
		LocalDatabaseAccessor databaseAccessor = null;
		DatabaseConnector databaseConnector = null;
		DatabaseUpdateThread databaseUpdateThread = null;
		Timer databasePeriodic = null;

		databaseAccessor = new LocalDatabaseAccessor(context, getDaoClasses());
		databaseHelper = new LocalDatabaseHelper(context, databaseAccessor);
		databaseConnector = new DatabaseConnector(context);

		List<DatabaseRequestor> databaseRequestors = getDatabaseRequestors(context, databaseConnector, databaseAccessor);
		databaseUpdateThread = new DatabaseUpdateThread(updateCompleteListener, databaseRequestors);

		databasePeriodic = new Timer();
		databasePeriodic.schedule(databaseUpdateThread, DELAY_BEFORE_FIRST_RUN_IN_MS, INTERVAL_BETWEEN_RUNS_IN_MS);

		return databaseHelper;
	}

	private static List<Class<? extends DBEntity>> getDaoClasses() {
		List<Class<? extends DBEntity>> daoClasses = new ArrayList<Class<? extends DBEntity>>();
		daoClasses.add(InventoryItem.class);
		daoClasses.add(ItemName.class);
		daoClasses.add(Order.class);
		daoClasses.add(OrderItem.class);
		daoClasses.add(Site.class);
		daoClasses.add(OrderToOrderItem.class);
		daoClasses.add(SiteToInventoryItem.class);
		daoClasses.add(SiteToOrder.class);

		return daoClasses;
	}

	private static List<DatabaseRequestor> getDatabaseRequestors(final Context context, final DatabaseConnector databaseConnector,
			final LocalDatabaseAccessor databaseAccessor) {
		List<DatabaseRequestor> databaseRequestors = new ArrayList<DatabaseRequestor>();
		databaseRequestors.add(new InventoryItemDatabaseRequestor(context, databaseConnector, databaseAccessor));
		databaseRequestors.add(new ItemNameDatabaseRequestor(context, databaseConnector, databaseAccessor));
		databaseRequestors.add(new OrderDatabaseRequestor(context, databaseConnector, databaseAccessor));
		databaseRequestors.add(new OrderItemDatabaseRequestor(context, databaseConnector, databaseAccessor));
		databaseRequestors.add(new OrderToOrderItemDatabaseRequestor(context, databaseConnector, databaseAccessor));
		databaseRequestors.add(new SiteDatabaseRequestor(context, databaseConnector, databaseAccessor));
		databaseRequestors.add(new SiteToInventoryItemDatabaseRequestor(context, databaseConnector, databaseAccessor));
		databaseRequestors.add(new SiteToOrderDatabaseRequestor(context, databaseConnector, databaseAccessor));

		return databaseRequestors;
	}
}
