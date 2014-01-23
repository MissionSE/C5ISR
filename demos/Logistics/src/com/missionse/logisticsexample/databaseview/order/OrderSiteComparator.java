package com.missionse.logisticsexample.databaseview.order;

import java.util.Comparator;

import com.missionse.logisticsexample.database.LocalDatabaseHelper;
import com.missionse.logisticsexample.model.Order;

/**
 * Compares two orders in terms of associated site. Sites are ranked alphabetically.
 */
public class OrderSiteComparator implements Comparator<Order> {

	private LocalDatabaseHelper mDatabaseHelper;

	/**
	 * Creates a new OrderSiteComparator.
	 * @param databaseHelper the database helper
	 */
	public OrderSiteComparator(final LocalDatabaseHelper databaseHelper) {
		mDatabaseHelper = databaseHelper;
	}

	@Override
	public int compare(final Order lhs, final Order rhs) {
		//String lhsSite = mDatabaseHelper.
		return 0;
	}

}
