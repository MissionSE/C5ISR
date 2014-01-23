package com.missionse.logisticsexample.model.orm;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.missionse.logisticsexample.model.Order;

/**
 * Wrapper for JSON response.
 */
public class OrderResponse extends Response {

	@SerializedName("orders")
	private List<Order> mOrders;

	/**
	 * @return the mOrder
	 */
	public List<Order> getOrders() {
		return mOrders;
	}

	/**
	 * @param orders the mOrder to set
	 */
	public void setOrders(final List<Order> orders) {
		this.mOrders = orders;
	}
}
