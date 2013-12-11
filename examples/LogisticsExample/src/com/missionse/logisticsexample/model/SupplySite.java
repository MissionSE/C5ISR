package com.missionse.logisticsexample.model;

import com.j256.ormlite.field.DatabaseField;

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
	

}
