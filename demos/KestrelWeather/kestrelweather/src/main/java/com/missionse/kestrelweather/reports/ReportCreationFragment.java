package com.missionse.kestrelweather.reports;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.missionse.kestrelweather.R;


/**
 * This fragment will be how the user will create new reports.
 */
public class ReportCreationFragment extends Fragment {

	/*
	 * status fields
	 */
	private TextView mNotesStatus;
	private TextView mPicturesStatus;
	private TextView mAudiosStatus;

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment ReportCreationFragment.
	 */
	public static ReportCreationFragment newInstance() {
		ReportCreationFragment fragment = new ReportCreationFragment();
		return fragment;
	}

	/**
	 * Default constructor.
	 * Required.
	 */
	public ReportCreationFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View contentView = inflater.inflate(R.layout.create_report_buttons, container, false);

		mNotesStatus = (TextView) contentView.findViewById(R.id.report_notes_status);
		mPicturesStatus = (TextView) contentView.findViewById(R.id.report_pictures_status);
		mAudiosStatus = (TextView) contentView.findViewById(R.id.report_audios_status);

		Button readingsBtn = (Button) contentView.findViewById(R.id.report_get_readings);
		Button pictureBtn = (Button) contentView.findViewById(R.id.report_get_pictures);
		Button audioBtn = (Button) contentView.findViewById(R.id.report_get_audios);
		Button cancelBtn = (Button) contentView.findViewById(R.id.cancel_button);
		Button okBtn = (Button) contentView.findViewById(R.id.ok_button);

		readingsBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onReadingsButtonPressed();
			}
		});

		pictureBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onPictureButtonPressed();
			}
		});

		audioBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onAudioButtonPressed();
			}
		});

		cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onCancelButtonPressed();
			}
		});

		okBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onOkButtonPressed();
			}
		});

		return contentView;
	}

	private void onReadingsButtonPressed() {

	}

	private void onPictureButtonPressed() {

	}

	private void onAudioButtonPressed() {

	}

	private void onCancelButtonPressed() {

	}


	private void onOkButtonPressed() {

	}

}
