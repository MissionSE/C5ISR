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

public class CreateStudentTask extends HttpRequestTask {
	private static final String TAG = GetAllStudentsTask.class.getName();

	private final String tagSuccess;
	private final String tagFirstName;
	private final String tagLastName;
	private final String addStudentsURL;

	private final ArrayAdapter<Student> studentListAdapter;
	private final List<Student> studentList;

	public CreateStudentTask(final Context context, final ArrayAdapter<Student> adapter, final List<Student> students) {
		super(context, "Creating student");
		studentListAdapter = adapter;
		studentList = students;

		Resources resources = getContext().getResources();
		tagSuccess = resources.getString(R.string.tag_success);
		tagFirstName = resources.getString(R.string.tag_first_name);
		tagLastName = resources.getString(R.string.tag_last_name);
		addStudentsURL = resources.getString(R.string.remote_db_path) +
				resources.getString(R.string.create_student);
	}

	@Override
	protected String doInBackground(final String... params) {
		if (params.length != 2) {
			throw new RuntimeException("Invalid number of parameters.");
		}

		Student student = new Student(params[0], params[1]);
		addParameter(tagFirstName, student.getFirstName());
		addParameter(tagLastName, student.getLastName());
		JSONObject json = makePostRequest(addStudentsURL);

		if (json != null) {
			Log.d(TAG, "Create Student Result: " + json.toString());

			try {
				if (json.getInt(tagSuccess) == 1) {
					studentList.add(student);
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
