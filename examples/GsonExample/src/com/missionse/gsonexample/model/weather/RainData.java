package com.missionse.gsonexample.model.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Rain information based on the current weather data.
 * @author rvieras
 *
 */
public class RainData {
	
	@SerializedName("3h")
	private double mVolumnPerThreeHours;
	
	/**
	 * Empty constructor.
	 */
	public RainData() {
		mVolumnPerThreeHours = 0;
	}

	/**
	 * @return the mVolumnPerThreeHours
	 */
	public double getVolumnPerThreeHours() {
		return mVolumnPerThreeHours;
	}

	/**
	 * @param pVolumnPerThreeHours the mVolumnPerThreeHours to set
	 */
	public void setVolumnPerThreeHours(double pVolumnPerThreeHours) {
		this.mVolumnPerThreeHours = pVolumnPerThreeHours;
	}
}
