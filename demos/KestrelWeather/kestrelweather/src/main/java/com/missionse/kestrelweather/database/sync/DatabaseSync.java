package com.missionse.kestrelweather.database.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Process;

import com.missionse.kestrelweather.database.DatabaseAccessor;

import org.joda.time.DateTime;

/**
 * Class that forces the database to sync with the remote.
 * <p/>
 * 0 Push Report Database
 * 1 Pull Remote Latest
 * 2 Push Media Database
 * 3 Merge Local and Remote Database
 */
public class DatabaseSync extends AsyncTask<Boolean, Void, Void> implements SyncStatusListener {
	private static final int PUSH_REPORT_IDX = 0;
	private static final int PULL_REMOTE_IDX = 1;
	private static final int PUSH_MEDIA_IDX = 2;
	private static final int EXPECTED_ARGUMENTS = 3;
	private DatabaseAccessor mAccessor;
	private Context mContext;
	private SyncStatusListener mListener;

	/**
	 * Constructor.
	 * @param databaseAccessor construct through which we can access the SQLite database
	 * @param context the application context
	 */
	public DatabaseSync(final DatabaseAccessor databaseAccessor, final Context context) {
		mAccessor = databaseAccessor;
		mContext = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mListener.onSyncStarted();
	}

	@Override
	protected Void doInBackground(Boolean... params) {
		if (params.length < EXPECTED_ARGUMENTS) {
			throw new IllegalArgumentException("Missing arguments...");
		}
		if (params[PUSH_REPORT_IDX]) {
			startAndWait(new DatabaseReportPusher(mContext, mAccessor));
		}
		if (params[PULL_REMOTE_IDX]) {
			DatabasePuller puller = new DatabasePuller(mContext, mAccessor);
			puller.setSyncStatusListener(this);
			startAndWait(puller);
		}
		if (params[PUSH_MEDIA_IDX]) {
			DatabaseMediaPusher mediaPusher = new DatabaseMediaPusher(mContext, mAccessor);
			mediaPusher.run();
		}

		mAccessor.setLastSyncedTime(DateTime.now());

		return null;
	}

	private void startAndWait(final Runnable runnable) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Process.setThreadPriority(Process.myTid(), Process.THREAD_PRIORITY_BACKGROUND);
				runnable.run();
			}
		});
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set the sync complete listener for this class.
	 * @param listener Instance of SyncCompleteListener.
	 */
	public void setSyncCompleteListener(SyncStatusListener listener) {
		mListener = listener;
	}

	@Override
	public void onSyncComplete() {
		if (mListener != null) {
			mListener.onSyncComplete();
		}
	}

	@Override
	public void onSyncStarted() {
		if (mListener != null) {
			mListener.onSyncStarted();
		}
	}

	@Override
	public void onSyncedReport(int reportId) {
		if (mListener != null) {
			mListener.onSyncedReport(reportId);
		}
	}
}
