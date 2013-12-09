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
 * Provides a task that deletes all students the database.
 */
public class DeleteAllStudentsTask extends HttpRequestTask {
	private static final String TAG = DeleteAllStudentsTask.class.getName();

	private final String mTagSuccess;
	private final String mDeleteStudentsURL;

	private final ArrayAdapter<Student> mStudentListAdapter;
	private final List<Student> mStudentList;

	/**
	 * Constructor.
	 * @param context The context of the owner of the task.
	 * @param adapter The adapter containing the students.
	 * @param students The list of students.
	 */
	public DeleteAllStudentsTask(final Context context, final ArrayAdapter<Student> adapter, final List<Student> students) {
		super(context, "Deleting all students");

		mStudentListAdapter = adapter;
		mStudentList = students;

		Resources resources = getContext().getResources();
		mTagSuccess = resources.getString(R.string.tag_success);
		mDeleteStudentsURL = resources.getString(R.string.remote_db_path)
				+ resources.getString(R.string.delete_all_students);
	}

	@Override
	protected String doInBackground(final String... params) {
		JSONObject json = makeGetRequest(mDeleteStudentsURL);
		if (json != null) {
			Log.d(TAG, "Delete result: " + json.toString());

			try {
				if (json.getInt(mTagSuccess) == 1) {
					mStudentList.clear();
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