package com.missionse.kestrelweather.reports.utils;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;

/**
 * Provides utility functions to download remote files.
 */
public final class FileDownloader {
	private static final String TAG = FileDownloader.class.getSimpleName();

	private FileDownloader() {
	}

	/**
	 * Downloads and scans a remote media file.
	 * @param context The current context.
	 * @param remoteFilePath The path to a remote file.
	 * @param fileDownloadCompleteListener The listener to be notified upon completion.
	 */
	public static void downloadFile(final Context context, final String remoteFilePath,
			final OnFileDownloadCompleteListener fileDownloadCompleteListener) {
		File remoteFile = new File(remoteFilePath);
//		File localFile = new File(Environment.getExternalStorageDirectory(), remoteFile.getName());
//		File localFile = new File(context.getExternalFilesDir(null), remoteFile.getName());
		File localFile = new File(context.getCacheDir(), remoteFile.getName());
		Log.v(TAG, "Saving file to: " + localFile.toString());
		Ion.with(context, remoteFilePath)
				.write(localFile)
				.setCallback(new FutureCallback<File>() {
					@Override
					public void onCompleted(final Exception e, final File result) {
						if (e == null) {
							if (result != null) {
								Log.d(TAG, "File output: " + result.toString());
								MediaScannerConnection.scanFile(context,
										new String[]{result.toString()}, null,
										new MediaScannerConnection.OnScanCompletedListener() {
											public void onScanCompleted(final String path, final Uri uri) {
												if (uri != null) {
													Log.v(TAG, "Scan complete.");
													Log.v(TAG, "path: " + path);
													Log.v(TAG, "uri: " + uri);
													if (fileDownloadCompleteListener != null) {
														fileDownloadCompleteListener.fileDownloadComplete(uri);
													}
												} else {
													Log.v(TAG, "Media Scanner returned null URI.");
												}
											}
										});
							} else {
								Log.v(TAG, "Downloaded file is null.");
							}
						} else {
							Log.v(TAG, "Caught exception while downloading file: " + e.toString());
						}
					}
				});
	}

	/**
	 * Provides an interface for listeners to be notified of completed downloads.
	 */
	public interface OnFileDownloadCompleteListener {
		/**
		 * Notifies a listener of a completed file download.
		 * @param uri The URI to the downloaded file.
		 */
		void fileDownloadComplete(final Uri uri);
	}
}
