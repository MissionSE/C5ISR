package com.missionse.gsonexample.model.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Specific weather measurements from the current weather data. 
 * @author rvieras
 *
 */
public class Measurement {
	
	@SerializedName("temp")
	private double mTempature;    
	
	@SerializedName("humidity")
	private double mHumidity;       
	
	@SerializedName("temp_min")
	private double mTempatureMin;
	
	@SerializedName("temp_max")
	private double mTempatureMax;
	
	@SerializedName("pressure")
	private double mPressure;
	
	/**
	 * Empty constructor. 
	 */
	public Measurement() {
		mTempature = 0.0;
		mHumidity = 0;
		mTempatureMin = 0.0;
		mTempatureMax = 0.0;
		mPressure = 0;
	}

	/**
	 * @return the mTempature
	 */
	public double getTempature() {
		return mTempature;
	}

	/**
	 * @param pTempature the mTempature to set
	 */
	public void setTempature(double pTempature) {
		this.mTempature = pTempature;
	}

	/**
	 * @return the mHumidity
	 */
	public double getHumidity() {
		return mHumidity;
	}

	/**
	 * @param pHumidity the mHumidity to set
	 */
	public void setHumidity(double pHumidity) {
		this.mHumidity = pHumidity;
	}

	/**
	 * @return the mTempatureMin
	 */
	public double getTempatureMin() {
		return mTempatureMin;
	}

	/**
	 * @param pTempatureMin the mTempatureMin to set
	 */
	public void setTempatureMin(double pTempatureMin) {
		this.mTempatureMin = pTempatureMin;
	}

	/**
	 * @return the mTempatureMax
	 */
	public double getTempatureMax() {
		return mTempatureMax;
	}

	/**
	 * @param pTempatureMax the mTempatureMax to set
	 */
	public void setTempatureMax(double pTempatureMax) {
		this.mTempatureMax = pTempatureMax;
	}

	/**
	 * @return the mPressure
	 */
	public double getPressure() {
		return mPressure;
	}

	/**
	 * @param pPressure the mPressure to set
	 */
	public void setPressure(double pPressure) {
		this.mPressure = pPressure;
	}
	
	

}
