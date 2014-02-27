package com.missionse.kestrelweather.database.sync;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.local.LocalDatabaseHelper;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.database.model.tables.serialization.ReportSerialization;
import com.missionse.kestrelweather.database.remote.RemoteDatabaseHelper;
import com.missionse.kestrelweather.database.util.IonUtil;

import java.util.List;

/**
 * Runnable to push dirty reports to the database.
 */
public class DatabasePusher implements Runnable {
	private static final String TAG = DatabasePusher.class.getSimpleName();
	private RemoteDatabaseHelper mRemoteHelper = null;
	private LocalDatabaseHelper mLocalHelper = null;
	private Context mContext;

	public DatabasePusher(Context context, DatabaseAccessor accessor) {
		mContext = context;
		mRemoteHelper = accessor.getRemoteDatabaseHelper();
		mLocalHelper = accessor.getLocalDatabaseHelper();
	}

	@Override
	public void run() {
		List<Report> reports = mLocalHelper.getReportTable().queryForAll();
		for (Report report : reports) {
			if (report.isDirty()) {
				uploadReport(report);
			}
		}
	}

	private void uploadReport(final Report report) {
		final GsonBuilder gsonBuilder = new GsonBuilder();
		final Gson gson = gsonBuilder.registerTypeAdapter(Report.class, new ReportSerialization(mLocalHelper))
				.setPrettyPrinting()
				.create();
		JsonParser jsonParser = new JsonParser();
		JsonObject json = (JsonObject) jsonParser.parse(gson.toJson(report));
		IonUtil.upload(mContext, json, new FutureCallback<JsonObject>() {
			@Override
			public void onCompleted(Exception e, JsonObject result) {
				if (e == null) {
					int id = result.get("id").getAsInt();
					report.setRemoteId(id);
					report.setDirty(false);
					mLocalHelper.getReportTable().update(report);
				} else {
					Log.e(TAG, "Unable to upload report.", e);
				}
			}
		});
	}
}