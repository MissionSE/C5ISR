package com.missionse.kestrelweather.database.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.missionse.kestrelweather.KestrelWeatherActivity;
import com.missionse.kestrelweather.database.DatabaseAccessor;

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
	private DatabaseAccessor mAccessor;
	private Context mContext;
	private SyncStatusListener mListener;

	/**
	 * Constructor.
	 * @param activity Instance of KestrelWeatherActivity.
	 */
	public DatabaseSync(KestrelWeatherActivity activity) {
		mAccessor = activity.getDatabaseAccessor();
		mContext = activity;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mListener.onSyncStarted();
	}

	@Override
	protected Void doInBackground(Boolean... params) {
		if (params.length < 3) {
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
		return null;
	}

	private void startAndWait(Runnable runnable) {
		Thread thread = new Thread(runnable);
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
	}

	@Override
	public void onSyncedReport(int reportId) {
		if (mListener != null) {
			mListener.onSyncedReport(reportId);
		}
	}
}
