package com.missionse.httpdatabaseconnector;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.util.Log;

/**
 * Provides functionality to perform http post and get requests.
 */
public class HttpClientRequester {
	private static final String TAG = HttpClientRequester.class.getName();

	private final JSONParser mJsonParser;

	/**
	 * Constructor.
	 */
	public HttpClientRequester() {
		mJsonParser = new JSONParser();
	}

	/**
	 * Makes an http get request with a specified url and parameters.
	 * @param url The http url with which the request will be made.
	 * @param parameters The parameters in the request.
	 * @return A JSON Object containing the response to the request.
	 */
	public JSONObject makeHttpGetRequest(final String url, final List<NameValuePair> parameters) {
		JSONObject json = null;
		InputStream input = null;

		DefaultHttpClient httpClient = new DefaultHttpClient();
		String paramString = URLEncodedUtils.format(parameters, "utf-8");
		String urlWithParams = url + "?" + paramString;
		HttpGet httpGet = new HttpGet(urlWithParams);

		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			input = httpEntity.getContent();

			json = mJsonParser.parse(input);
			input.close();
		} catch (ClientProtocolException e) {
			Log.e(TAG, "Error connecting to HTTP client: " + e.toString());
		} catch (IOException e) {
			Log.e(TAG, "Error handling input stream: " + e.toString());
		}

		return json;
	}

	/**
	 * Makes an http post request with a specified url and parameters.
	 * @param url The http url with which the request will be made.
	 * @param parameters The parameters in the request.
	 * @return A JSON Object containing the response to the request.
	 */
	public JSONObject makeHttpPostRequest(final String url, final List<NameValuePair> parameters) {
		JSONObject json = null;
		InputStream input = null;

		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(parameters));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			input = httpEntity.getContent();

			json = mJsonParser.parse(input);
			input.close();
		} catch (ClientProtocolException e) {
			Log.e(TAG, "Error connecting to HTTP client: " + e.toString());
		} catch (IOException e) {
			Log.e(TAG, "Error handling input stream: " + e.toString());
		}

		return json;
	}

}
