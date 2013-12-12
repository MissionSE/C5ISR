package com.missionse.logisticsexample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

/**
 * Represents a location on a map that contains supplies. 
 * @author rvieras
 *
 */
public class Site extends DBEntity {
	
	@Expose(serialize = true, deserialize = true)
	@SerializedName("name")
	@DatabaseField(columnName = "name")
	private String mName;
	
	@Expose(serialize = true, deserialize = true)
	@SerializedName("latitude")
	@DatabaseField(columnName = "latitude")
	private double mLatitude;
	
	@Expose(serialize = true, deserialize = true)
	@SerializedName("longitude")
	@DatabaseField(columnName = "longitude")
	private double mLongitude; 
	
	@Expose(serialize = true, deserialize = true)
	@SerializedName("parent_id")
	@DatabaseField(columnName = "parent_id")
	private int mParentId;
	
	/**
	 * Default constructor.  Needed for ORM library.
	 */
	public Site() { }

	/**
	 * @return the mName
	 */
	public String getName() {
		return mName;
	}

	/**
	 * @param name the mName to set
	 */
	public void setName(String name) {
		this.mName = name;
	}

	/**
	 * @return the mLatitude
	 */
	public double getLatitude() {
		return mLatitude;
	}

	/**
	 * @param latitude the mLatitude to set
	 */
	public void setLatitude(double latitude) {
		this.mLatitude = latitude;
	}

	/**
	 * @return the mLongitude
	 */
	public double getLongitude() {
		return mLongitude;
	}

	/**
	 * @param longitude the mLongitude to set
	 */
	public void setLongitude(double longitude) {
		this.mLongitude = longitude;
	}
}
