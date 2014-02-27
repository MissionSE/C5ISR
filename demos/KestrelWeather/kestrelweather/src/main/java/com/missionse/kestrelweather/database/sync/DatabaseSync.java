package com.missionse.kestrelweather.database.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.missionse.kestrelweather.database.DatabaseAccessor;

/**
 * Class that forces the database to sync with the remote.
 *
 * 0 Push Report Database
 * 1 Pull Remote Latest
 * 2 Push Media Database
 * 3 Merge Local and Remote Database
 */
public class DatabaseSync extends AsyncTask<Boolean, Void, Void> {
	private static final int PUSH_REPORT_IDX = 0;
	private static final int PULL_REMOTE_IDX = 1;
	private static final int PUSH_MEDIA_IDX = 2;
	private DatabaseAccessor mAccessor;
	private Context mContext;

	/**
	 * Constructor.
	 * @param context The application context.
	 * @param accessor The database accessor.
	 */
	public DatabaseSync(Context context, DatabaseAccessor accessor) {
		mAccessor = accessor;
		mContext = context;
	}

	@Override
	protected Void doInBackground(Boolean... params) {
		if (params.length < 3) {
			throw new IllegalArgumentException("Missing arguments...");
		}

		if (params[PUSH_REPORT_IDX]) {
			DatabaseReportPusher pusher = new DatabaseReportPusher(mContext, mAccessor);
			pusher.run();
		}
		if (params[PULL_REMOTE_IDX]) {
			DatabasePuller puller = new DatabasePuller(mContext, mAccessor);
			puller.run();
		}
		if (params[PUSH_MEDIA_IDX]) {
			DatabaseMediaPusher mediaPusher = new DatabaseMediaPusher(mContext, mAccessor);
			mediaPusher.run();
		}
		return null;
	}
}
