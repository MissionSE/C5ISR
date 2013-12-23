package com.missionse.logisticsexample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

/**
 * Status name. Maps to a database table.
 */
public class StatusName extends DBEntity {

	@Expose(serialize = true, deserialize = true)
	@SerializedName("name")
	@DatabaseField(columnName = "name")
	private String mName;

	/**
	 * Empty constructor.
	 */
	public StatusName() {
	}

	/**
	 * Retrieves the name of this status.
	 * 
	 * @return the name
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Sets the name of this status.
	 * 
	 * @param name
	 *            the name
	 */
	public void setName(final String name) {
		mName = name;
	}

	@Override
	public String toString() {
		return mName;
	}

}
