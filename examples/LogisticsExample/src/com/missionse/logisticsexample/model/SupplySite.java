package com.missionse.logisticsexample.model;

import java.util.Collection;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

/**
 * Represents a location on a map that contains supplies. 
 * @author rvieras
 *
 */
public class SupplySite {
	@DatabaseField(generatedId = true, columnName = "_id")
	private int mId;
	
	@DatabaseField(columnName = "name")
	private String mName;
	
	@DatabaseField(columnName = "latitude")
	private double mLatitude;
	
	@DatabaseField(columnName = "longitude")
	private double mLongitude; 
	
	@ForeignCollectionField
	private Collection<Supply> mInventory;
	
	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "site_id")
	private SupplySite mParent;
	
	
	/**
	 * Default constructor.  Needed for ORM library.
	 */
	public SupplySite() { }

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

	/**
	 * @return the mInventory
	 */
	public Collection<Supply> getInventory() {
		return mInventory;
	}

	/**
	 * @param inventory the mInventory to set
	 */
	public void setInventory(Collection<Supply> inventory) {
		this.mInventory = inventory;
	}

	/**
	 * @return the mParent
	 */
	public SupplySite getParent() {
		return mParent;
	}

	/**
	 * @param parent the mParent to set
	 */
	public void setParent(SupplySite parent) {
		this.mParent = parent;
	}

	/**
	 * @return the mId
	 */
	public int getId() {
		return mId;
	}
}
