package com.missionse.mapdatabaseexample.tasks;

import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.missionse.httpdatabaseconnector.HttpRequestTask;
import com.missionse.mapdatabaseexample.MapDatabaseExampleActivity;
import com.missionse.mapdatabaseexample.MapLocationListener;
import com.missionse.mapdatabaseexample.R;

/**
 * Provides a task that creates a location in the database.
 */
public class EditLocationTask extends HttpRequestTask {
	private static final String TAG = EditLocationTask.class.getName();
	private static final int EXPECTED_PARAMETERS = 4;
	private static final int ID_PARAMETER = 0;
	private static final int NAME_PARAMETER = 1;
	private static final int LATITUDE_PARAMETER = 2;
	private static final int LONGITUDE_PARAMETER = 3;

	private final String mTagId;
	private final String mTagName;
	private final String mTagLatitude;
	private final String mTagLongitude;
	private final String mEditLocationURL;

	private final MapLocationListener mLocationListener;

	/**
	 * Constructor.
	 * @param context The context of the owner of the task.
	 * @param locationListener The listener who cares about the locations.
	 */
	public EditLocationTask(final Context context, final MapLocationListener locationListener) {
		super(context, "Unable to save location modification.");

		mLocationListener = locationListener;

		Resources resources = getContext().getResources();
		mTagId = resources.getString(R.string.tag_id);
		mTagName = resources.getString(R.string.tag_name);
		mTagLatitude = resources.getString(R.string.tag_latitude);
		mTagLongitude = resources.getString(R.string.tag_longitude);
		mEditLocationURL = resources.getString(R.string.remote_db_path)
				+ resources.getString(R.string.edit_location);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((MapDatabaseExampleActivity) getContext()).setDatabaseConnectionInProgress(true);
	}

	@Override
	protected String doInBackground(final String... params) {
		if (params.length != EXPECTED_PARAMETERS) {
			throw new RuntimeException("Invalid number of parameters.");
		}

		addParameter(mTagId, params[ID_PARAMETER]);
		addParameter(mTagName, params[NAME_PARAMETER]);
		addParameter(mTagLatitude, params[LATITUDE_PARAMETER]);
		addParameter(mTagLongitude, params[LONGITUDE_PARAMETER]);
		JSONObject json = makePostRequest(mEditLocationURL);

		if (json != null) {
			Log.d(TAG, "Edit Location Result: " + json.toString());
		}

		return null;
	}

	@Override
	protected void onPostExecute(final String result) {
		super.onPostExecute(result);
		new GetAllLocationsTask(getContext(), mLocationListener).execute();
	}
}
