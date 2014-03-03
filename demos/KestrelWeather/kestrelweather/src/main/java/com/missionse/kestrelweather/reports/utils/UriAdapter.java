package com.missionse.kestrelweather.reports.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Provides a base adapter to that contains Uris.
 */
public abstract class UriAdapter extends ArrayAdapter<Uri> {
	private static final String TAG = UriAdapter.class.getCanonicalName();
	private static final int BYTES_IN_KILOBYTE = 1024;
	private static final int MILLISECONDS_IN_SECOND = 1000;
	private final int mResource;

	/**
	 * Constructor.
	 * @param context The current context.
	 * @param resource The resource ID for a layout file containing a View to use when instantiating views.
	 */
	public UriAdapter(final Context context, final int resource) {
		super(context, resource);
		mResource = resource;
	}

	protected int getResource() {
		return mResource;
	}

	protected Cursor getCursor(final Uri uri) {
		return getContext().getContentResolver()
				.query(uri, null, null, null, null, null);
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	protected long getUriId(final Cursor cursor) {
		long uriId = -1;

		int idIndex = cursor.getColumnIndex(DocumentsContract.Root.COLUMN_DOCUMENT_ID);
		if (idIndex == -1) {
			idIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
		}
		if (idIndex != -1) {
			String uri = cursor.getString(idIndex);
			if (uri != null) {
				String[] uriSegments = uri.split(":");
				try {
					uriId = Long.parseLong(uriSegments[uriSegments.length - 1]);
				} catch (NumberFormatException exception) {
					Log.v(TAG, "Unable to get ID from URI: " + uri);
				}
			}
		}

		return uriId;
	}

	protected String getFileName(final Cursor cursor) {
		return cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	protected String getFileDateModified(final Cursor cursor) {
		String dateModified = null;
		int unitConversion = 1;
		int dateModifiedIndex = cursor.getColumnIndex(DocumentsContract.Document.COLUMN_LAST_MODIFIED);
		if (dateModifiedIndex == -1) {
			dateModifiedIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DATE_MODIFIED);
			unitConversion = MILLISECONDS_IN_SECOND;
		}
		if (dateModifiedIndex != -1) {
			long dateModifiedInSeconds = cursor.getLong(dateModifiedIndex);
			SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd");
			dateModified = dateFormatter.format(new Date(dateModifiedInSeconds * unitConversion));
		}

		return dateModified;
	}

	protected long getFileSize(final Cursor cursor) {
		long size = 0;
		int sizeIndex = cursor.getColumnIndexOrThrow(OpenableColumns.SIZE);
		if (!cursor.isNull(sizeIndex)) {
			size = cursor.getLong(sizeIndex);
		}

		return size;
	}

	protected String getHumanReadableByteCount(final long bytes) {
		if (bytes < BYTES_IN_KILOBYTE) {
			return bytes + " B";
		}

		int exp = (int) (Math.log(bytes) / Math.log(BYTES_IN_KILOBYTE));
		String pre = ("KMGTPE").charAt(exp - 1) + "";
		return String.format("%.1f %sB", bytes / Math.pow(BYTES_IN_KILOBYTE, exp), pre);
	}

	/**
	 * Determine if uri is currently in the list.
	 * @param uri The uri to check.
	 * @return true if value exists in the list.
	 */
	public boolean contains(Uri uri) {
		if (uri != null && uri.getPath() != null) {
			for (int idx = 0; idx < getCount(); idx++) {
				if (uri.getPath().equals(getItem(idx).getPath())) {
					return true;
				}
			}
		}
		return false;
	}
}
