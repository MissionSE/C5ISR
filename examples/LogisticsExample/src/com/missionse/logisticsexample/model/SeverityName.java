package com.missionse.logisticsexample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

/**
 * Severity name. Maps to database table.
 */
public class SeverityName extends DBEntity {
	@Expose(serialize = true, deserialize = true)
	@SerializedName("name")
	@DatabaseField(columnName = "name")
	private String mName;

	/**
	 * Empty constructor.
	 */
	public SeverityName() {
	}

	/**
	 * Retrieves the name of this severity.
	 * 
	 * @return the name
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Sets the name of this severity.
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
