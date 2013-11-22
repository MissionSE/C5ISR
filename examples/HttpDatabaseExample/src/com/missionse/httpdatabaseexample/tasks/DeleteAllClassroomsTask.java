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

public class DeleteAllClassroomsTask extends HttpRequestTask {
	private static final String TAG = DeleteAllClassroomsTask.class.getName();

	private final String tagSuccess;
	private final String deleteClassroomsURL;

	private final ArrayAdapter<Classroom> classroomListAdapter;
	private final List<Classroom> classroomList;

	public DeleteAllClassroomsTask(final Context context, final ArrayAdapter<Classroom> adapter, final List<Classroom> classrooms) {
		super(context, "Deleting all classrooms");

		classroomListAdapter = adapter;
		classroomList = classrooms;

		Resources resources = getContext().getResources();
		tagSuccess = resources.getString(R.string.tag_success);
		deleteClassroomsURL = resources.getString(R.string.remote_db_path) +
				resources.getString(R.string.delete_all_classrooms);
	}

	@Override
	protected String doInBackground(final String... params) {
		JSONObject json = makeGetRequest(deleteClassroomsURL);
		if (json != null) {
			Log.d(TAG, "Delete result: " + json.toString());

			try {
				if (json.getInt(tagSuccess) == 1) {
					classroomList.clear();
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
