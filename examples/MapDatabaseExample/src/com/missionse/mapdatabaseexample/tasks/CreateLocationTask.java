package com.missionse.mapdatabaseexample.tasks;

import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.missionse.httpdatabaseconnector.HttpRequestTask;
import com.missionse.mapdatabaseexample.MapLocationListener;
import com.missionse.mapdatabaseexample.R;

/**
 * Provides a task that creates a student in the database.
 */
public class CreateLocationTask extends HttpRequestTask {
	private static final String TAG = CreateLocationTask.class.getName();
	private static final int EXPECTED_PARAMETERS = 3;

	private final String mTagName;
	private final String mTagLatitude;
	private final String mTagLongitude;
	private final String mCreateLocationURL;

	private final MapLocationListener mLocationListener;

	/**
	 * Constructor.
	 * @param context The context of the owner of the task.
	 * @param locationListener The listener who cares about the locations.
	 */
	public CreateLocationTask(final Context context, final MapLocationListener locationListener) {
		super(context, "Creating location");

		mLocationListener = locationListener;

		Resources resources = getContext().getResources();
		mTagName = resources.getString(R.string.tag_name);
		mTagLatitude = resources.getString(R.string.tag_latitude);
		mTagLongitude = resources.getString(R.string.tag_longitude);
		mCreateLocationURL = resources.getString(R.string.remote_db_path)
				+ resources.getString(R.string.create_location);
	}

	@Override
	protected String doInBackground(final String... params) {
		if (params.length != EXPECTED_PARAMETERS) {
			throw new RuntimeException("Invalid number of parameters.");
		}

		addParameter(mTagName, params[0]);
		addParameter(mTagLatitude, params[1]);
		addParameter(mTagLongitude, params[2]);
		JSONObject json = makePostRequest(mCreateLocationURL);

		if (json != null) {
			Log.d(TAG, "Create Student Result: " + json.toString());
		}

		return null;
	}

	@Override
	protected void onPostExecute(final String result) {
		super.onPostExecute(result);
		new GetAllLocationsTask(getContext(), mLocationListener).execute();
	}
}
