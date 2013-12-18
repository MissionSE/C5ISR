package com.missionse.logisticsexample.model.orm;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.missionse.logisticsexample.model.Order;

/**
 * Wrapper for JSON response.
 */
public class OrderResponse extends Response {
	
	@SerializedName("orders")
	private List<Order> mOrder;

	/**
	 * @return the mOrder
	 */
	public List<Order> getOrder() {
		return mOrder;
	}

	/**
	 * @param order the mOrder to set
	 */
	public void setOrder(List<Order> order) {
		this.mOrder = order;
	}
}
