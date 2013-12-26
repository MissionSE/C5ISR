package com.missionse.logisticsexample.model.mappings;

import java.util.Map;

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
	 * Default constructor.
	 */
	public OrderToOrderItem() {
	}

	/**
	 * Gets the order id for the mapping.
	 * @return The order id.
	 */
	public int getOrderId() {
		return mOrderId;
	}

	/**
	 * Sets the order id for the mapping.
	 * @param orderId The order id.
	 */
	public void setOrderId(final int orderId) {
		mOrderId = orderId;
	}

	/**
	 * Gets the item id for the mapping.
	 * @return The item id.
	 */
	public int getItemId() {
		return mItemId;
	}

	/**
	 * Sets the item id for the mapping.
	 * @param itemId The item id.
	 */
	public void setItemId(final int itemId) {
		mItemId = itemId;
	}

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		string.append("OrderToOrderItem>: ");
		string.append(" id = " + getId());
		string.append(" site_id = " + mItemId);
		string.append(" order_id = " + mOrderId);
		return string.toString();
	}

	@Override
	public Map<String, String> toMap() {
		Map<String, String> map = super.toMap();
		map.put("order_id", Integer.toString(mOrderId));
		map.put("item_id", Integer.toString(mItemId));

		return map;
	}
}
