package com.missionse.logisticsexample.model;

import java.util.Map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

/**
 * Represents a location on a map.
 */
public class Site extends DBEntity implements Comparable<Object> {

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

	@Expose(serialize = true, deserialize = true)
	@SerializedName("short_name")
	@DatabaseField(columnName = "short_name")
	private String mShortName;

	/**
	 * Default constructor.
	 */
	public Site() {
	}

	/**
	 * Retrieves the name of the site.
	 * @return The name of the site.
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Sets the name of the site.
	 * @param name The name of the site.
	 */
	public void setName(final String name) {
		mName = name;
	}

	/**
	 * Retrieves the latitude of the site.
	 * @return The latitude of the site.
	 */
	public double getLatitude() {
		return mLatitude;
	}

	/**
	 * Sets the latitude of the site.
	 * @param latitude The latitude of the site.
	 */
	public void setLatitude(final double latitude) {
		mLatitude = latitude;
	}

	/**
	 * Retrieves the longitude of the site.
	 * @return The longitude of the site.
	 */
	public double getLongitude() {
		return mLongitude;
	}

	/**
	 * Sets the longitude of the site.
	 * @param longitude The longitude of the site.
	 */
	public void setLongitude(final double longitude) {
		mLongitude = longitude;
	}

	/**
	 * Retrieves the id of the parent associated with the site.
	 * @return The id of the parent associated with the site.
	 */
	public int getParentId() {
		return mParentId;
	}

	/**
	 * Sets the parent id associated with the site.
	 * @param id The id of the parent.
	 */
	public void setParentId(final int id) {
		mParentId = id;
	}

	@Override
	public String toString() {
		return mName;
	}

	@Override
	public Map<String, String> toMap() {
		Map<String, String> map = super.toMap();
		map.put("name", mName);
		map.put("short_name", mShortName);
		map.put("latitude", Double.toString(mLatitude));
		map.put("longitude", Double.toString(mLongitude));
		map.put("parent_id", Integer.toString(mParentId));

		return map;
	}

	@Override
	public int compareTo(final Object another) {
		if (another != null) {
			if (getClass() == another.getClass()) {
				return getName().compareTo(((Site) another).getName());
			} else {
				return 1;
			}
		} else {
			return 1;
		}
	}
}
