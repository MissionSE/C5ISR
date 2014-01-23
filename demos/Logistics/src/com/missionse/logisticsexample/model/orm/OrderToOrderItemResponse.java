package com.missionse.logisticsexample.model.orm;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.missionse.logisticsexample.model.mappings.OrderToOrderItem;

/**
 * Wrapper class to JSON.
 */
public class OrderToOrderItemResponse extends Response {

		@SerializedName("orders_to_order_items")
		private List<OrderToOrderItem> mOrdersToOrderItems;

		/**
		 * @return the mOrdersToOrderItems
		 */
		public List<OrderToOrderItem> getOrdersToOrderItems() {
			return mOrdersToOrderItems;
		}

		/**
		 * @param ordersToOrderItems the mOrdersToOrderItems to set
		 */
		public void setOrdersToOrderItems(final List<OrderToOrderItem> ordersToOrderItems) {
			mOrdersToOrderItems = ordersToOrderItems;
		}
}
