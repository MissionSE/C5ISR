package com.missionse.kestrelweather.reports.audio;


import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.reports.utils.FileManager;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Use the {@link AudioViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AudioViewFragment extends ListFragment implements MediaPlayerWrapper.OnMediaPlayerEventListener {
	private static final String EDITABLE_REPORT = "is_report_editable";
	private static final String REPORT_ID = "report_id";
	private static final int INVALID_REPORT_ID = -1;
	private int mReportId = INVALID_REPORT_ID;
	private static final int SKIP_INTERVAL_IN_MILLI = 5000;
	private boolean mEditable;
	private List<View> mSelectedAudioFiles;
	private ImageButton mPlayButton;
	private ImageButton mPauseButton;
	private String[] mSongList;
	private MediaPlayerWrapper mMediaWrapper;

	/**
	 * Empty default constructor required.
	 */
	public AudioViewFragment() {
		mSelectedAudioFiles = new ArrayList<View>();
		mMediaWrapper = new MediaPlayerWrapper();
		mMediaWrapper.setCompleteListener(this);
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provide parameters.
	 *
	 * @param editable Determine if the report is editable.
	 * @return A new instance of fragment AudioViewFragment.
	 */
	public static AudioViewFragment newInstance(boolean editable) {
		return newInstance(editable, INVALID_REPORT_ID);
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param editable Determine if the report is editable.
	 * @param reportId The database ID report associated with this report.
	 * @return A new instance of fragment AudioViewFragment.
	 */
	public static AudioViewFragment newInstance(boolean editable, int reportId) {
		AudioViewFragment fragment = new AudioViewFragment();
		Bundle args = new Bundle();
		args.putBoolean(EDITABLE_REPORT, editable);
		args.putInt(REPORT_ID, reportId);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		if (getArguments() != null) {
			mEditable = getArguments().getBoolean(EDITABLE_REPORT);
			mReportId = getArguments().getInt(REPORT_ID);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (inflater == null) {
			throw new NullPointerException("Unable to inflate view.");
		}

		View contentView = inflater.inflate(R.layout.fragment_audio_viewer, container, false);

		if (contentView != null) {
			initMediaComponents(contentView);
			initLoadMedia();
			setListAdapter(new AudioAdapter(getActivity(), mSongList));
		}
		return contentView;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mMediaWrapper.destroy();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		mMediaWrapper.setMediaSource(mSongList[position]);
		onMediaPlayButtonPressed();
	}

	private void initLoadMedia() {

		File[] audioFiles = FileManager.getTestFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				return filename.endsWith("mp3");
			}
		});

		mSongList = new String[audioFiles.length];
		for (int i = 0; i < audioFiles.length; i++) {
			mSongList[i] = audioFiles[i].getAbsolutePath();
		}

	}

	private void initMediaComponents(final View root) {

		mMediaWrapper.setSeekBar((SeekBar) root.findViewById(R.id.audio_control_seek_bar));
		mMediaWrapper.setDurationTextView((TextView) root.findViewById(R.id.audio_control_end_time_time));
		mMediaWrapper.setCurrentTextView((TextView) root.findViewById(R.id.audio_control_current_time));
		mPlayButton = (ImageButton) root.findViewById(R.id.audio_control_play_button);
		mPauseButton = (ImageButton) root.findViewById(R.id.audio_control_pause_button);
		ImageButton ffBtn = (ImageButton) root.findViewById(R.id.audio_control_forward_button);
		ImageButton revBtn = (ImageButton) root.findViewById(R.id.audio_control_reverse_button);

		mPlayButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onMediaPlayButtonPressed();
			}
		});
		mPauseButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onMediaPauseButtonPressed();
			}
		});
		ffBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onMediaFastForwardButtonPressed();
			}
		});
		revBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onMediaReverseButtonPressed();
			}
		});
	}

	private void showPlayButton() {
		mPauseButton.setVisibility(View.INVISIBLE);
		mPlayButton.setVisibility(View.VISIBLE);
	}

	private void onMediaReverseButtonPressed() {
		mMediaWrapper.rewind(SKIP_INTERVAL_IN_MILLI);
	}

	private void onMediaFastForwardButtonPressed() {
		mMediaWrapper.fastForward(SKIP_INTERVAL_IN_MILLI);
	}

	private void onMediaPauseButtonPressed() {
		if (mPauseButton.getVisibility() == View.VISIBLE) {
			showPlayButton();
			mMediaWrapper.pauseMedia();
		}
	}

	private void onMediaPlayButtonPressed() {
		showPauseButton();
		mMediaWrapper.playMedia();
	}

	private void showPauseButton() {
		mPlayButton.setVisibility(View.INVISIBLE);
		mPauseButton.setVisibility(View.VISIBLE);
	}

	@Override
	public void onMediaComplete() {
		showPlayButton();
	}
}
