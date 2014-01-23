package com.missionse.httpdatabaseconnector;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Provides an asynchronous task used for http requests.
 */
public abstract class HttpRequestTask extends AsyncTask<String, String, String> {
	private final Context mContext;
	private final String mMessage;

	private boolean mError = false;
	private List<NameValuePair> mParameters = new ArrayList<NameValuePair>();
	private HttpClientRequester mHttpRequester = new HttpClientRequester();

	/**
	 * Constructor used to create a task with no error message.
	 * @param context The context of the owner of the task.
	 */
	public HttpRequestTask(final Context context) {
		mContext = context;
		mMessage = "";
	}

	/**
	 * Constructor used to create a task that displays a toast when an error occurs.
	 * @param context The context of the owner of the task.
	 * @param errorMessage The message to display while loading.
	 */
	public HttpRequestTask(final Context context, final String errorMessage) {
		mContext = context;
		mMessage = errorMessage;
	}

	protected Context getContext() {
		return mContext;
	}

	protected void addParameter(final NameValuePair parameter) {
		mParameters.add(parameter);
	}

	protected void addParameter(final String name, final String value) {
		mParameters.add(new BasicNameValuePair(name, value));
	}

	protected JSONObject makeGetRequest(final String url) {
		JSONObject response = mHttpRequester.makeHttpGetRequest(url, mParameters);
		if (null == response) {
			mError = true;
		}
		return response;
	}

	protected JSONObject makePostRequest(final String url) {
		JSONObject response = mHttpRequester.makeHttpPostRequest(url, mParameters);
		if (null == response) {
			mError = true;
		}
		return response;
	}

	@Override
	protected void onPostExecute(final String result) {
		super.onPostExecute(result);

		if (mError && !mMessage.equals("")) {
			Toast.makeText(mContext, mMessage, Toast.LENGTH_SHORT).show();
		}
	}
}
