package com.missionse.httpdatabaseconnector;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public abstract class HttpRequestTask extends AsyncTask<String, String, String> {
	private final Context context;
	private final String message;

	private ProgressDialog progressDialog;
	private List<NameValuePair> parameters = new ArrayList<NameValuePair>();
	private HttpClientRequester httpRequester = new HttpClientRequester();

	public HttpRequestTask(final Context context, final String loadingMessage) {
		this.context = context;
		this.message = loadingMessage;
	}

	protected Context getContext() {
		return context;
	}

	protected void addParameter(final NameValuePair parameter) {
		parameters.add(parameter);
	}

	protected void addParameter(final String name, final String value) {
		parameters.add(new BasicNameValuePair(name, value));
	}

	protected JSONObject makeGetRequest(final String url) {
		return httpRequester.makeHttpGetRequest(url, parameters);
	}

	protected JSONObject makePostRequest(final String url) {
		return httpRequester.makeHttpPostRequest(url, parameters);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(message);
		progressDialog.setIndeterminate(false);
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	@Override
	protected void onPostExecute(final String result) {
		progressDialog.dismiss();
	}
}
