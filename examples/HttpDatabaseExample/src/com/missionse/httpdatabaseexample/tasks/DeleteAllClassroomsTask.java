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
 * Provides a task that deletes all classrooms from the database.
 */
public class DeleteAllClassroomsTask extends HttpRequestTask {
	private static final String TAG = DeleteAllClassroomsTask.class.getName();

	private final String mTagSuccess;
	private final String mDeleteClassroomsURL;

	private final ArrayAdapter<Classroom> mClassroomListAdapter;
	private final List<Classroom> mClassroomList;

	/**
	 * Constructor.
	 * @param context The context of the owner of the task.
	 * @param adapter The adapter containing the classrooms.
	 * @param classrooms The list of classrooms.
	 */
	public DeleteAllClassroomsTask(final Context context, final ArrayAdapter<Classroom> adapter, final List<Classroom> classrooms) {
		super(context, "Deleting all classrooms");

		mClassroomListAdapter = adapter;
		mClassroomList = classrooms;

		Resources resources = getContext().getResources();
		mTagSuccess = resources.getString(R.string.tag_success);
		mDeleteClassroomsURL = resources.getString(R.string.remote_db_path)
				+ resources.getString(R.string.delete_all_classrooms);
	}

	@Override
	protected String doInBackground(final String... params) {
		JSONObject json = makeGetRequest(mDeleteClassroomsURL);
		if (json != null) {
			Log.d(TAG, "Delete result: " + json.toString());

			try {
				if (json.getInt(mTagSuccess) == 1) {
					mClassroomList.clear();
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
