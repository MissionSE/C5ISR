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
import com.missionse.httpdatabaseexample.model.Classroom;

/**
 * Provides a task that gets all classrooms in the database.
 */
public class GetAllClassroomsTask extends HttpRequestTask {
	private static final String TAG = GetAllClassroomsTask.class.getName();

	private final String mTagSuccess;
	private final String mTagClassrooms;
	private final String mTagName;
	private final String mGetClassroomsURL;

	private final ArrayAdapter<Classroom> mClassroomListAdapter;
	private final List<Classroom> mClassroomList;

	/**
	 * Constructor.
	 * @param context The context of the owner of the task.
	 * @param adapter The adapter containing the classrooms.
	 * @param classrooms The list of classrooms.
	 */
	public GetAllClassroomsTask(final Context context, final ArrayAdapter<Classroom> adapter, final List<Classroom> classrooms) {
		super(context, "Getting list of classrooms");

		mClassroomListAdapter = adapter;
		mClassroomList = classrooms;

		Resources resources = getContext().getResources();
		mTagSuccess = resources.getString(R.string.tag_success);
		mTagClassrooms = resources.getString(R.string.tag_classrooms);
		mTagName = resources.getString(R.string.tag_name);
		mGetClassroomsURL = resources.getString(R.string.remote_db_path)
				+ resources.getString(R.string.get_all_classrooms);
	}

	@Override
	protected String doInBackground(final String... params) {
		JSONObject json = makeGetRequest(mGetClassroomsURL);
		if (json != null) {
			Log.d(TAG, "All classrooms: " + json.toString());

			try {
				if (json.getInt(mTagSuccess) == 1) {
					mClassroomList.clear();

					JSONArray classrooms = json.getJSONArray(mTagClassrooms);
					for (int index = 0; index < classrooms.length(); ++index) {
						JSONObject classroomJSON = classrooms.getJSONObject(index);
						Classroom classroom = new Classroom(classroomJSON.getString(mTagName));
						mClassroomList.add(classroom);
					}
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
