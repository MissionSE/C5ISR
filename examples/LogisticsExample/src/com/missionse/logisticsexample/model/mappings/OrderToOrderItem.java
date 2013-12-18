package com.missionse.logisticsexample.model.mappings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.missionse.logisticsexample.model.DBEntity;

/**
 * Mapping from and {@link com.missionse.logisticsexample.model.Order} to a
 * {@link com.missionse.logisticsexample.model.OrderItem}.
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
	public OrderToOrderItem() {
	}

	/**
	 * Gets the order id for this mapping.
	 * @return the order id
	 */
	public int getOrderId() {
		return mOrderId;
	}

	/**
	 * Sets the order id for this mapping.
	 * @param orderId the order id to set
	 */
	public void setOrderId(final int orderId) {
		mOrderId = orderId;
	}

	/**
	 * Gets the item id for this mapping.
	 * @return the item id
	 */
	public int getItemId() {
		return mItemId;
	}

	/**
	 * Sets the item id for this mapping.
	 * @param itemId the item id to set
	 */
	public void setItemId(final int itemId) {
		mItemId = itemId;
	}
	
	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		string.append("OrderToOrderItem>: ");
		string.append(" id = " + getId());
		string.append(" site_id = " + this.mItemId);
		string.append(" order_id = " + this.mOrderId);
		return string.toString();
	}
}
