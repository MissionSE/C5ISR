package com.missionse.kestrelweather.reports;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.reports.audio.AudioViewFragment;
import com.missionse.kestrelweather.reports.photos.PhotoOverviewFragment;


/**
 * This fragment will be how the user will create new reports.
 */
public class ReportCreationFragment extends Fragment {
	private static String EDIT_REPORT = "edit_report";
	private static String REPORT_ID = "report_id";
	private static int INVALID_REPORT_ID = -1;

	/*
	 * status fields
	 */
	private TextView mNotesStatus;
	private TextView mPicturesStatus;
	private TextView mAudiosStatus;

	private boolean mModifyReport = false;
	private int mReportId = INVALID_REPORT_ID;

	/**
	 * @param editable - Boolean to determine if the report is editable.
	 * @return A new instance of fragment ReportCreationFragment.
	 * @see com.missionse.kestrelweather.reports.ReportCreationFragment#newInstance(boolean, int)
	 */
	public static ReportCreationFragment newInstance(boolean editable) {
		return newInstance(editable, INVALID_REPORT_ID);
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 * @param editable - Boolean to determine if the report is editable.
	 * @param reportId - The database ID associated with the report.
	 * @return A new instance of fragment ReportCreationFragment.
	 */
	public static ReportCreationFragment newInstance(boolean editable, int reportId) {
		ReportCreationFragment fragment = new ReportCreationFragment();
		Bundle bundle = new Bundle();
		bundle.putBoolean(EDIT_REPORT, editable);
		bundle.putInt(REPORT_ID, reportId);
		fragment.setArguments(bundle);
		return fragment;
	}

	/**
	 * Default constructor.
	 * Required by android library.
	 */
	public ReportCreationFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			processCreateBundle(getArguments());
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View contentView = inflater.inflate(R.layout.fragment_report_creation, container, false);

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
		launchReport(new PhotoOverviewFragment(), "photo_report");
	}

	private void onAudioButtonPressed() {
		launchReport(AudioViewFragment.newInstance(false), "audio_report");
	}

	private void onCancelButtonPressed() {
		getActivity().getFragmentManager().beginTransaction().remove(this).commit();
	}


	private void onOkButtonPressed() {

	}

	private void launchReport(Fragment fragment, String backStackId) {
		getActivity().getFragmentManager().beginTransaction()
		  .replace(R.id.content, fragment, backStackId)
		  .addToBackStack(backStackId)
		  .commit();
	}

	private void processCreateBundle(Bundle bundle) {

	}

}
