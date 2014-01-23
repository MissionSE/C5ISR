package com.missionse.logisticsexample.database.table;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.missionse.logisticsexample.R;
import com.missionse.logisticsexample.database.DatabaseRequestCompletedListener;
import com.missionse.logisticsexample.database.RemoteDatabaseRequestor;
import com.missionse.logisticsexample.database.LocalDatabaseAccessor;
import com.missionse.logisticsexample.database.RemoteDatabaseAccessor;
import com.missionse.logisticsexample.model.StatusName;
import com.missionse.logisticsexample.model.orm.StatusNameResponse;

/**
 * Provides an implementation of a database requestor for the StatusNames table in the database.
 */
public class StatusNameDatabaseRequestor implements RemoteDatabaseRequestor {
	private Context mContext;
	private RemoteDatabaseAccessor mRemoteDatabaseAccessor;
	private LocalDatabaseAccessor mLocalDatabaseAccessor;

	private boolean mUpdateComplete;
	private String mUrl;
	private String mName;

	/**
	 * Constructor.
	 * @param context The context of the activity that owns this requestor.
	 * @param remoteDatabaseAccessor A utility class that is used to make remote database calls.
	 * @param localDatabaseAccessor A utility class that is used to make local database calls.
	 */
	public StatusNameDatabaseRequestor(final Context context, final RemoteDatabaseAccessor remoteDatabaseAccessor,
			final LocalDatabaseAccessor localDatabaseAccessor) {
		mContext = context;
		mRemoteDatabaseAccessor = remoteDatabaseAccessor;
		mLocalDatabaseAccessor = localDatabaseAccessor;

		mUrl = mContext.getString(R.string.get_all_status_names);
		mName = mContext.getString(R.string.status_names);
	}

	@Override
	public void fetchAll(final DatabaseRequestCompletedListener requestCompleteListener) {
		mUpdateComplete = false;
		mRemoteDatabaseAccessor.postRequest(mUrl, new TypeToken<StatusNameResponse>() { },
				new FutureCallback<StatusNameResponse>() {
					@Override
					public void onCompleted(final Exception exception, final StatusNameResponse response) {
						if (mRemoteDatabaseAccessor.verifyException(exception, mName)) {
							if (mRemoteDatabaseAccessor.verifyResponse(response, mName)) {
								List<StatusName> statusNames = response.getStatusNames();
								for (StatusName statusName : statusNames) {
									try {
										if (mLocalDatabaseAccessor.getObjectDao(StatusName.class).idExists(statusName.getId())) {
											mLocalDatabaseAccessor.getObjectDao(StatusName.class).update(statusName);
										} else {
											mLocalDatabaseAccessor.getObjectDao(StatusName.class).create(statusName);
										}
									} catch (SQLException mysqlException) {
										mysqlException.printStackTrace();
									}
								}
							}
						}

						mUpdateComplete = true;
						requestCompleteListener.databaseRequestCompleted();
					}
				});
	}

	@Override
	public boolean hasCompleted() {
		return mUpdateComplete;
	}
}
