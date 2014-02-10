package com.missionse.kestrelweather.reports.audio;


import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.missionse.kestrelweather.R;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link AudioViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AudioViewFragment extends ListFragment {
	private static final String EDITABLE_REPORT = "is_report_editable";
	private static final String REPORT_ID = "report_id";
	private static final int INVALID_REPORT_ID = -1;

	// TODO: Rename and change types of parameters
	private boolean mEditable;
	private int mReportId = INVALID_REPORT_ID;
	private ImageButton mPlayButton;
	private ImageButton mPauseButton;


	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param editable Determine if the report is editable.
	 * @param reportId The database ID report associated with this report.
	 * @return A new instance of fragment AudioViewFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static AudioViewFragment newInstance(boolean editable, int reportId) {
		AudioViewFragment fragment = new AudioViewFragment();
		Bundle args = new Bundle();
		args.putBoolean(EDITABLE_REPORT, editable);
		args.putInt(REPORT_ID, reportId);
		fragment.setArguments(args);
		return fragment;
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provide parameters.
	 * @param editable Determine if the report is editable.
	 * @return A new instance of fragment AudioViewFragment.
	 */
	public static AudioViewFragment newInstance(boolean editable) {
		return newInstance(editable, INVALID_REPORT_ID);
	}

	/**
	 * Empty default constructor required.
	 */
	public AudioViewFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mEditable = getArguments().getBoolean(EDITABLE_REPORT);
			mReportId = getArguments().getInt(REPORT_ID);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {

		View contentView = inflater.inflate(R.layout.fragment_audio_viewer, container, false);

		initMediaComponents(contentView);


		return contentView;
	}

	private void initMediaComponents(final View root) {

		mPlayButton = (ImageButton) root.findViewById(R.id.audio_control_play_button);
		mPauseButton = (ImageButton) root.findViewById(R.id.audio_control_pause_button);
		ImageButton ffBtn = (ImageButton) root.findViewById(R.id.audio_control_forward_button);
		ImageButton revBtn = (ImageButton) root.findViewById(R.id.audio_control_reverse_button);

		mPlayButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onMediaPlayButtonPressed();
			}
		}
		);
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

	private void onMediaPlayButtonPressed() {
		flipMediaButton();
	}

	private void onMediaPauseButtonPressed() {
		flipMediaButton();
	}

	private void onMediaFastForwardButtonPressed() {

	}

	private void onMediaReverseButtonPressed() {

	}

	private void flipMediaButton() {
		if (mPlayButton.getVisibility() == View.VISIBLE) {
			mPlayButton.setVisibility(View.INVISIBLE);
			mPauseButton.setVisibility(View.VISIBLE);
		} else if(mPauseButton.getVisibility() == View.VISIBLE) {
			mPauseButton.setVisibility(View.INVISIBLE);
			mPlayButton.setVisibility(View.VISIBLE);
		}
	}


}
