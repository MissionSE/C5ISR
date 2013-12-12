package com.missionse.logisticsexample.model.mappings;

import com.j256.ormlite.field.DatabaseField;
import com.missionse.logisticsexample.model.Order;
import com.missionse.logisticsexample.model.InventorySupply;

/**
 * Mapping from and {@link Order} to a {@link InventorySupply}.
 */
public class OrderToSupply {
	
	@DatabaseField(generatedId = true, columnName = "_id")
	private int mId;
	
	@DatabaseField(columnName = "order_id")
	private int mOrderId;
	
	@DatabaseField(columnName = "supply_id")
	private int mSupplyId;
	
	/**
	 * Empty constructor. 
	 */
	public OrderToSupply() { }

	/**
	 * @return the mId
	 */
	public int getId() {
		return mId;
	}

	/**
	 * @param id the mId to set
	 */
	public void setId(int id) {
		this.mId = id;
	}

	/**
	 * @return the mOrderId
	 */
	public int getOrderId() {
		return mOrderId;
	}

	/**
	 * @param orderId the mOrderId to set
	 */
	public void setOrderId(int orderId) {
		this.mOrderId = orderId;
	}

	/**
	 * @return the mSupplyId
	 */
	public int getSupplyId() {
		return mSupplyId;
	}

	/**
	 * @param supplyId the mSupplyId to set
	 */
	public void setSupplyId(int supplyId) {
		this.mSupplyId = supplyId;
	}
}
