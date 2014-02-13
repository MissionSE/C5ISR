package com.missionse.kestrelweather.reports.audio;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.reports.utils.MediaTimeConverter;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;

/**
 * An adapter used to display audio files.
 */
public class AudioAdapter extends ArrayAdapter<String> {
	private static final String TAG = AudioAdapter.class.getSimpleName();
	private final int mResource;

	/**
	 * Constructor.
	 * @param context The current context.
	 * @param resource The resource ID for a layout file containing a TextView to use when instantiating views.
	 * @param songs The objects to represent in the ListView.
	 */
	public AudioAdapter(Context context, final int resource, String[] songs) {
		super(context, resource, songs);
		mResource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(mResource, parent, false);

		if (view == null) {
			throw new NullPointerException("Unable to inflate view.");
		}

		ImageView thumbnail = (ImageView) view.findViewById(R.id.report_item_thumbnail);
		TextView title = (TextView) view.findViewById(R.id.report_item_file_name);
		TextView date = (TextView) view.findViewById(R.id.report_item_file_date);
		TextView duration = (TextView) view.findViewById(R.id.report_item_file_size);

		File song = new File(getItem(position));
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd");
		MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
		try {
			FileInputStream inputStream = new FileInputStream(song);
			metadataRetriever.setDataSource(inputStream.getFD());
			inputStream.close();
			metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
			duration.setText(
					MediaTimeConverter.extractRunTime(
							Long.valueOf(metadataRetriever.extractMetadata(
									MediaMetadataRetriever.METADATA_KEY_DURATION))));
		} catch (Exception exec) {
			Log.e(TAG, "Unable to retrieve metadata.", exec);
		}

		title.setText(song.getName());
		date.setText(dateFormat.format(song.lastModified()));
		thumbnail.setImageResource(R.drawable.ic_action_play);

		return view;
	}

}
