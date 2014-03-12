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

	private IonUtil() {
	}

	/**
	 * Upload report via ION.
	 * @param context The current context.
	 * @param json The json string that represents a report.
	 * @param callback The callback class.
	 */
	public static void upload(final Context context, final JsonObject json, final FutureCallback<JsonObject> callback) {
		Resources res = context.getResources();
		String remoteUrl = String.format(res.getString(R.string.upload_report_url),
				res.getString(R.string.remote_database));
		Log.d(TAG, "Upload Json(\'" + json.toString() + "\') to remote database: " + remoteUrl);

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
	 * @param context The current context.
	 * @param latestId The latest id pulled.
	 * @param callback The callback class.
	 */
	public static void pullLatestEvent(final Context context, final String latestId, final FutureCallback<JsonObject> callback) {
		Resources res = context.getResources();
		String remoteUrl = String.format(
				res.getString(R.string.retrieve_latest_url),
				res.getString(R.string.remote_database));
		Log.d(TAG, "Pulling latest with latestCode: " + latestId + " from URL: " + remoteUrl);

		//try {
			Ion.with(context, remoteUrl + latestId).asJsonObject().setCallback(callback);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			e.printStackTrace();
//		}
	}

	/**
	 * Pull latest event via ION.
	 * @param context The current context.
	 * @param latestId The latest id pulled.
	 * @return JsonObject that represents the response.
	 */
	public static JsonObject pullLatestEvent(final Context context, final String latestId) {
		Resources res = context.getResources();
		String remoteUrl = String.format(
				res.getString(R.string.retrieve_latest_url),
				res.getString(R.string.remote_database));
		Log.d(TAG, "Pulling latest with latestCode: " + latestId + " from URL: " + remoteUrl);

		try {
			return Ion.with(context, remoteUrl + latestId).asJsonObject().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Pull report via ION.
	 * @param context The current context.
	 * @param reportId The report to pull.
	 * @param callback The callback class.
	 */
	public static void pullReport(final Context context, final String reportId, final FutureCallback<JsonObject> callback) {
		Resources res = context.getResources();
		String remoteUrl = String.format(
				res.getString(R.string.retrieve_report_url),
				res.getString(R.string.remote_database));
		Log.d(TAG, "Pulling report with id: " + reportId + " from URL: " + remoteUrl);

		//try {
			Ion.with(context, remoteUrl + reportId).asJsonObject().setCallback(callback);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			e.printStackTrace();
//		}
	}

	/**
	 * Pull report via ION.
	 * @param context The current context.
	 * @param reportId The report to pull.
	 * @return  JsonObject that represents the response.
	 */
	public static JsonObject pullReport(final Context context, final String reportId) {
		Resources res = context.getResources();
		String remoteUrl = String.format(
				res.getString(R.string.retrieve_report_url),
				res.getString(R.string.remote_database));
		Log.d(TAG, "Pulling report with id: " + reportId + " from URL: " + remoteUrl);

		try {
			return Ion.with(context, remoteUrl + reportId).asJsonObject().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Upload a single media file to the database.
	 * @param context The current context.
	 * @param reportId The remote database report id to upload.
	 * @param media Instance of File that points to the media to be uploaded.
	 * @param callback Instance of FutureCallback<JsonObject>.
	 */
	public static void uploadMedia(final Context context, final int reportId, final File media, final FutureCallback<JsonObject> callback) {
		Resources res = context.getResources();
		String remoteUrl = String.format(res.getString(R.string.upload_report_url),
				res.getString(R.string.remote_database));
		Log.d(TAG, "Upload Media(\'" + media.getAbsolutePath() + "\') to reportid=" + reportId + " to remote database: " + remoteUrl);

		try {
			Ion.with(context, remoteUrl)
					.setMultipartParameter("id", Integer.toString(reportId))
					.setMultipartFile(media.getName(),
							MediaResolver.getMimeType(media.getAbsolutePath()), media)
					.asJsonObject()
					.setCallback(callback)
					.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Upload a single media file to the database.
	 * @param container Instance of UploadContainer.
	 */
	public static void uploadMedia(final UploadContainer container) {
		Resources res = container.getContext().getResources();
		String remoteUrl = String.format(res.getString(R.string.upload_report_url),
				res.getString(R.string.remote_database));
		Log.d(TAG, "Upload Media(\'" + container.getAsFile().getAbsolutePath() + "\') to reportid=" + container.getRemoteId() + " to remote database: " + remoteUrl);

		try {
			Ion.with(container.getContext(), remoteUrl)
					.setMultipartParameter("filename", container.getFileName())
					.setMultipartParameter("size", Long.toString(container.getSize()))
					.setMultipartParameter("date", Long.toString(container.getDate().getMillis()))
					.setMultipartParameter("id", Integer.toString(container.getRemoteId()))
					.setMultipartFile(container.getFileName(), container.getMimeType(), container.getAsFile())
					.asJsonObject()
					.setCallback(container.getCallback())
					.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

}
