package com.missionse.httpdatabaseconnector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Provides a parser to generate JSON objects.
 */
public class JSONParser {
	private static final String TAG = JSONParser.class.getName();
	private static final int BUFFER_SIZE = 8;

	/**
	 * Constructor.
	 */
	public JSONParser() {
	}

	/**
	 * Parses an input stream into a JSON object.
	 * @param input The input stream containing JSON.
	 * @return A JSON object that contains the data from the input stream.
	 */
	public JSONObject parse(final InputStream input) {
		JSONObject jsonObject = null;
		String json = null;

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(input, "iso-8859-1"), BUFFER_SIZE);
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				builder.append(line + "\n");
			}

			json = builder.toString();
		} catch (IOException e) {
			Log.e(TAG, "Error processing input stream: " + e.toString());
		}

		try {
			jsonObject = new JSONObject(json);
		} catch (JSONException e) {
			Log.e(TAG, "Error parsing JSON: " + e.toString());
		}

		return jsonObject;
	}
}
