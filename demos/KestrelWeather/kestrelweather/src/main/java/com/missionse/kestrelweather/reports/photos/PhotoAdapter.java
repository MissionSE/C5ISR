package com.missionse.kestrelweather.reports.photos;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.missionse.kestrelweather.R;

/**
 * Provides an adapter for a list of photos.
 */
public class PhotoAdapter extends ArrayAdapter<Uri> {
	private static final int BYTE_SIZE = 1024;
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
			Bitmap imageThumbnail = null;
			String imageFilename;
			long imageSize = -1;

			Uri uri = getItem(position);
			Cursor cursor = getContext().getContentResolver()
					.query(uri, null, null, null, null, null);
			try {
				if (cursor != null && cursor.moveToFirst()) {
					int idIndex = cursor.getColumnIndex(DocumentsContract.Root.COLUMN_DOCUMENT_ID);
					if (idIndex == -1) {
						idIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
					}
					if (idIndex != -1) {
						long id = getUriId(cursor.getString(idIndex));
						if (id != -1) {
							imageThumbnail = MediaStore.Images.Thumbnails.getThumbnail(
									getContext().getContentResolver(), id, MediaStore.Images.Thumbnails.MICRO_KIND, null);
						}
					}

					imageFilename = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
					int sizeIndex = cursor.getColumnIndexOrThrow(OpenableColumns.SIZE);
					if (!cursor.isNull(sizeIndex)) {
						imageSize = cursor.getLong(sizeIndex);
					}

					setThumbnail(view, imageThumbnail);
					setFilename(view, imageFilename);
					setFilesize(view, imageSize);
				}
			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}
		}
		return view;
	}

	private void setThumbnail(final View view, final Bitmap thumbnail) {
		if (view != null) {
			ImageView thumbnailView = (ImageView) view.findViewById(R.id.photo_overview_item_thumbnail);
			if (thumbnailView != null) {
				if (thumbnail != null) {
					thumbnailView.setImageBitmap(thumbnail);
				} else {
					thumbnailView.setImageResource(R.drawable.ic_launcher);
				}
			}
		}
	}

	private void setFilename(final View view, final String filename) {
		if (view != null) {
			TextView filenameView = (TextView) view.findViewById(R.id.photo_overview_item_file_name);
			if (filenameView != null) {
				if (filename != null) {
					filenameView.setText(filename);
				} else {
					filenameView.setText(getContext().getString(R.string.unknown));
				}
			}
		}
	}

	private void setFilesize(final View view, final long size) {
		if (view != null) {
			TextView filesizeView = (TextView) view.findViewById(R.id.photo_overview_item_file_size);
			if (filesizeView != null) {
				if (size != -1) {
					filesizeView.setText(humanReadableByteCount(size));
				} else {
					filesizeView.setText(getContext().getString(R.string.unknown));
				}
			}
		}
	}

	private long getUriId(final String uri) {
		long uriId = -1;
		String[] uriSegments = uri.split(":");
		try {
			uriId = Long.valueOf(uriSegments[uriSegments.length - 1]);
		} catch (NumberFormatException exception) {
			// Invalid long, nothing to do.
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
