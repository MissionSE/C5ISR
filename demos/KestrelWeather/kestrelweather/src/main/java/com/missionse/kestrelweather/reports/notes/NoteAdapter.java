package com.missionse.kestrelweather.reports.notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.model.tables.Note;

/**
 * ArrayAdapter for listing Notes for a report.
 */
public class NoteAdapter extends ArrayAdapter<Note> {
	private static final int BYTES_IN_KILOBYTE = 1024;
	private static int RESOURCE_LAYOUT_ID = R.layout.fragment_report_item_list_entry;


	public NoteAdapter(Context context) {
		super(context, RESOURCE_LAYOUT_ID);
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(RESOURCE_LAYOUT_ID, null);
		}

		if (view != null) {
			Note note = getItem(position);
			if (note != null) {
				setThumbnail(view);
				setFileName(view, note.getTitle());
				setFileDateModified(view, note.getUpdateAt().toString());
				setFileSize(view, note.getContent().length() * 8);
			}
		}
		return view;
	}

	private void setThumbnail(final View view) {
		if (view != null) {
			ImageView thumbnailView = (ImageView) view.findViewById(R.id.report_item_thumbnail);
			if (thumbnailView != null) {
				thumbnailView.setImageResource(R.drawable.ic_action_note);
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

	protected String getHumanReadableByteCount(final long bytes) {
		if (bytes < BYTES_IN_KILOBYTE) {
			return bytes + " B";
		}

		int exp = (int) (Math.log(bytes) / Math.log(BYTES_IN_KILOBYTE));
		String pre = ("KMGTPE").charAt(exp - 1) + "";
		return String.format("%.1f %sB", bytes / Math.pow(BYTES_IN_KILOBYTE, exp), pre);
	}
}
