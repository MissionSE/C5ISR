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

	private Toast mConnectionToast;
	private List<NameValuePair> mParameters = new ArrayList<NameValuePair>();
	private HttpClientRequester mHttpRequester = new HttpClientRequester();

	/**
	 * Constructor.
	 * @param context The context of the owner of the task.
	 * @param loadingMessage The message to display while loading.
	 */
	public HttpRequestTask(final Context context, final String loadingMessage) {
		mContext = context;
		mMessage = loadingMessage;
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
		return mHttpRequester.makeHttpGetRequest(url, mParameters);
	}

	protected JSONObject makePostRequest(final String url) {
		return mHttpRequester.makeHttpPostRequest(url, mParameters);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mConnectionToast = Toast.makeText(mContext, mMessage, Toast.LENGTH_LONG);
		mConnectionToast.show();
	}

	@Override
	protected void onPostExecute(final String result) {
		mConnectionToast.cancel();
	}
}
