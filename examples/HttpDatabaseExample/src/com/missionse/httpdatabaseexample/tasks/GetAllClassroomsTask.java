package com.missionse.httpdatabaseexample.tasks;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.missionse.httpdatabaseconnector.HttpRequestTask;
import com.missionse.httpdatabaseexample.R;
import com.missionse.httpdatabaseexample.model.Classroom;

public class GetAllClassroomsTask extends HttpRequestTask {
	private static final String TAG = GetAllClassroomsTask.class.getName();

	private final String tagSuccess;
	private final String tagClassrooms;
	private final String tagName;
	private final String getClassroomsURL;

	private final ArrayAdapter<Classroom> classroomListAdapter;
	private final List<Classroom> classroomList;

	public GetAllClassroomsTask(final Context context, final ArrayAdapter<Classroom> adapter, final List<Classroom> classrooms) {
		super(context, "Getting list of classrooms");

		classroomListAdapter = adapter;
		classroomList = classrooms;

		Resources resources = getContext().getResources();
		tagSuccess = resources.getString(R.string.tag_success);
		tagClassrooms = resources.getString(R.string.tag_classrooms);
		tagName = resources.getString(R.string.tag_name);
		getClassroomsURL = resources.getString(R.string.remote_db_path) +
				resources.getString(R.string.get_all_classrooms);
	}

	@Override
	protected String doInBackground(final String... params) {
		JSONObject json = makeGetRequest(getClassroomsURL);
		if (json != null) {
			Log.d(TAG, "All classrooms: " + json.toString());

			try {
				if (json.getInt(tagSuccess) == 1) {
					classroomList.clear();

					JSONArray classrooms = json.getJSONArray(tagClassrooms);
					for (int index = 0; index < classrooms.length(); ++index) {
						JSONObject classroomJSON = classrooms.getJSONObject(index);
						Classroom classroom = new Classroom(classroomJSON.getString(tagName));
						classroomList.add(classroom);
					}
					((Activity) getContext()).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							classroomListAdapter.notifyDataSetChanged();
						}
					});
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return null;
	}
}
