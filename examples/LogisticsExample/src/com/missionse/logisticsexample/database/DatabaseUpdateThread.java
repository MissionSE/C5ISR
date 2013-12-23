package com.missionse.logisticsexample.database;

import java.util.List;
import java.util.TimerTask;

/**
 * Task that will run on a periodic and sync the remote database with the local one.
 */
public class DatabaseUpdateThread extends TimerTask implements DatabaseRequestCompletedListener {
	private DatabaseUpdateCompleteListener mUpdateCompleteListener;
	private List<DatabaseRequestor> mRequestors;

	/**
	 * Constructor.
	 * @param updateCompleteListener The listener to call upon completion of a database update.
	 * @param requestors The list of requestors that will perform database updates.
	 */
	public DatabaseUpdateThread(final DatabaseUpdateCompleteListener updateCompleteListener, final List<DatabaseRequestor> requestors) {
		mUpdateCompleteListener = updateCompleteListener;
		mRequestors = requestors;
	}

	@Override
	public void run() {
		for (DatabaseRequestor requestor : mRequestors) {
			requestor.fetchAll(this);
		}
	}

	@Override
	public synchronized void databaseRequestCompleted() {
		boolean requestorsCompleted = true;
		for (DatabaseRequestor requestor : mRequestors) {
			if (!requestor.hasCompleted()) {
				requestorsCompleted = false;
				break;
			}
		}

		if (requestorsCompleted) {
			mUpdateCompleteListener.onDatabaseUpdateComplete();
		}
	}
}
