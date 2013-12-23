package com.missionse.logisticsexample.database;

import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders.Any.B;
import com.missionse.logisticsexample.R;
import com.missionse.logisticsexample.model.orm.Response;

/**
 * Provides functionality to make requests to a remote database using Ion.
 */
public class DatabaseConnector {
	private static final String TAG = DatabaseConnector.class.getName();

	private Context mContext;
	private String mDatabaseUrl;

	/**
	 * Constructor.
	 * @param context The context for the activity.
	 */
	public DatabaseConnector(final Context context) {
		mContext = context;
		mDatabaseUrl = context.getResources().getString(R.string.remote_db_path);
	}

	/**
	 * Creates an ion call that posts a request to a remote database.
	 * @param <T> The type of the response expected.
	 * @param script The name of the script on the remote database to handle the request.
	 * @param token A token with the type of the response expected.
	 * @param callback A callback to handle the response.
	 */
	public synchronized <T> void postRequest(final String script, final TypeToken<T> token, final FutureCallback<T> callback) {
		Ion.with(mContext, mDatabaseUrl + script)
				.as(token).setCallback(callback);
	}

	/**
	 * Creates an ion call that posts a request to a remote database.
	 * @param <T> The type of the response expected.
	 * @param script The name of the script on the remote database to handle the request.
	 * @param token A token with the type of the response expected.
	 * @param parameters A mapping of parameters that will be added as post arguments.
	 * @param callback A callback to handle the response.
	 */
	public synchronized <T> void postRequest(final String script, final TypeToken<T> token,
			final Map<String, String> parameters, final FutureCallback<T> callback) {
		B requestBuilder = Ion.with(mContext, mDatabaseUrl + script);
		for (Map.Entry<String, String> parameter : parameters.entrySet()) {
			requestBuilder.setBodyParameter(parameter.getKey(), parameter.getValue());
		}
		requestBuilder.as(token).setCallback(callback);
	}

	/**
	 * Verifies that an exeception was not thrown in a post request.
	 * @param exception The exception that is returned from a request.
	 * @param type The type of request that was performed.
	 * @return True if an exception was not thrown in response to the request.
	 */
	public synchronized boolean verifyException(final Exception exception, final String type) {
		boolean exceptionThrown = false;
		if (exception != null) {
			Log.d(TAG, "An exception was thrown in the request for " + type + ": ", exception);
			exceptionThrown = true;
		}

		return !exceptionThrown;
	}

	/**
	 * Determines whether a request was successful as indicated by the response.
	 * @param response The response returned by the post request.
	 * @param type The type of request that was performed.
	 * @return Whether the request was successful or not.
	 */
	public synchronized boolean verifyResponse(final Response response, final String type) {
		boolean responseValid = false;
		if (response != null) {
			if (response.isSuccess()) {
				responseValid = true;
			} else {
				Log.d(TAG, "The request for " + type + " was unsuccessful: " + response.getMessage());
			}
		}

		return responseValid;
	}
}
