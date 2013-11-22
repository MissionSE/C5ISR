package com.missionse.httpdatabaseexample.tasks;

import java.util.List;

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

public class CreateClassroomTask extends HttpRequestTask {
	private static final String TAG = GetAllClassroomsTask.class.getName();

	private final String tagSuccess;
	private final String tagName;
	private final String addClassroomsURL;

	private final ArrayAdapter<Classroom> classroomListAdapter;
	private final List<Classroom> classroomList;

	public CreateClassroomTask(final Context context, final ArrayAdapter<Classroom> adapter, final List<Classroom> classrooms) {
		super(context, "Creating classroom");
		classroomListAdapter = adapter;
		classroomList = classrooms;

		Resources resources = getContext().getResources();
		tagSuccess = resources.getString(R.string.tag_success);
		tagName = resources.getString(R.string.tag_name);
		addClassroomsURL = resources.getString(R.string.remote_db_path) +
				resources.getString(R.string.create_classroom);
	}

	@Override
	protected String doInBackground(final String... params) {
		if (params.length != 1) {
			throw new RuntimeException("Invalid number of parameters.");
		}

		Classroom classroom = new Classroom(params[0]);
		addParameter(tagName, classroom.getName());
		JSONObject json = makePostRequest(addClassroomsURL);

		if (json != null) {
			Log.d(TAG, "Create Classroom Result: " + json.toString());

			try {
				if (json.getInt(tagSuccess) == 1) {
					classroomList.add(classroom);
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
