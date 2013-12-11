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
	private double mAmout;
	
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
	 * @return the mAmout
	 */
	public double getAmout() {
		return mAmout;
	}

	/**
	 * @param amout the mAmout to set
	 */
	public void setAmout(double amout) {
		this.mAmout = amout;
	}

	/**
	 * @return the mId
	 */
	public int getId() {
		return mId;
	}
}
