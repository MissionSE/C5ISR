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
import com.missionse.httpdatabaseexample.model.Student;

public class DeleteAllStudentsTask extends HttpRequestTask {
	private static final String TAG = DeleteAllStudentsTask.class.getName();

	private final String tagSuccess;
	private final String deleteStudentsURL;

	private final ArrayAdapter<Student> studentListAdapter;
	private final List<Student> studentList;

	public DeleteAllStudentsTask(final Context context, final ArrayAdapter<Student> adapter, final List<Student> students) {
		super(context, "Deleting all students");

		studentListAdapter = adapter;
		studentList = students;

		Resources resources = getContext().getResources();
		tagSuccess = resources.getString(R.string.tag_success);
		deleteStudentsURL = resources.getString(R.string.remote_db_path) +
				resources.getString(R.string.delete_all_students);
	}

	@Override
	protected String doInBackground(final String... params) {
		JSONObject json = makeGetRequest(deleteStudentsURL);
		if (json != null) {
			Log.d(TAG, "Delete result: " + json.toString());

			try {
				if (json.getInt(tagSuccess) == 1) {
					studentList.clear();
					((Activity) getContext()).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							studentListAdapter.notifyDataSetChanged();
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
