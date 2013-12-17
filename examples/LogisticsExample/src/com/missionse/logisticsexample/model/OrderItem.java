package com.missionse.logisticsexample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

/**
 * Supplies for orders.
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
	 * Default constructor needed by ORM library.
	 */
	public OrderItem() {
	}

	/**
	 * Retrieves the quantity of this order item.
	 * @return the quantity
	 */
	public double getQuantity() {
		return mQuantity;
	}

	/**
	 * Sets the quantity of this order item.
	 * @param amount the quantity to set
	 */
	public void setQuantity(final double amount) {
		mQuantity = amount;
	}

	/**
	 * Retrieves the id.
	 * @return the DB ID associated with this item's name
	 */
	public int getNameId() {
		return mNameId;
	}

	/**
	 * Sets the id.
	 * @param nameId the DB id. NOTE: this should only be set if you know what you are doing!
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
}
