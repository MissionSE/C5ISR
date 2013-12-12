package com.missionse.logisticsexample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

/**
 * Mapping between an item id and its name.
 */
public class ItemName extends DBEntity {
	
	@Expose(serialize = true, deserialize = true)
	@SerializedName("name")
	@DatabaseField(columnName = "name")
	private String mName;
	
	/**
	 * Empty constructor. 
	 */
	public ItemName() { }

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
