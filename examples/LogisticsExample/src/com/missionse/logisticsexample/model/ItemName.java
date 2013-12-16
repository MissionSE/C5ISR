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
	public ItemName() {
	}

	/**
	 * Retrieves the name of this item.
	 * @return the name
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Sets the name of this item.
	 * @param name the name
	 */
	public void setName(final String name) {
		mName = name;
	}
}
