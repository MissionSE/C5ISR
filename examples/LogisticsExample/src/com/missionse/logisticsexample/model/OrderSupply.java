package com.missionse.logisticsexample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
/**
 * Supplies for orders. 
 */
public class OrderSupply extends DBEntity {

	@Expose(serialize = true, deserialize = true)
	@SerializedName("name")
	@DatabaseField(columnName = "name")
	private String mName;
	
	@Expose(serialize = true, deserialize = true)
	@SerializedName("amount")
	@DatabaseField(columnName = "amount")
	private double mAmount;
		
	/**
	 * Default constructor needed by ORM library. 
	 */
	public OrderSupply() { }

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
	 * @return the mAmount
	 */
	public double getAmount() {
		return mAmount;
	}

	/**
	 * @param amount the mAmount to set
	 */
	public void setAmout(double amount) {
		this.mAmount = amount;
	}
}
