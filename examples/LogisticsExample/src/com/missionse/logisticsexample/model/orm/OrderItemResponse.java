package com.missionse.logisticsexample.model.orm;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.missionse.logisticsexample.model.OrderItem;

/**
 * Wrapper for JSON object.
 */
public class OrderItemResponse extends Response {

	@SerializedName("order_items")
	private List<OrderItem> mItems;

	/**
	 * @return the mItems
	 */
	public List<OrderItem> getItems() {
		return mItems;
	}

	/**
	 * @param items the mItems to set
	 */
	public void setItems(final List<OrderItem> items) {
		this.mItems = items;
	}
}
