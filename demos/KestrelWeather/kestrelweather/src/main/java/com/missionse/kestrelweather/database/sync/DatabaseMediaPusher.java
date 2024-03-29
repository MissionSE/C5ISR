package com.missionse.kestrelweather.database.sync;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.database.model.tables.Supplement;
import com.missionse.kestrelweather.database.model.tables.manipulators.SupplementTable;
import com.missionse.kestrelweather.database.util.IonUtil;
import com.missionse.kestrelweather.database.util.UploadContainer;

/**
 * Runnable to push media files to the database.
 */
public class DatabaseMediaPusher implements Runnable {
	private static final String TAG = DatabaseMediaPusher.class.getSimpleName();
	private DatabaseAccessor mAccessor;
	private Context mContext;

	/**
	 * Constructor.
	 * @param context The current context.
	 * @param accessor Instance of DatabaseAccessor.
	 */
	public DatabaseMediaPusher(Context context, DatabaseAccessor accessor) {
		mAccessor = accessor;
		mContext = context;
	}

	@Override
	public void run() {
		for (Report report : mAccessor.getReportTable().queryForAll()) {
			syncFiltered(report);
		}
	}

	private void syncFiltered(Report report) {
		if (!report.isDirty()) {
			for (Supplement supplement : report.getSupplements()) {
				if (supplement.isDirty()) {
					syncSupplement(supplement, report);
				}
			}
		}
	}

	private void syncSupplement(final Supplement supplement, final Report report) {
		UploadContainer container = new UploadContainer(mContext, supplement, report.getRemoteId(),
				new FutureCallback<JsonObject>() {
					@Override
					public void onCompleted(Exception e, JsonObject result) {
						if (e == null) {
							Log.d(TAG, "Received new media path: " + result.toString());
							flipDirtyFlag(supplement.getId());
						} else {
							Log.d(TAG, "Failed to upload media file...", e);
						}
					}
				}
		);
		IonUtil.uploadMedia(container);
	}

	private void flipDirtyFlag(final int id) {
		SupplementTable table = mAccessor.getSupplementTable();
		Supplement supp = table.queryForId(id);
		if (supp != null) {
			supp.setDirty(false);
			table.update(supp);
		}
	}
}
