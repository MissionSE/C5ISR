package com.missionse.gsonexample.model.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Condition relating to the current weather data.
 * @author rvieras
 *
 */
public class Condition {
	
	@SerializedName("id")
	private int mId;
	
	@SerializedName("main")
	private String mName;
	
	@SerializedName("description")
	private String mDescription;
	
	@SerializedName("icon")
	private String mIconName;
	
	/**
	 * Empty constructor. 
	 */
	public Condition() {
		mId = 0;
		mName = "";
		mDescription = "";
		mIconName = "";
	}

	/**
	 * @return the mId
	 */
	public int getId() {
		return mId;
	}

	/**
	 * @param pId the mId to set
	 */
	public void setId(int pId) {
		this.mId = pId;
	}

	/**
	 * @return the mName
	 */
	public String getName() {
		return mName;
	}

	/**
	 * @param pName the mName to set
	 */
	public void setName(String pName) {
		this.mName = pName;
	}

	/**
	 * @return the mDescription
	 */
	public String getDescription() {
		return mDescription;
	}

	/**
	 * @param pDescription the mDescription to set
	 */
	public void setDescription(String pDescription) {
		this.mDescription = pDescription;
	}

	/**
	 * @return the mIconName
	 */
	public String getIconName() {
		return mIconName;
	}

	/**
	 * @param pIconName the mIconName to set
	 */
	public void setIconName(String pIconName) {
		this.mIconName = pIconName;
	}
	
	

}
