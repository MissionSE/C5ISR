package com.missionse.kestrelweather.reports.photos;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Provides an adapter for a list of photos.
 */
public class PhotoAdapter extends ArrayAdapter<Uri> {
	private static final String TAG = PhotoAdapter.class.getName();
	private static final int BYTE_SIZE = 1024;
	private final int mResource;
	private List<Uri> mSelectedPhotos;

	/**
	 * Constructor.
	 * @param context The current context.
	 * @param resource The resource ID for a layout file containing a TextView to use when instantiating views.
	 */
	public PhotoAdapter(final Context context, final int resource) {
		super(context, resource);
		mResource = resource;
		mSelectedPhotos = new ArrayList<Uri>();
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(mResource, null);
		}

		if (view != null) {
			ImageView thumbnailView = (ImageView) view.findViewById(R.id.photo_overview_item_thumbnail);
			TextView imageFilenameView = (TextView) view.findViewById(R.id.photo_overview_item_file_name);
			TextView imageSizeView = (TextView) view.findViewById(R.id.photo_overview_item_file_size);

			Uri uri = getItem(position);
			Cursor cursor = getContext().getContentResolver()
					.query(uri, null, null, null, null, null);
			try {
				if (cursor != null && cursor.moveToFirst()) {
					if (thumbnailView != null) {
						long id = getUriId(cursor.getString(
								cursor.getColumnIndex(DocumentsContract.Root.COLUMN_DOCUMENT_ID)));
						if (id != -1) {
							Bitmap thumbnail = MediaStore.Images.Thumbnails.getThumbnail(
									getContext().getContentResolver(), id, MediaStore.Images.Thumbnails.MICRO_KIND, null);
							thumbnailView.setImageBitmap(thumbnail);
						} else {
							thumbnailView.setImageResource(R.drawable.ic_launcher);
						}
					}

					if (imageFilenameView != null) {
						String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
						imageFilenameView.setText(displayName);
					}

					if (imageSizeView != null) {
						int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
						if (!cursor.isNull(sizeIndex)) {
							long size = cursor.getLong(sizeIndex);
							imageSizeView.setText(humanReadableByteCount(size));
						} else {
							imageSizeView.setText(getContext().getString(R.string.unknown));
						}
					}
				}
			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}

			if (mSelectedPhotos.contains(uri)) {
				view.setBackgroundColor(getContext().getResources().getColor(R.color.gray_light));
			} else {
				view.setBackgroundColor(getContext().getResources().getColor(R.color.background_holo_light));
			}
		}
		return view;
	}

	/**
	 * Sets the selected status of a URI.
	 * @param uri The uri item.
	 * @param selected Whether the uri is selected.
	 */
	public void setSelected(final Uri uri, final boolean selected) {
		Log.d(TAG, "setSelected: " + selected + " - " + uri.toString());
		if (selected) {
			mSelectedPhotos.add(uri);
		} else {
			mSelectedPhotos.remove(uri);
		}
	}

	/**
	 * Clear all selected items.
	 */
	public void clearSelected() {
		mSelectedPhotos.clear();
	}

	private long getUriId(final String uri) {
		long uriId = -1;
		String[] uriSegments = uri.split(":");
		try {
			uriId = Long.valueOf(uriSegments[uriSegments.length - 1]);
		} catch (NumberFormatException exception) {
			Log.d(TAG, "ID was not valid long: " + uri);
		}

		return uriId;
	}

	private String humanReadableByteCount(long bytes) {
		if (bytes < BYTE_SIZE) {
			return bytes + " B";
		}

		int exp = (int) (Math.log(bytes) / Math.log(BYTE_SIZE));
		String pre = ("KMGTPE").charAt(exp - 1) + "";
		return String.format("%.1f %sB", bytes / Math.pow(BYTE_SIZE, exp), pre);
	}
}
