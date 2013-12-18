package com.missionse.logisticsexample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

/**
 * Database entity.
 */
public class DBEntity {

	@Expose(serialize = true, deserialize = true)
	@SerializedName("id")
	@DatabaseField(id = true, columnName = "_id")
	private int mId;

	/**
	 * Returns the id.
	 * @return the id
	 */
	public int getId() {
		return mId;
	}

	/**
	 * Sets the id.
	 * @param id the id to set
	 */
	public void setId(final int id) {
		mId = id;
	}
}
