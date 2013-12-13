package com.missionse.logisticsexample.model.orm;

import com.google.gson.annotations.SerializedName;

/**
 * Base class sever response with json.
 */
public class Response {
	
	@SerializedName("success")
	private int mSuccess;
	
	@SerializedName("message")
	private String mMessage;

	/**
	 * @return the mSuccess
	 */
	public boolean isSuccess() {
		return mSuccess > 0;
	}

	/**
	 * @param success the mSuccess to set
	 */
	public void setSuccess(int success) {
		this.mSuccess = success;
	}

	/**
	 * @return the mMessage
	 */
	public String getMessage() {
		return mMessage;
	}

	/**
	 * @param message the mMessage to set
	 */
	public void setMessage(String message) {
		this.mMessage = message;
	}
	
	

}
