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
import com.missionse.logisticsexample.database.table.SeverityNameDatabaseRequestor;
import com.missionse.logisticsexample.database.table.SiteDatabaseRequestor;
import com.missionse.logisticsexample.database.table.SiteToInventoryItemDatabaseRequestor;
import com.missionse.logisticsexample.database.table.SiteToOrderDatabaseRequestor;
import com.missionse.logisticsexample.database.table.StatusNameDatabaseRequestor;
import com.missionse.logisticsexample.model.DBEntity;
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

/**
 * Provides functionality to set up the database objects.
 */
public final class DatabaseFactory {
	private static final long DELAY_BEFORE_FIRST_RUN_IN_MS = 500;
	private static final long INTERVAL_BETWEEN_RUNS_IN_MS = 10000;
    private static DatabaseUpdateThread mDatabaseThread = null;

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
		LocalDatabaseAccessor localDatabaseAccessor = null;
		RemoteDatabaseAccessor remoteDatabaseAccessor = null;
		Timer databasePeriodic = null;

		localDatabaseAccessor = new LocalDatabaseAccessor(context, getDaoClasses());
		databaseHelper = new LocalDatabaseHelper(context, localDatabaseAccessor);
		remoteDatabaseAccessor = new RemoteDatabaseAccessor(context);

		List<RemoteDatabaseRequestor> databaseRequestors = getDatabaseRequestors(context, remoteDatabaseAccessor, localDatabaseAccessor);
		mDatabaseThread = new DatabaseUpdateThread(updateCompleteListener, databaseRequestors);

		databasePeriodic = new Timer();
		databasePeriodic.schedule(mDatabaseThread, DELAY_BEFORE_FIRST_RUN_IN_MS, INTERVAL_BETWEEN_RUNS_IN_MS);

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
		daoClasses.add(SeverityName.class);
		daoClasses.add(StatusName.class);

		return daoClasses;
	}

	private static List<RemoteDatabaseRequestor> getDatabaseRequestors(final Context context,
			final RemoteDatabaseAccessor remoteDatabaseAccessor, final LocalDatabaseAccessor localDatabaseAccessor) {
		List<RemoteDatabaseRequestor> databaseRequestors = new ArrayList<RemoteDatabaseRequestor>();
		databaseRequestors.add(new InventoryItemDatabaseRequestor(context, remoteDatabaseAccessor, localDatabaseAccessor));
		databaseRequestors.add(new ItemNameDatabaseRequestor(context, remoteDatabaseAccessor, localDatabaseAccessor));
		databaseRequestors.add(new OrderDatabaseRequestor(context, remoteDatabaseAccessor, localDatabaseAccessor));
		databaseRequestors.add(new OrderItemDatabaseRequestor(context, remoteDatabaseAccessor, localDatabaseAccessor));
		databaseRequestors.add(new OrderToOrderItemDatabaseRequestor(context, remoteDatabaseAccessor, localDatabaseAccessor));
		databaseRequestors.add(new SiteDatabaseRequestor(context, remoteDatabaseAccessor, localDatabaseAccessor));
		databaseRequestors.add(new SiteToInventoryItemDatabaseRequestor(context, remoteDatabaseAccessor, localDatabaseAccessor));
		databaseRequestors.add(new SiteToOrderDatabaseRequestor(context, remoteDatabaseAccessor, localDatabaseAccessor));
		databaseRequestors.add(new StatusNameDatabaseRequestor(context, remoteDatabaseAccessor, localDatabaseAccessor));
		databaseRequestors.add(new SeverityNameDatabaseRequestor(context, remoteDatabaseAccessor, localDatabaseAccessor));

		return databaseRequestors;
	}

    /**
     * This will pause any necessary components used by the DatabaseFactory.
     */
    public static void pause() {
        mDatabaseThread.pause();
    }

    /**
     *  Resume any necessary components used by the DatabaseFactory.
     */
    public static void resume() {
       mDatabaseThread.resume();
    }
}
