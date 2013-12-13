package com.missionse.logisticsexample.database;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.missionse.httpdatabaseconnector.HttpRequestTask;
import com.missionse.logisticsexample.R;
import com.missionse.logisticsexample.map.MapLocation;
import com.missionse.logisticsexample.map.MapLocationListener;

/**
 * Provides a task that gets all locations in the database.
 */
public class GetAllLocationsTask extends HttpRequestTask {
	private static final String TAG = GetAllLocationsTask.class.getName();

	private final String mTagSuccess;
	private final String mTagLocations;
	private final String mTagId;
	private final String mTagName;
	private final String mTagLatitude;
	private final String mTagLongitude;
	private final String mGetLocationsURL;

	private final MapLocationListener mLocationListener;
	private final List<MapLocation> mLocations;

	/**
	 * Constructor.
	 * @param context The context of the owner of the task.
	 * @param locationListener The listener who cares about the locations.
	 */
	public GetAllLocationsTask(final Context context, final MapLocationListener locationListener) {
		super(context, "Unable to retrieve list of locations.");

		mLocationListener = locationListener;
		mLocations = new ArrayList<MapLocation>();

		Resources resources = getContext().getResources();
		mTagSuccess = resources.getString(R.string.tag_success);
		mTagLocations = resources.getString(R.string.tag_locations);
		mTagId = resources.getString(R.string.tag_id);
		mTagName = resources.getString(R.string.tag_name);
		mTagLatitude = resources.getString(R.string.tag_latitude);
		mTagLongitude = resources.getString(R.string.tag_longitude);
		mGetLocationsURL = resources.getString(R.string.map_db_path)
				+ resources.getString(R.string.get_all_locations);
	}

	@Override
	protected String doInBackground(final String... params) {
		JSONObject json = makeGetRequest(mGetLocationsURL);
		if (json != null) {
			Log.d(TAG, "All locations: " + json.toString());

			try {
				if (json.getInt(mTagSuccess) == 1) {
					JSONArray locations = json.getJSONArray(mTagLocations);
					for (int index = 0; index < locations.length(); ++index) {
						JSONObject locationJSON = locations.getJSONObject(index);
						MapLocation location = new MapLocation(
								locationJSON.getInt(mTagId),
								locationJSON.getString(mTagName),
								locationJSON.getDouble(mTagLatitude),
								locationJSON.getDouble(mTagLongitude));
						mLocations.add(location);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	@Override
	protected void onPostExecute(final String result) {
		super.onPostExecute(result);
		for (MapLocation location : mLocations) {
			mLocationListener.locationReceived(location);
		}
	}
}
