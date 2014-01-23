package com.missionse.logisticsexample.model;

import java.util.HashMap;
import java.util.Map;

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

	/**
	 * Creates a map representation of the fields in the entity.
	 * @return A map containting the fields in the entity.
	 */
	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", Integer.toString(mId));

		return map;
	}
}
