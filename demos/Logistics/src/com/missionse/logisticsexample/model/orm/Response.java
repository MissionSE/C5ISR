package com.missionse.logisticsexample.model.orm;

import com.google.gson.annotations.SerializedName;

/**
 * Base class server response with json.
 */
public class Response {

	@SerializedName("success")
	private int mSuccess;

	@SerializedName("message")
	private String mMessage;

	/**
	 * Determines whether or not the associated action was successful.
	 * @return the success
	 */
	public boolean isSuccess() {
		return mSuccess > 0;
	}

	/**
	 * Sets the success of the associated action.
	 * @param success the success of the action
	 */
	public void setSuccess(final int success) {
		mSuccess = success;
	}

	/**
	 * Retrieves the message for this response.
	 * @return the message
	 */
	public String getMessage() {
		return mMessage;
	}

	/**
	 * Sets the message for this response.
	 * @param message the message to use
	 */
	public void setMessage(final String message) {
		mMessage = message;
	}

}
