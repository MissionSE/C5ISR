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

/**
 * Provides a task that creates a student in the database.
 */
public class CreateStudentTask extends HttpRequestTask {
	private static final String TAG = GetAllStudentsTask.class.getName();

	private final String mTagSuccess;
	private final String mTagFirstName;
	private final String mTagLastName;
	private final String mAddStudentsURL;

	private final ArrayAdapter<Student> mStudentListAdapter;
	private final List<Student> mStudentList;

	/**
	 * Constructor.
	 * @param context The context of the owner of the task.
	 * @param adapter The adapter containing the students.
	 * @param students The list of students.
	 */
	public CreateStudentTask(final Context context, final ArrayAdapter<Student> adapter, final List<Student> students) {
		super(context, "Creating student");
		mStudentListAdapter = adapter;
		mStudentList = students;

		Resources resources = getContext().getResources();
		mTagSuccess = resources.getString(R.string.tag_success);
		mTagFirstName = resources.getString(R.string.tag_first_name);
		mTagLastName = resources.getString(R.string.tag_last_name);
		mAddStudentsURL = resources.getString(R.string.remote_db_path)
				+ resources.getString(R.string.create_student);
	}

	@Override
	protected String doInBackground(final String... params) {
		if (params.length != 2) {
			throw new RuntimeException("Invalid number of parameters.");
		}

		Student student = new Student(params[0], params[1]);
		addParameter(mTagFirstName, student.getFirstName());
		addParameter(mTagLastName, student.getLastName());
		JSONObject json = makePostRequest(mAddStudentsURL);

		if (json != null) {
			Log.d(TAG, "Create Student Result: " + json.toString());

			try {
				if (json.getInt(mTagSuccess) == 1) {
					mStudentList.add(student);
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
