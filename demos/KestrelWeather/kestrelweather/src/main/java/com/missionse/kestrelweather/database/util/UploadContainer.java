package com.missionse.kestrelweather.database.util;

import android.content.Context;
import android.net.Uri;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.missionse.kestrelweather.database.model.tables.Supplement;

import org.joda.time.DateTime;

import java.io.File;

/**
 * Util class container that hold information about media file to upload.
 */
public final class UploadContainer {
	private Context mContext;
	private Supplement mSupplement;
	private FutureCallback<JsonObject> mCallBack;
	private int mReportId;

	/**
	 * Constructor.
 	 * @param context The current context.
	 * @param supplement Instance of Supplement.
	 * @param callback Instance of FutureCallback<JsonObject>.
	 */
	public UploadContainer(Context context, Supplement supplement, int reportId, FutureCallback<JsonObject> callback) {
		mContext = context;
		mSupplement = supplement;
		mCallBack = callback;
		mReportId = reportId;
	}

	/**
	 * Retrieve the remote id.
	 * @return Int representing the remote database id.
	 */
	public int getRemoteId() {
		return mReportId;
	}

	/**
	 * Retrieve the callback interface.
	 * @return Instance of FutureCallback<JsonObject>.
	 */
	public FutureCallback<JsonObject> getCallback() {
		return mCallBack;
	}

	/**
	 * Retrieve the size of the supplement.
	 * @return Long representing the size of the supplement.
	 */
	public long getSize() {
		return mSupplement.getSize();
	}

	/**
	 * Retrieve the filename of the supplement.
	 * @return String represents the file name.
	 */
	public String getFileName() {
		return mSupplement.getFileName();
	}

	/**
	 * Retrieve the supplement as a file.
	 * @return File represents the supplement.
	 */
	public File getAsFile() {
		Uri mediaUri = Uri.parse(mSupplement.getUri());
		String mediaPath = MediaResolver.getPath(mContext, mediaUri);
		return new File(mediaPath);
	}

	/**
	 * Retrieve the current context.
	 * @return Context that represents the current context.
	 */
	public Context getContext() {
		return mContext;
	}

	/**
	 * Retrieve the date of the supplement.
	 * @return Instance of DateTime that represents the time the supplement was created.
	 */
	public DateTime getDate() {
		return mSupplement.getDate();
	}

	/**
	 * Retrieve the mime type of this supplement.
	 * @return String that represents the mime type.
	 */
	public String getMimeType() {
		return MediaResolver.getMimeType(getAsFile().getAbsolutePath());
	}
}
