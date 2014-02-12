package com.missionse.kestrelweather.reports.audio;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
	private Context mContext;
	private String[] mSongPaths;

	public AudioAdapter(Context context, String[] songs) {
		super(context, R.layout.fragment_audio_viewer_list_entry, songs);
		mContext = context;
		mSongPaths = songs;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.fragment_audio_viewer_list_entry, parent, false);

		if (row == null) {
			throw new NullPointerException("Unable to inflate view.");
		}

		TextView title = (TextView) row.findViewById(R.id.audio_entry_title);
		TextView date = (TextView) row.findViewById(R.id.audio_entry_date);
		TextView duration = (TextView) row.findViewById(R.id.audio_entry_duration);

		File song = new File(mSongPaths[position]);
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		MediaMetadataRetriever mmdr = new MediaMetadataRetriever();
		try {
			FileInputStream inputStream = new FileInputStream(song);
			mmdr.setDataSource(inputStream.getFD());
			inputStream.close();
			mmdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
			duration.setText(
					MediaTimeConverter.extractRunTime(Long.valueOf(mmdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))));
		} catch (Exception exec) {
			Log.e(TAG, "Unable to retrieve metadata.", exec);
		}
		title.setText(song.getName());
		date.setText(sdf.format(song.lastModified()));

		return row;
	}

}
