package com.missionse.logisticsexample.model.orm;

import com.google.gson.annotations.SerializedName;

/**
 * Response for the creation of an item in a table.
 */
public class CreateResponse extends Response {

	@SerializedName("id")
	private int mId;

	/**
	 * Gets the id.
	 * @return The id.
	 */
	public int getId() {
		return mId;
	}

	/**
	 * Sets the id.
	 * @param id The id.
	 */
	public void setId(final int id) {
		mId = id;
	}
}
