package com.missionse.kestrelweather.reports.audio;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.reports.utils.UriAdapter;

/**
 * An adapter used to display audio files.
 */
public class AudioAdapter extends UriAdapter {
	private static final String TAG = AudioAdapter.class.getSimpleName();

	/**
	 * Constructor.
	 * @param context The current context.
	 * @param resource The resource ID for a layout file containing a TextView to use when instantiating views.
	 */
	public AudioAdapter(Context context, final int resource) {
		super(context, resource);
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(getResource(), null);
		}

		if (view != null) {
			Cursor cursor = getCursor(getItem(position));
			try {
				if (cursor != null && cursor.moveToFirst()) {
					setThumbnail(view);
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

	private void setThumbnail(final View view) {
		if (view != null) {
			ImageView thumbnailView = (ImageView) view.findViewById(R.id.report_item_thumbnail);
			if (thumbnailView != null) {
				thumbnailView.setImageResource(R.drawable.ic_action_audio);
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
