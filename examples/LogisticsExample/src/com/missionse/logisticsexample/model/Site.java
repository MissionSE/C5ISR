package com.missionse.logisticsexample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

/**
 * Represents a location on a map that contains supplies.
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

	/**
	 * Default constructor. Needed for ORM library.
	 */
	public Site() {
	}

	/**
	 * Retrieves the name of this site.
	 * @return the name
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Sets the name of this site.
	 * @param name the name to set
	 */
	public void setName(final String name) {
		mName = name;
	}

	/**
	 * Retrieves the latitude of this site.
	 * @return the latitude
	 */
	public double getLatitude() {
		return mLatitude;
	}

	/**
	 * Sets the latitude of this site.
	 * @param latitude the latitude to set
	 */
	public void setLatitude(final double latitude) {
		mLatitude = latitude;
	}

	/**
	 * Retrieves the longitude of this site.
	 * @return the longitude
	 */
	public double getLongitude() {
		return mLongitude;
	}

	/**
	 * Sets the longitude of this site.
	 * @param longitude the longitude to set
	 */
	public void setLongitude(final double longitude) {
		mLongitude = longitude;
	}

	/**
	 * Retrieves the id of the parent associated with this site.
	 * @return the id
	 */
	public int getParentId() {
		return mParentId;
	}

	/**
	 * Sets the parent id stored with this site to link it to its parent.
	 * @param id the parent's id
	 */
	public void setParentId(final int id) {
		mParentId = id;
	}

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		string.append("Site>: ");
		string.append(" id = " + getId());
		string.append(" name = " + mName);
		string.append(" lat = " + mLatitude);
		string.append(" lng = " + mLongitude);
		string.append(" parentId = " + mParentId);
		return string.toString();
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
