package com.missionse.gsonexample.model.weather;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * This model acts as an interface for interacting with the open weather api. 
 * @author rvieras
 *
 */
public class WeatherData {
	
	@SerializedName("dt")
	private double mRecieveDate;
	
	@SerializedName("name")
	private String mCityName;
	
	@SerializedName("coord")
	private Coordinates mLocation;
	
	@SerializedName("main")
	private Measurement mMeasurements;
	
	@SerializedName("clouds")
	private CloudData mCloudData;
	
	@SerializedName("wind")
	private WindData mWindData;
	
	@SerializedName("rain")
	private RainData mRainData;
	
	@SerializedName("snow")
	private SnowData mSnowData;
	
	@SerializedName("weather")
	private List<Condition> mConditions;
	
	/**
	 * Empty constructor. 
	 */
	public WeatherData() {
		mRecieveDate = 0;
		mCityName = "";
		mLocation = new Coordinates();
		mMeasurements = new Measurement();
		mCloudData = new CloudData();
		mWindData = new WindData();
		mSnowData = new SnowData();
		mConditions = new ArrayList<Condition>();
	}

	/**
	 * @return the mRecieveDate
	 */
	public double getRecieveDate() {
		return mRecieveDate;
	}

	/**
	 * @param pRecieveDate the mRecieveDate to set
	 */
	public void setRecieveDate(double pRecieveDate) {
		this.mRecieveDate = pRecieveDate;
	}

	/**
	 * @return the mCityName
	 */
	public String getCityName() {
		return mCityName;
	}

	/**
	 * @param pCityName the mCityName to set
	 */
	public void setCityName(String pCityName) {
		this.mCityName = pCityName;
	}

	/**
	 * @return the mLocation
	 */
	public Coordinates getLocation() {
		return mLocation;
	}

	/**
	 * @param pLocation the mLocation to set
	 */
	public void setLocation(Coordinates pLocation) {
		this.mLocation = pLocation;
	}

	/**
	 * @return the mMeasurements
	 */
	public Measurement getMeasurements() {
		return mMeasurements;
	}

	/**
	 * @param pMeasurements the mMeasurements to set
	 */
	public void setMeasurements(Measurement pMeasurements) {
		this.mMeasurements = pMeasurements;
	}

	/**
	 * @return the mCloudData
	 */
	public CloudData getCloudData() {
		return mCloudData;
	}

	/**
	 * @param pCloudData the mCloudData to set
	 */
	public void setCloudData(CloudData pCloudData) {
		this.mCloudData = pCloudData;
	}

	/**
	 * @return the mWindData
	 */
	public WindData getWindData() {
		return mWindData;
	}

	/**
	 * @param pWindData the mWindData to set
	 */
	public void setWindData(WindData pWindData) {
		this.mWindData = pWindData;
	}

	/**
	 * @return the mRainData
	 */
	public RainData getRainData() {
		return mRainData;
	}

	/**
	 * @param pRainData the mRainData to set
	 */
	public void setRainData(RainData pRainData) {
		this.mRainData = pRainData;
	}

	/**
	 * @return the mSnowData
	 */
	public SnowData getSnowData() {
		return mSnowData;
	}

	/**
	 * @param pSnowData the mSnowData to set
	 */
	public void setSnowData(SnowData pSnowData) {
		this.mSnowData = pSnowData;
	}

	/**
	 * @return the mConditions
	 */
	public List<Condition> getConditions() {
		return mConditions;
	}

	/**
	 * @param pConditions the mConditions to set
	 */
	public void setConditions(List<Condition> pConditions) {
		this.mConditions = pConditions;
	}
	
	
	
}
