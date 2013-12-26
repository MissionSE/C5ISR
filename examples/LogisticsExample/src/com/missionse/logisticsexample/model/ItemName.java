package com.missionse.logisticsexample.model;

import java.util.Map;

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
	 * Default constructor.
	 */
	public ItemName() {
	}

	/**
	 * Retrieves the name of this item.
	 * @return The name of the item.
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Sets the name of the item.
	 * @param name The name of the item.
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
