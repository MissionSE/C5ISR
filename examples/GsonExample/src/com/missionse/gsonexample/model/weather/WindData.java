package com.missionse.gsonexample.model.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Wind information for the current weather data set. 
 */
public class WindData {
	
	@SerializedName("speed")
	private double mSpeed;
	
	@SerializedName("deg")
	private double mDeg;
	
	/**
	 * Empty constructor.
	 */
	public WindData() {
		mSpeed = 0;
		mDeg = 0;
	}

	/**
	 * @return the mSpeed
	 */
	public double getSpeed() {
		return mSpeed;
	}

	/**
	 * @param pSpeed the mSpeed to set
	 */
	public void setSpeed(double pSpeed) {
		this.mSpeed = pSpeed;
	}

	/**
	 * @return the mDeg
	 */
	public double getDeg() {
		return mDeg;
	}

	/**
	 * @param pDeg the mDeg to set
	 */
	public void setDeg(double pDeg) {
		this.mDeg = pDeg;
	}
}
