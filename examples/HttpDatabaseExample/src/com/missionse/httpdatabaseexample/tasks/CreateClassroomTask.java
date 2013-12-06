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

/**
 * Provides a task that creates a classroom in the database.
 */
public class CreateClassroomTask extends HttpRequestTask {
	private static final String TAG = CreateClassroomTask.class.getName();

	private final String mTagSuccess;
	private final String mTagName;
	private final String mAddClassroomsURL;

	private final ArrayAdapter<Classroom> mClassroomListAdapter;
	private final List<Classroom> mClassroomList;

	/**
	 * Constructor.
	 * @param context The context of the owner of the task.
	 * @param adapter The adapter containing the classrooms.
	 * @param classrooms The list of classrooms.
	 */
	public CreateClassroomTask(final Context context, final ArrayAdapter<Classroom> adapter, final List<Classroom> classrooms) {
		super(context, "Creating classroom");
		mClassroomListAdapter = adapter;
		mClassroomList = classrooms;

		Resources resources = getContext().getResources();
		mTagSuccess = resources.getString(R.string.tag_success);
		mTagName = resources.getString(R.string.tag_name);
		mAddClassroomsURL = resources.getString(R.string.remote_db_path)
				+ resources.getString(R.string.create_classroom);
	}

	@Override
	protected String doInBackground(final String... params) {
		if (params.length != 1) {
			throw new RuntimeException("Invalid number of parameters.");
		}

		Classroom classroom = new Classroom(params[0]);
		addParameter(mTagName, classroom.getName());
		JSONObject json = makePostRequest(mAddClassroomsURL);

		if (json != null) {
			Log.d(TAG, "Create Classroom Result: " + json.toString());

			try {
				if (json.getInt(mTagSuccess) == 1) {
					mClassroomList.add(classroom);
					((Activity) getContext()).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mClassroomListAdapter.notifyDataSetChanged();
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
