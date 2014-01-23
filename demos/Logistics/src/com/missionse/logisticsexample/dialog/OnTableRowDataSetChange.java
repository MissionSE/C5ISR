package com.missionse.logisticsexample.dialog;

import com.missionse.logisticsexample.view.OrderItemTableRow;

/**
 * Used for communication between a Table and TableRow.
 */
public interface OnTableRowDataSetChange {

	/**
	 * Remove the view being passed in.
	 * 
	 * @param row
	 *            The row to be removed
	 */
	void onRemove(OrderItemTableRow row);

}
