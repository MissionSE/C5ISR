package com.missionse.logisticsexample.database;

import java.util.List;
import java.util.TimerTask;

/**
 * Task that will run on a periodic and sync the remote database with the local one.
 */
public class DatabaseUpdateThread extends TimerTask implements DatabaseRequestCompletedListener {
	private DatabaseUpdateCompleteListener mUpdateCompleteListener;
	private List<RemoteDatabaseRequestor> mRequestors;
    private boolean mRun = false;

	/**
	 * Constructor.
	 * @param updateCompleteListener The listener to call upon completion of a database update.
	 * @param requestors The list of requestors that will perform database updates.
	 */
	public DatabaseUpdateThread(final DatabaseUpdateCompleteListener updateCompleteListener,
			final List<RemoteDatabaseRequestor> requestors) {
		mUpdateCompleteListener = updateCompleteListener;
		mRequestors = requestors;
        mRun = true;
	}

	@Override
	public void run() {
        while(mRun) {
            for (RemoteDatabaseRequestor requestor : mRequestors) {
                requestor.fetchAll(this);
            }
        }
	}

	@Override
	public synchronized void databaseRequestCompleted() {
		boolean requestorsCompleted = true;
		for (RemoteDatabaseRequestor requestor : mRequestors) {
			if (!requestor.hasCompleted()) {
				requestorsCompleted = false;
				break;
			}
		}

		if (requestorsCompleted) {
			mUpdateCompleteListener.onDatabaseUpdateComplete();
		}
	}

    /**
     * Pause the the update thread from running.
     */
    public void pause() {
        mRun = false;
    }

    /**
     * Continue running the update thread.
     */
    public void resume() {
        mRun = true;
    }
}
