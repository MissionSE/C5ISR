package com.missionse.kestrelweather.reports.photos;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.model.tables.Supplement;
import com.missionse.kestrelweather.reports.utils.SupplementAdapter;

import org.joda.time.DateTime;

/**
 * Provides an adapter for a list of photos.
 */
public class PhotoAdapter extends SupplementAdapter {
	private static final String TAG = PhotoAdapter.class.getSimpleName();

	/**
	 * Constructor.
	 * @param context The current context.
	 * @param resource The resource ID for a layout file containing a View to use when instantiating views.
	 */
	public PhotoAdapter(final Context context, final int resource) {
		super(context, resource);
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(getResource(), null);
		}

		if (view != null) {
			Supplement supplement = getItem(position);
			setFileName(view, supplement.getFileName());
			setFileSize(view, supplement.getSize());
			setFileDateModified(view, supplement.getDate());

			Bitmap thumbnail = getLocalThumbnail(supplement);
			if (thumbnail != null) {
				setThumbnail(view, thumbnail);
			} else {
				setThumbnail(view, supplement.getThumbnailUri());
			}
		}
		return view;
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	private Bitmap getLocalThumbnail(final Supplement supplement) {
		Bitmap thumbnail = null;

		String localUri = supplement.getUri();
		if (localUri != null && localUri.length() > 0) {
			Cursor cursor = getContext().getContentResolver()
					.query(Uri.parse(localUri), null, null, null, null, null);
			try {
				if (cursor != null && cursor.moveToFirst()) {
					long id = getUriId(cursor);
					if (id != -1) {
						thumbnail = MediaStore.Images.Thumbnails.getThumbnail(
								getContext().getContentResolver(), id,
								MediaStore.Images.Thumbnails.MICRO_KIND, null);
					}
				}
			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}
		}

		return thumbnail;
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	private long getUriId(final Cursor cursor) {
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
					Log.d(TAG, "Unable to get ID from URI: " + uri);
				}
			}
		}

		return uriId;
	}

	private void setThumbnail(final View view, final String thumbnailUri) {
		if (view != null) {
			ImageView thumbnailView = (ImageView) view.findViewById(R.id.report_item_thumbnail);
			if (thumbnailView != null) {
				if (thumbnailUri != null && thumbnailUri.length() > 0) {
					String serverAddress = getContext().getString(R.string.remote_server_development);
					Ion.with(thumbnailView)
							.placeholder(R.drawable.ic_action_picture_black)
							.error(R.drawable.ic_action_picture_black)
							.load(serverAddress + thumbnailUri);
				} else {
					thumbnailView.setImageResource(R.drawable.ic_action_picture_black);
				}
			}
		}
	}

	private void setThumbnail(final View view, final Bitmap thumbnail) {
		if (view != null) {
			ImageView thumbnailView = (ImageView) view.findViewById(R.id.report_item_thumbnail);
			if (thumbnailView != null) {
				thumbnailView.setImageBitmap(thumbnail);
			}
		}
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

	private void setFileSize(final View view, final long size) {
		if (view != null) {
			TextView fileSizeView = (TextView) view.findViewById(R.id.report_item_file_size);
			if (fileSizeView != null) {
				if (size != -1) {
					fileSizeView.setText(getHumanReadableByteCount(size));
				} else {
					fileSizeView.setText(getContext().getString(R.string.unknown));
				}
			}
		}
	}

	private void setFileDateModified(final View view, final DateTime date) {
		if (view != null) {
			TextView fileDateView = (TextView) view.findViewById(R.id.report_item_file_date);
			if (fileDateView != null) {
				if (date != null) {
					fileDateView.setText(getFormattedDate(date));
				} else {
					fileDateView.setText(getContext().getString(R.string.unknown));
				}
			}
		}
	}
}
