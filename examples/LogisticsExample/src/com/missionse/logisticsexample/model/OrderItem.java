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
	public OrderItem() { }

	/**
	 * @return the mQuantity
	 */
	public double getQuantity() {
		return mQuantity;
	}

	/**
	 * @param amount the mQuantity to set
	 */
	public void setQuantity(double amount) {
		this.mQuantity = amount;
	}
	
	/**
	 * @return the DB ID associated with this items name.
	 */
	public int getNameId() {
		return mNameId;
	}
	
	/**
	 * @param nameId - this should only be set if you know what you are doing
	 */
	public void setNameId(int nameId) {
		mNameId = nameId;
	}
}
