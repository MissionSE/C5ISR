package com.missionse.logisticsexample.model;

import java.util.Map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

/**
 * Mapping between a severity name and its id.
 */
public class SeverityName extends DBEntity {
	@Expose(serialize = true, deserialize = true)
	@SerializedName("name")
	@DatabaseField(columnName = "name")
	private String mName;

	/**
	 * Default Constructor.
	 */
	public SeverityName() {
	}

	/**
	 * Retrieves the name of the severity.
	 * @return The name of the severity.
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Sets the name of the severity.
	 * @param name The name of the severity.
	 */
	public void setName(final String name) {
		mName = name;
	}

	@Override
	public String toString() {
		return mName;
	}

	@Override
	public Map<String, String> toMap() {
		Map<String, String> map = super.toMap();
		map.put("name", mName);

		return map;
	}
}
