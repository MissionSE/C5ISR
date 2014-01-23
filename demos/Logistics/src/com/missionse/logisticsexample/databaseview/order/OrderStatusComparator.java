package com.missionse.logisticsexample.databaseview.order;

import java.util.Comparator;

import com.missionse.logisticsexample.database.LocalDatabaseHelper;
import com.missionse.logisticsexample.model.Order;

/**
 * Compares two orders in terms of status. Status rank is: processing, shipping, delivered, cancelled.
 */
public class OrderStatusComparator implements Comparator<Order> {

	private LocalDatabaseHelper mDatabaseHelper;

	/**
	 * Creates a new OrderStatusComparator.
	 * @param databaseHelper the database helper
	 */
	public OrderStatusComparator(final LocalDatabaseHelper databaseHelper) {
		mDatabaseHelper = databaseHelper;
	}

	@Override
	public int compare(final Order lhs, final Order rhs) {
		String lhsStatus = mDatabaseHelper.getStatusName(lhs.getStatusId()).getName();
		String rhsStatus = mDatabaseHelper.getStatusName(rhs.getStatusId()).getName();

		if (lhsStatus.equals(rhsStatus)) {
			return lhs.toString().compareTo(rhs.toString());
		}

		if (lhsStatus.equals("processing")) {
			return -1;
		} else if (lhsStatus.equals("shipping")) {
			if (rhsStatus.equals("processing")) {
				return 1;
			} else {
				return -1;
			}
		} else if (lhsStatus.equals("delivered")) {
			if (rhsStatus.equals("cancelled")) {
				return -1;
			} else {
				return 1;
			}
		} else if (lhsStatus.equals("cancelled")) {
			return 1;
		}
		return 0;
	}

}
