package com.missionse.kestrelweather.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * A BroadcastReceiver that starts the SyncService on demand.
 */
public class AlarmReceiver extends BroadcastReceiver {
	public static final int REQUEST_CODE = 1;

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent syncServiceStart = new Intent(context, SyncService.class);
		context.startService(syncServiceStart);
	}
}
