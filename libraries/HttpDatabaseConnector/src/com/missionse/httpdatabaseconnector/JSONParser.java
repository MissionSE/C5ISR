package com.missionse.httpdatabaseconnector;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {

	private static final String TAG = JSONParser.class.getName();

	public JSONParser() {
	}

	public JSONObject parse(final InputStream input) {
		JSONObject jsonObject = null;
		String json = null;

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(input, "iso-8859-1"), 8);
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				builder.append(line + "\n");
			}

			json = builder.toString();
		} catch (Exception e) {
			Log.e(TAG, "Error converting result " + e.toString());
		}

		try {
			jsonObject = new JSONObject(json);
		} catch (JSONException e) {
			Log.e(TAG, "Error parsing data " + e.toString());
		}

		return jsonObject;
	}
}
