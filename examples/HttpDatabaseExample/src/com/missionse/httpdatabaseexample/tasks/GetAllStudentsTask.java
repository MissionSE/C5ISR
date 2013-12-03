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

/**
 * Provides a task that gets all students in the database.
 */
public class GetAllStudentsTask extends HttpRequestTask {
	private static final String TAG = GetAllStudentsTask.class.getName();

	private final String mTagSuccess;
	private final String mTagStudents;
	private final String mTagFirstName;
	private final String mTagLastName;
	private final String mGetStudentsURL;

	private final ArrayAdapter<Student> mStudentListAdapter;
	private final List<Student> mStudentList;

	/**
	 * Constructor.
	 * @param context The context of the owner of the task.
	 * @param adapter The adapter containing the students.
	 * @param students The list of students.
	 */
	public GetAllStudentsTask(final Context context, final ArrayAdapter<Student> adapter, final List<Student> students) {
		super(context, "Getting list of students");

		mStudentListAdapter = adapter;
		mStudentList = students;

		Resources resources = getContext().getResources();
		mTagSuccess = resources.getString(R.string.tag_success);
		mTagStudents = resources.getString(R.string.tag_students);
		mTagFirstName = resources.getString(R.string.tag_first_name);
		mTagLastName = resources.getString(R.string.tag_last_name);
		mGetStudentsURL = resources.getString(R.string.remote_db_path)
				+ resources.getString(R.string.get_all_students);
	}

	@Override
	protected String doInBackground(final String... params) {
		JSONObject json = makeGetRequest(mGetStudentsURL);
		if (json != null) {
			Log.d(TAG, "All students: " + json.toString());

			try {
				if (json.getInt(mTagSuccess) == 1) {
					mStudentList.clear();

					JSONArray students = json.getJSONArray(mTagStudents);
					for (int index = 0; index < students.length(); ++index) {
						JSONObject studentJSON = students.getJSONObject(index);
						Student student = new Student(
								studentJSON.getString(mTagFirstName),
								studentJSON.getString(mTagLastName));
						mStudentList.add(student);
					}
					((Activity) getContext()).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mStudentListAdapter.notifyDataSetChanged();
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
