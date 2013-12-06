package com.missionse.mapdatabaseexample.tasks;

import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.missionse.httpdatabaseconnector.HttpRequestTask;
import com.missionse.mapdatabaseexample.MapLocationListener;
import com.missionse.mapdatabaseexample.R;

/**
 * Provides a task that creates a location in the database.
 */
public class DeleteLocationTask extends HttpRequestTask {
	private static final String TAG = DeleteLocationTask.class.getName();
	private static final int EXPECTED_PARAMETERS = 1;
	private static final int ID_PARAMETER = 0;

	private final String mTagId;
	private final String mDeleteLocationURL;

	private final MapLocationListener mLocationListener;

	/**
	 * Constructor.
	 * @param context The context of the owner of the task.
	 * @param locationListener The listener who cares about the locations.
	 */
	public DeleteLocationTask(final Context context, final MapLocationListener locationListener) {
		super(context, "Deleting location");

		mLocationListener = locationListener;

		Resources resources = getContext().getResources();
		mTagId = resources.getString(R.string.tag_id);
		mDeleteLocationURL = resources.getString(R.string.remote_db_path)
				+ resources.getString(R.string.delete_location);
	}

	@Override
	protected String doInBackground(final String... params) {
		if (params.length != EXPECTED_PARAMETERS) {
			throw new RuntimeException("Invalid number of parameters.");
		}

		addParameter(mTagId, params[ID_PARAMETER]);
		JSONObject json = makePostRequest(mDeleteLocationURL);

		if (json != null) {
			Log.d(TAG, "Delete Location Result: " + json.toString());
		}

		return null;
	}

	@Override
	protected void onPostExecute(final String result) {
		super.onPostExecute(result);
		new GetAllLocationsTask(getContext(), mLocationListener).execute();
	}
}
