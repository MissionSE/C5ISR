package com.missionse.kestrelweather.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.missionse.kestrelweather.KestrelWeatherActivity;
import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.DatabaseManager;
import com.missionse.kestrelweather.database.sync.DatabaseSync;
import com.missionse.kestrelweather.database.sync.SyncStatusListener;

/**
 * Syncs the client-side database with the remote database when invoked.
 */
public class SyncService extends Service implements SyncStatusListener {
	private DatabaseAccessor mDatabaseAccessor;
	private SharedPreferences mSharedPreferences;
	private int mSyncedReportCount;
	private static final int NOTIFICATION_ID = 1;

	/**
	 * Constructor.
	 */
	public SyncService() {
		super();
		mDatabaseAccessor = new DatabaseManager(this);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	}

	@Override
	public IBinder onBind(final Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(final Intent intent, final int flags, final int startId) {
		DatabaseSync databaseSync = new DatabaseSync(mDatabaseAccessor, this);
		databaseSync.setSyncCompleteListener(this);
		databaseSync.execute(true, true, true);

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onSyncComplete() {
		boolean notificationsEnabled = mSharedPreferences.getBoolean(getString(R.string.key_sync_notification), true);
		if (notificationsEnabled && mSyncedReportCount > 0) {
			NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Sync complete.")
				.setContentText(mSyncedReportCount + " report(s) synced.");
			builder.setAutoCancel(true);
			Intent resultIntent = new Intent(this, KestrelWeatherActivity.class);

			// The stack builder object will contain an artificial back stack for the started Activity.
			// This ensures that navigating backward from the Activity leads out of
			// your application to the Home screen.
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
			// Adds the back stack for the Intent (but not the Intent itself)
			stackBuilder.addParentStack(KestrelWeatherActivity.class);
			// Adds the Intent that starts the Activity to the top of the stack
			stackBuilder.addNextIntent(resultIntent);
			PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
			builder.setContentIntent(resultPendingIntent);

			NotificationManager notificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			// NOTIFICATION_ID allows you to update the notification later on.
			notificationManager.notify(NOTIFICATION_ID, builder.build());
		}
	}

	@Override
	public void onSyncStarted() {
		mSyncedReportCount = 0;
	}

	@Override
	public void onSyncedReport(final int reportId) {
		mSyncedReportCount++;
	}
}
