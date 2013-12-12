package com.missionse.logisticsexample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

/**
 * Represents supply/assets for different locations.
 */
public class InventorySupply extends DBEntity {

	@Expose(serialize = true, deserialize = true)
	@SerializedName("name")
	@DatabaseField(columnName = "name")
	private String mName;
	
	@Expose(serialize = true, deserialize = true)
	@SerializedName("quantity")
	@DatabaseField(columnName = "quantity")
	private double mQuantity;
	
	@Expose(serialize = true, deserialize = true)
	@SerializedName("maxamount")
	@DatabaseField(columnName = "maxamount")
	private double mMaxAmount;
	
	/**
	 * Default constructor needed by ORM library. 
	 */
	public InventorySupply() { }

	/**
	 * @return the mName
	 */
	public String getName() {
		return mName;
	}

	/**
	 * @param name the mName to set
	 */
	public void setName(String name) {
		this.mName = name;
	}

	/**
	 * @return the mQuantity
	 */
	public double getAmount() {
		return mQuantity;
	}

	/**
	 * @param amount the mQuantity to set
	 */
	public void setAmout(double amount) {
		this.mQuantity = amount;
	}

	/**
	 * @return the mMaxAmount
	 */
	public double getMaxAmount() {
		return mMaxAmount;
	}

	/**
	 * @param maxAmount the mMaxAmount to set
	 */
	public void setMaxAmout(double maxAmount) {
		this.mMaxAmount = maxAmount;
	}
}
