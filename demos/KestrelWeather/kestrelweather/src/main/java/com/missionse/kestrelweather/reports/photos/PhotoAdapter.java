package com.missionse.kestrelweather.reports.photos;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.reports.utils.UriAdapter;

/**
 * Provides an adapter for a list of photos.
 */
public class PhotoAdapter extends UriAdapter {
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
			Cursor cursor = getCursor(getItem(position));
			try {
				if (cursor != null && cursor.moveToFirst()) {
					setThumbnail(view, getThumbnail(cursor));
					setFileName(view, getFileName(cursor));
					setFileDateModified(view, getFileDateModified(cursor));
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

		long id = getUriId(cursor);
		if (id != -1) {
			thumbnail = MediaStore.Images.Thumbnails.getThumbnail(
					getContext().getContentResolver(), id, MediaStore.Images.Thumbnails.MICRO_KIND, null);
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
					thumbnailView.setImageResource(R.drawable.ic_action_picture_black);
				}
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

	private void setFileDateModified(final View view, final String date) {
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
}
