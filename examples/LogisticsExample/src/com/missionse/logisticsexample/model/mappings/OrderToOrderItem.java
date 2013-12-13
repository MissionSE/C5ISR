package com.missionse.logisticsexample.model.mappings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.missionse.logisticsexample.model.DBEntity;

/**
 * Mapping from and {@link com.missionse.logisticsexample.model.MyOrder} to a {@link com.missionse.logisticsexample.model.OrderItem}.
 */
public class OrderToOrderItem extends DBEntity {
	
	@Expose(serialize = true, deserialize = true)
	@SerializedName("order_id")
	@DatabaseField(columnName = "order_id")
	private int mOrderId;
	
	@Expose(serialize = true, deserialize = true)
	@SerializedName("item_id")
	@DatabaseField(columnName = "item_id")
	private int mItemId;
	
	/**
	 * Empty constructor. 
	 */
	public OrderToOrderItem() { }

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
	 * @return the mItemId
	 */
	public int getItemId() {
		return mItemId;
	}

	/**
	 * @param itemId the mItemId to set
	 */
	public void setItemId(int itemId) {
		this.mItemId = itemId;
	}
}
