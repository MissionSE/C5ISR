package com.missionse.kestrelweather.reports.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ArrayAdapter;

import com.missionse.kestrelweather.database.model.tables.Supplement;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Provides a base adapter to that contains Uris.
 */
public abstract class SupplementAdapter extends ArrayAdapter<Supplement> {
	private static final int BYTES_IN_KILOBYTE = 1024;
	private final int mResource;

	/**
	 * Constructor.
	 * @param context The current context.
	 * @param resource The resource ID for a layout file containing a View to use when instantiating views.
	 */
	public SupplementAdapter(final Context context, final int resource) {
		super(context, resource);
		mResource = resource;
	}

	/**
	 * Determine if uri is currently in the supplement list.
	 * @param uri The uri to check.
	 * @return Whether a supplement with the matching Uri exists in the list.
	 */
	public boolean contains(final Uri uri) {
		boolean found = false;
		if (uri != null && uri.getPath() != null) {
			for (int index = 0; index < getCount(); index++) {
				String supplementUri = getItem(index).getUri();
				if (supplementUri != null) {
					if (uri.getPath().equals(supplementUri)) {
						found = true;
						break;
					}
				}
			}
		}
		return found;
	}

	protected int getResource() {
		return mResource;
	}

	protected String getFormattedDate(final DateTime date) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MMM dd");
		return date.toString(dateTimeFormatter);
	}

	protected String getHumanReadableByteCount(final long bytes) {
		if (bytes < BYTES_IN_KILOBYTE) {
			return bytes + " B";
		}

		int exp = (int) (Math.log(bytes) / Math.log(BYTES_IN_KILOBYTE));
		String pre = ("KMGTPE").charAt(exp - 1) + "";
		return String.format("%.1f %sB", bytes / Math.pow(BYTES_IN_KILOBYTE, exp), pre);
	}
}
