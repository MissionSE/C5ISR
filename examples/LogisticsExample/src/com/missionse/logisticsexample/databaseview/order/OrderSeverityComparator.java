package com.missionse.logisticsexample.databaseview.order;

import java.util.Comparator;

import com.missionse.logisticsexample.database.LocalDatabaseHelper;
import com.missionse.logisticsexample.model.Order;

/**
 * Compares two orders in terms of severity. Severity rank is: 3 (urgent), 2 (normal), 1 (minor).
 */
public class OrderSeverityComparator implements Comparator<Order> {

	private LocalDatabaseHelper mDatabaseHelper;

	/**
	 * Creates a new OrderSeverityComparator.
	 * @param databaseHelper the helper to access database information
	 */
	public OrderSeverityComparator(final LocalDatabaseHelper databaseHelper) {
		mDatabaseHelper = databaseHelper;
	}

	@Override
	public int compare(final Order lhs, final Order rhs) {
		String lhsSeverity = mDatabaseHelper.getSeverityName(lhs.getSeverityId()).getName();
		String rhsSeverity = mDatabaseHelper.getSeverityName(rhs.getSeverityId()).getName();

		if (lhsSeverity.equals(rhsSeverity)) {
			return lhs.toString().compareTo(rhs.toString());
		}

		if (lhsSeverity.equals("minor")) {
			return -1;
		} else if (lhsSeverity.equals("normal")) {
			if (rhsSeverity.equals("minor")) {
				return 1;
			} else {
				return -1;
			}
		} else if (lhsSeverity.equals("urgent")) {
			return 1;
		}
		return 0;
	}
}
