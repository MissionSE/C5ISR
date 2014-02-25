package com.missionse.kestrelweather.database.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.missionse.kestrelweather.R;

/**
 * Created by rvieras on 2/25/14.
 */
public final class IonUtil {
	private static final String TAG = IonUtil.class.getSimpleName();

	/**
	 * Upload report via ION
	 * @param context The application context.
	 * @param json The json string that represents a report.
	 * @param callback The callback class.
	 */
	public static void upload(Context context, JsonObject json, FutureCallback<JsonObject> callback) {
		Resources res = context.getResources();
		String remoteUrl = String.format(res.getString(R.string.upload_report_url),
			res.getString(R.string.remote_database));
		Log.d(TAG, "Upload Json(\'" + json.toString() + "\') to remote database:" + remoteUrl);

		Ion.with(context, remoteUrl).setJsonObjectBody(json).asJsonObject().setCallback(callback);
	}
}
