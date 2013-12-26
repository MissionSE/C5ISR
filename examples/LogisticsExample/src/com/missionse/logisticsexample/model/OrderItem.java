package com.missionse.logisticsexample.model;

import java.util.Map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

/**
 * Represents an item in an order.
 */
public class OrderItem extends DBEntity {

	@Expose(serialize = true, deserialize = true)
	@SerializedName("quantity")
	@DatabaseField(columnName = "quantity")
	private double mQuantity;

	@Expose(serialize = true, deserialize = true)
	@SerializedName("name_id")
	@DatabaseField(columnName = "name_id")
	private int mNameId;

	/**
	 * Default constructor.
	 */
	public OrderItem() {
	}

	/**
	 * Retrieves the quantity of the item.
	 * @return The quantity of the item.
	 */
	public double getQuantity() {
		return mQuantity;
	}

	/**
	 * Sets the quantity of the item.
	 * @param quantity The quantity of the item.
	 */
	public void setQuantity(final double quantity) {
		mQuantity = quantity;
	}

	/**
	 * Retrieves the id of the item name.
	 * @return The id associated with the item's name
	 */
	public int getNameId() {
		return mNameId;
	}

	/**
	 * Sets the id of the name of the item.
	 * @param nameId The id of the name of the item.
	 */
	public void setNameId(final int nameId) {
		mNameId = nameId;
	}

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		string.append("OrderItem>: ");
		string.append(" id = " + getId());
		string.append(" quantity = " + this.mQuantity);
		string.append(" nameid = " + this.mNameId);
		return string.toString();
	}

	@Override
	public Map<String, String> toMap() {
		Map<String, String> map = super.toMap();
		map.put("quantity", Double.toString(mQuantity));
		map.put("name_id", Integer.toString(mNameId));

		return map;
	}
}
