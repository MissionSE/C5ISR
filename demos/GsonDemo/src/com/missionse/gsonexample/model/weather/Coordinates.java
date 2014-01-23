package com.missionse.gsonexample.model.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Location of the current weather data. 
 * @author rvieras
 *
 */
public class Coordinates {
	
	@SerializedName("lat")
	private double mLat;
	
	@SerializedName("lon")
	private double mLon;
	
	/**
	 * Empty constructor. 
	 */
	public Coordinates() {
		mLat = 0;
		mLon = 0;
	}

	/**
	 * @return the mLat
	 */
	public double getLat() {
		return mLat;
	}

	/**
	 * @param pLat the mLat to set
	 */
	public void setLat(double pLat) {
		this.mLat = pLat;
	}

	/**
	 * @return the mLon
	 */
	public double getLon() {
		return mLon;
	}

	/**
	 * @param pLon the mLon to set
	 */
	public void setLon(double pLon) {
		this.mLon = pLon;
	}
	
	

}
