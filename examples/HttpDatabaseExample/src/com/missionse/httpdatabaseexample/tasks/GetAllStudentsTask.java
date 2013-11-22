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
import com.missionse.httpdatabaseexample.model.Student;

public class GetAllStudentsTask extends HttpRequestTask {
	private static final String TAG = GetAllStudentsTask.class.getName();

	private final String tagSuccess;
	private final String tagStudents;
	private final String tagFirstName;
	private final String tagLastName;
	private final String getStudentsURL;

	private final ArrayAdapter<Student> studentListAdapter;
	private final List<Student> studentList;

	public GetAllStudentsTask(final Context context, final ArrayAdapter<Student> adapter, final List<Student> students) {
		super(context, "Getting list of students");

		studentListAdapter = adapter;
		studentList = students;

		Resources resources = getContext().getResources();
		tagSuccess = resources.getString(R.string.tag_success);
		tagStudents = resources.getString(R.string.tag_students);
		tagFirstName = resources.getString(R.string.tag_first_name);
		tagLastName = resources.getString(R.string.tag_last_name);
		getStudentsURL = resources.getString(R.string.remote_db_path) +
				resources.getString(R.string.get_all_students);
	}

	@Override
	protected String doInBackground(final String... params) {
		JSONObject json = makeGetRequest(getStudentsURL);
		if (json != null) {
			Log.d(TAG, "All students: " + json.toString());

			try {
				if (json.getInt(tagSuccess) == 1) {
					studentList.clear();

					JSONArray students = json.getJSONArray(tagStudents);
					for (int index = 0; index < students.length(); ++index) {
						JSONObject studentJSON = students.getJSONObject(index);
						Student student = new Student(
								studentJSON.getString(tagFirstName),
								studentJSON.getString(tagLastName));
						studentList.add(student);
					}
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
