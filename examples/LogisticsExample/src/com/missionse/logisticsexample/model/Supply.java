package com.missionse.logisticsexample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

/**
 * Generic Supply.
 */
public class Supply extends DBEntity {
	
	@Expose(serialize = true, deserialize = true)
	@SerializedName("name")
	@DatabaseField(columnName = "name")
	private String mName;
	
	/**
	 * Empty constructor. 
	 */
	public Supply() { }

	/**
	 * @return the mName
	 */
	public String getmName() {
		return mName;
	}

	/**
	 * @param name the mName to set
	 */
	public void setmName(String name) {
		this.mName = name;
	}
}
