package com.missionse.httpdatabaseconnector;

import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.util.Log;

public class HttpClientRequester {

	private static final String TAG = HttpClientRequester.class.getName();

	private final JSONParser jsonParser;

	public HttpClientRequester() {
		jsonParser = new JSONParser();
	}

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

			json = jsonParser.parse(input);
			input.close();
		} catch (Exception e) {
			Log.e(TAG, "Error getting HTTP Get Response: " + e.toString());
		}

		return json;
	}

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

			json = jsonParser.parse(input);
			input.close();
		} catch (Exception e) {
			Log.e(TAG, "Error getting HTTP Post Response: " + e.toString());
		}

		return json;
	}

}
