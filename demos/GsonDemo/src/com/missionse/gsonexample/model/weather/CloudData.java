package com.missionse.gsonexample.model.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Cloud information on the current weather data. 
 * @author rvieras
 *
 */
public class CloudData {
	
	@SerializedName("all")
	private int mCoverage;

	/**
	 * Empty constructor. 
	 */
	public CloudData() {
		mCoverage = 0;
	}

	/**
	 * @return the mCoverage
	 */
	public int getCoverage() {
		return mCoverage;
	}

	/**
	 * @param pCoverage the mCoverage to set
	 */
	public void setCoverage(int pCoverage) {
		this.mCoverage = pCoverage;
	}
	
	
}
