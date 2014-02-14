package com.missionse.kestrelweather.reports.photos;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.missionse.kestrelweather.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Provides an adapter for a list of photos.
 */
public class PhotoAdapter extends ArrayAdapter<Uri> {
	private static final String TAG = PhotoAdapter.class.getName();
	private static final int BYTES_IN_KILOBYTE = 1024;
	private static final int MILLISECONDS_IN_SECOND = 1000;
	private final int mResource;

	/**
	 * Constructor.
	 * @param context The current context.
	 * @param resource The resource ID for a layout file containing a TextView to use when instantiating views.
	 */
	public PhotoAdapter(final Context context, final int resource) {
		super(context, resource);
		mResource = resource;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(mResource, null);
		}

		if (view != null) {
			Uri uri = getItem(position);
			Cursor cursor = getContext().getContentResolver()
					.query(uri, null, null, null, null, null);
			try {
				if (cursor != null && cursor.moveToFirst()) {
					setThumbnail(view, getThumbnail(cursor));
					setFileName(view, getFileName(cursor));
					setFileDate(view, getFileDate(cursor));
					setFileSize(view, getFileSize(cursor));
				}
			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}
		}
		return view;
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	private Bitmap getThumbnail(final Cursor cursor) {
		Bitmap thumbnail = null;
		int idIndex = cursor.getColumnIndex(DocumentsContract.Root.COLUMN_DOCUMENT_ID);
		if (idIndex == -1) {
			idIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
		}
		if (idIndex != -1) {
			long id = getUriId(cursor.getString(idIndex));
			if (id != -1) {
				thumbnail = MediaStore.Images.Thumbnails.getThumbnail(
						getContext().getContentResolver(), id, MediaStore.Images.Thumbnails.MICRO_KIND, null);
			}
		}

		return thumbnail;
	}

	private void setThumbnail(final View view, final Bitmap thumbnail) {
		if (view != null) {
			ImageView thumbnailView = (ImageView) view.findViewById(R.id.report_item_thumbnail);
			if (thumbnailView != null) {
				if (thumbnail != null) {
					thumbnailView.setImageBitmap(thumbnail);
				} else {
					thumbnailView.setImageResource(R.drawable.ic_action_picture);
				}
			}
		}
	}

	private String getFileName(final Cursor cursor) {
		return cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
	}

	private void setFileName(final View view, final String fileName) {
		if (view != null) {
			TextView fileNameView = (TextView) view.findViewById(R.id.report_item_file_name);
			if (fileNameView != null) {
				if (fileName != null) {
					fileNameView.setText(fileName);
				} else {
					fileNameView.setText(getContext().getString(R.string.unknown));
				}
			}
		}
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	private String getFileDate(final Cursor cursor) {
		String dateModified = null;
		int unitConversion = 1;
		int dateModifiedIndex = cursor.getColumnIndex(DocumentsContract.Document.COLUMN_LAST_MODIFIED);
		if (dateModifiedIndex == -1) {
			dateModifiedIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED);
			unitConversion = MILLISECONDS_IN_SECOND;
		}
		if (dateModifiedIndex != -1) {
			long dateModifiedInSeconds = cursor.getLong(dateModifiedIndex);
			SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd");
			dateModified = dateFormatter.format(new Date(dateModifiedInSeconds * unitConversion));
		}

		return dateModified;
	}

	private void setFileDate(final View view, final String date) {
		if (view != null) {
			TextView fileDateView = (TextView) view.findViewById(R.id.report_item_file_date);
			if (fileDateView != null) {
				if (date != null) {
					fileDateView.setText(date);
				} else {
					fileDateView.setText(getContext().getString(R.string.unknown));
				}
			}
		}
	}

	private long getFileSize(final Cursor cursor) {
		long size = 0;
		int sizeIndex = cursor.getColumnIndexOrThrow(OpenableColumns.SIZE);
		if (!cursor.isNull(sizeIndex)) {
			size = cursor.getLong(sizeIndex);
		}

		return size;
	}

	private void setFileSize(final View view, final long size) {
		if (view != null) {
			TextView fileSizeView = (TextView) view.findViewById(R.id.report_item_file_size);
			if (fileSizeView != null) {
				if (size != -1) {
					fileSizeView.setText(humanReadableByteCount(size));
				} else {
					fileSizeView.setText(getContext().getString(R.string.unknown));
				}
			}
		}
	}

	private long getUriId(final String uri) {
		long uriId = -1;
		String[] uriSegments = uri.split(":");
		try {
			uriId = Long.parseLong(uriSegments[uriSegments.length - 1]);
		} catch (NumberFormatException exception) {
			Log.v(TAG, "Unable to get ID from URI: " + uri);
		}

		return uriId;
	}

	private String humanReadableByteCount(long bytes) {
		if (bytes < BYTES_IN_KILOBYTE) {
			return bytes + " B";
		}

		int exp = (int) (Math.log(bytes) / Math.log(BYTES_IN_KILOBYTE));
		String pre = ("KMGTPE").charAt(exp - 1) + "";
		return String.format("%.1f %sB", bytes / Math.pow(BYTES_IN_KILOBYTE, exp), pre);
	}
}
