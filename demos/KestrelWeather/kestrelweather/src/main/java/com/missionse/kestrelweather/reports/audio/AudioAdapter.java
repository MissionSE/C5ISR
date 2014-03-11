package com.missionse.kestrelweather.reports.audio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.model.tables.Supplement;
import com.missionse.kestrelweather.reports.utils.SupplementAdapter;

import org.joda.time.DateTime;

/**
 * An adapter used to display audio files.
 */
public class AudioAdapter extends SupplementAdapter {
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
			setThumbnail(view);

			Supplement supplement = getItem(position);
			setFileName(view, supplement.getFileName());
			setFileSize(view, supplement.getSize());
			setFileDateModified(view, supplement.getDate());
		}

		return view;
	}

	private void setThumbnail(final View view) {
		if (view != null) {
			ImageView thumbnailView = (ImageView) view.findViewById(R.id.report_item_thumbnail);
			if (thumbnailView != null) {
				thumbnailView.setImageResource(R.drawable.ic_action_audio_black);
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
