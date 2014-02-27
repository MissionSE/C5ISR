package com.missionse.kestrelweather.database.sync;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.database.model.tables.serialization.ReportSerialization;
import com.missionse.kestrelweather.database.util.IonUtil;

import java.util.List;

/**
 * Runnable to push dirty reports to the database.
 */
public class DatabaseReportPusher implements Runnable {
	private static final String TAG = DatabaseReportPusher.class.getSimpleName();
	private DatabaseAccessor mAccessor = null;
	private Context mContext;

	/**
	 * Constructor.
	 * @param context The application context.
	 * @param accessor Instance of DatabaseAccessor.
	 */
	public DatabaseReportPusher(Context context, DatabaseAccessor accessor) {
		mContext = context;
		mAccessor = accessor;
	}

	@Override
	public void run() {
		List<Report> reports = mAccessor.getReportTable().queryForAll();
		for (Report report : reports) {
			if (report.isDirty()) {
				uploadReport(report);
			}
		}
	}

	private void uploadReport(final Report report) {
		final GsonBuilder gsonBuilder = new GsonBuilder();
		final Gson gson = gsonBuilder.registerTypeAdapter(Report.class, new ReportSerialization(mAccessor))
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
					mAccessor.getReportTable().update(report);
				} else {
					Log.e(TAG, "Unable to upload report.", e);
				}
			}
		});
	}
}
