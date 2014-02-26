package com.missionse.kestrelweather.database.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.missionse.kestrelweather.database.DatabaseAccessor;

/**
 * Created by rvieras on 2/25/14.
 */
public class DatabaseSync extends AsyncTask<Void, Void, Void> {
	private static final String TAG = DatabaseSync.class.getSimpleName();
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
	protected Void doInBackground(Void... params) {
		DatabasePusher pusher = new DatabasePusher(mContext, mAccessor);
		DatabasePuller puller = new DatabasePuller(mContext, mAccessor);

		pusher.run();
		puller.run();
		return null;
	}


}
