package com.missionse.logisticsexample.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Represents supply/assets for different locations.
 * @author rvieras
 *
 */
public class Supply {
	@DatabaseField(generatedId = true, columnName = "_id")
	private int mId;
	
	@DatabaseField(columnName = "name")
	private String mName;
	
	@DatabaseField(columnName = "amount")
	private double mAmount;
	
	@DatabaseField(columnName = "maxamount")
	private double mMaxAmount;
	
	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "order_id")
	private Order mOrder;
	
	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "site_id")
	private SupplySite mSite;
	
	/**
	 * Default constructor needed by ORM library. 
	 */
	public Supply() { }

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

	/**
	 * @return the mId
	 */
	public int getId() {
		return mId;
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
