package com.missionse.kestrelweather.database.sync;

import android.content.Context;

import com.missionse.kestrelweather.database.local.LocalDatabaseHelper;
import com.missionse.kestrelweather.database.remote.RemoteDatabaseHelper;

/**
 * Created by rvieras on 2/23/14.
 */
public class DatabaseSync implements Runnable {
	private RemoteDatabaseHelper mRemoteHelper = null;
	private LocalDatabaseHelper mLocalHelper = null;
	private Context mContext;

	public DatabaseSync(LocalDatabaseHelper localHelper, RemoteDatabaseHelper remoteHelper) {
		mRemoteHelper = remoteHelper;
		mLocalHelper = localHelper;
	}

	@Override
	public void run() {
		//check to see if valid connection
		//if valid connection get lastest from DB:
			//
	}
}
