package com.missionse.logisticsexample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

/**
 * Database entity. 
 */
public class DBEntity {
	
	@Expose(serialize = true, deserialize = true)
	@SerializedName("_id")
	@DatabaseField(generatedId = true, columnName = "_id")
	private int mId;
	
	/**
	 * @return the mId
	 */
	public int getId() {
		return mId;
	}

	/**
	 * @param id the mId to set
	 */
	public void setId(int id) {
		this.mId = id;
	}


}
