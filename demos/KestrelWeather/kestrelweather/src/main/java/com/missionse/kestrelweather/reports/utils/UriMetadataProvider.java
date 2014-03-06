package com.missionse.kestrelweather.reports.utils;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Uses the cursor to determine the metadata for a given Uri.
 */
public class UriMetadataProvider {
	private static final int MILLISECONDS_IN_SECOND = 1000;

	private Cursor mCursor;
	private List<String> mColumnNames;

	/**
	 * Constructor.
	 * @param contentResolver The content resolver to use to get the metadata.
	 * @param uri The uri from which the metadata should be retrieved.
	 */
	public UriMetadataProvider(final ContentResolver contentResolver, final Uri uri) {
		mCursor = contentResolver.query(uri, null, null, null, null, null);
		if (mCursor != null && mCursor.moveToFirst()) {
			mColumnNames = Arrays.asList(mCursor.getColumnNames());
		} else {
			mColumnNames = new ArrayList<String>();
		}
	}

	/**
	 * Gets the list of column names available in the metadata cursor.
	 * @return The list of metadata available.
	 */
	public List<String> getMetadataAvailable() {
		return mColumnNames;
	}

	/**
	 * Close the cursor.
	 */
	public void close() {
		if (mCursor != null) {
			mCursor.close();
		}
	}

	/**
	 * Gets the filename from the metadata of the content.
	 * @return The name of the file.
	 */
	public String getName() {
		String filename = null;
		if (mCursor != null && mCursor.moveToFirst()) {
			filename = mCursor.getString(mCursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
		}
		return filename;
	}

	/**
	 * Gets the date the file was last modified.
	 * @return The date the file was last modified in milliseconds since epoch.
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public long getDateModified() {
		long dateModified = 0;

		if (mCursor != null && mCursor.moveToFirst()) {
			int unitConversion = 1;
			int dateModifiedIndex = mCursor.getColumnIndex(DocumentsContract.Document.COLUMN_LAST_MODIFIED);
			if (dateModifiedIndex == -1) {
				dateModifiedIndex = mCursor.getColumnIndex(MediaStore.MediaColumns.DATE_MODIFIED);
				unitConversion = MILLISECONDS_IN_SECOND;
			}
			if (dateModifiedIndex != -1) {
				dateModified = mCursor.getLong(dateModifiedIndex) * unitConversion;
			}
		}

		return dateModified;
	}

	/**
	 * Gets the size of the file from the metadata of the content.
	 * @return The size of the file in bytes.
	 */
	public long getSize() {
		long size = 0;

		if (mCursor != null && mCursor.moveToFirst()) {
			int sizeIndex = mCursor.getColumnIndexOrThrow(OpenableColumns.SIZE);
			if (!mCursor.isNull(sizeIndex)) {
				size = mCursor.getLong(sizeIndex);
			}
		}

		return size;
	}
}
