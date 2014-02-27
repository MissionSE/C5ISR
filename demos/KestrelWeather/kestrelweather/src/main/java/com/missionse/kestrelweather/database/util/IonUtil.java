package com.missionse.kestrelweather.database.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.missionse.kestrelweather.R;

import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * Util class for ION calls.
 */
public final class IonUtil {
	private static final String TAG = IonUtil.class.getSimpleName();

	private IonUtil() { }

	/**
	 * Upload report via ION.
	 * @param context The application context.
	 * @param json The json string that represents a report.
	 * @param callback The callback class.
	 */
	public static void upload(Context context, JsonObject json, FutureCallback<JsonObject> callback) {
		Resources res = context.getResources();
		String remoteUrl = String.format(res.getString(R.string.upload_report_url),
				res.getString(R.string.remote_database));
		Log.d(TAG, "Upload Json(\'" + json.toString() + "\') to remote database:" + remoteUrl);

		try {
			Ion.with(context, remoteUrl).setJsonObjectBody(json).asJsonObject().setCallback(callback).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Pull latest event via ION.
	 * @param context The application context.
	 * @param latestId The latest id pulled.
	 * @param callback The callback class.
	 */
	public static void pullLatestEvent(Context context, String latestId, FutureCallback<JsonObject> callback) {
		Resources res = context.getResources();
		String remoteUrl = String.format(
			res.getString(R.string.retrieve_latest_url),
			res.getString(R.string.remote_database));
		Log.d(TAG, "Pulling latest with latestCode:" + latestId + " from URL:" + remoteUrl);

		Ion.with(context, remoteUrl + latestId).asJsonObject().setCallback(callback);
	}

	/**
	 * Pull report via ION.
	 * @param context The application context.
	 * @param reportId The report to pull.
	 * @param callback The callback class.
	 */
	public static void pullReport(Context context, String reportId, FutureCallback<JsonObject> callback) {
		Resources res = context.getResources();
		String remoteUrl = String.format(
		   res.getString(R.string.retrieve_report_url),
		   res.getString(R.string.remote_database));
		Log.d(TAG, "Pulling report with id:" + reportId + " from URL:" + remoteUrl);

		try {
			Ion.with(context, remoteUrl +  reportId).asJsonObject().setCallback(callback).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Upload a single media file to the database.
	 * @param context The application context.
	 * @param reportId The remote database report id to upload.
	 * @param media  Instance of File that points to the media to be uploaded.
	 * @param callback Instance of FutureCallback<JsonObject>.
	 */
	public static void uploadMedia(Context context, int reportId, File media, FutureCallback<JsonObject> callback) {
		Resources res = context.getResources();
		String remoteUrl = String.format(res.getString(R.string.upload_report_url),
		   res.getString(R.string.remote_database));
		Log.d(TAG, "Upload Media(\'" + media.getAbsolutePath() + "\') to reportid=" + reportId + " to remote database:" + remoteUrl);

		Ion.with(context, remoteUrl).setMultipartFile(media.getName(), media).asJsonObject().setCallback(callback);
	}
}
