package com.missionse.logisticsexample.model.orm;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.missionse.logisticsexample.model.mappings.OrderToOrderItem;

/**
 * Wrapper class to JSON.
 */
public class OrderToOrderItemResponse extends Response {

		@SerializedName("orders_to_order_items")
		private List<OrderToOrderItem> mOrderItems;

		/**
		 * @return the mOrderItems
		 */
		public List<OrderToOrderItem> getOrderItems() {
			return mOrderItems;
		}

		/**
		 * @param orderItems the mOrderItems to set
		 */
		public void setOrderItems(List<OrderToOrderItem> orderItems) {
			this.mOrderItems = orderItems;
		}
}
