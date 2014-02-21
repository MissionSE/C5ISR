package com.missionse.kestrelweather.reports;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.reports.audio.AudioViewFragment;
import com.missionse.kestrelweather.reports.notes.NoteOverviewFragment;
import com.missionse.kestrelweather.reports.photos.PhotoOverviewFragment;


/**
 * This fragment will be how the user will create new reports.
 */
public class ReportCreationFragment extends Fragment {
	private static final String EDITABLE_REPORT = "edit_report";
	private static final String REPORT_ID = "report_id";
	private static final String PHOTO_REPORT = "photo_report";
	private static final String AUDIO_REPORT = "audio_report";
	private static final String NOTE_REPORT = "note_report";
	private static final int INVALID_REPORT_ID = -1;

	private Activity mActivity;
	private boolean mEditable = false;
	private int mReportId = INVALID_REPORT_ID;


	/**
	 * Default constructor.
	 */
	public ReportCreationFragment() {
	}

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
		bundle.putBoolean(EDITABLE_REPORT, editable);
		bundle.putInt(REPORT_ID, reportId);
		fragment.setArguments(bundle);

		return fragment;
	}

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mActivity = null;
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
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_report_creation, container, false);
		if (contentView != null) {
			Button readingsBtn = (Button) contentView.findViewById(R.id.report_get_readings);
			Button pictureBtn = (Button) contentView.findViewById(R.id.report_get_pictures);
			Button audioBtn = (Button) contentView.findViewById(R.id.report_get_audios);
			Button cancelBtn = (Button) contentView.findViewById(R.id.cancel_button);
			Button okBtn = (Button) contentView.findViewById(R.id.ok_button);
			Button noteBtn = (Button) contentView.findViewById(R.id.report_get_notes);

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
			noteBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onNoteButtonPressed();
				}
			});
		}

		return contentView;
	}

	private void onNoteButtonPressed() {
		editReportAttachments(NoteOverviewFragment.newInstance(true), PHOTO_REPORT);

	}

	private void onReadingsButtonPressed() {

	}

	private void onPictureButtonPressed() {
		editReportAttachments(PhotoOverviewFragment.newInstance(true), PHOTO_REPORT);
	}

	private void onAudioButtonPressed() {
		editReportAttachments(AudioViewFragment.newInstance(true), AUDIO_REPORT);
	}

	private void onCancelButtonPressed() {
		if (mActivity != null) {
			mActivity.getFragmentManager().beginTransaction().remove(this).commit();
		}
	}

	private void onOkButtonPressed() {
	}

	private void editReportAttachments(final Fragment fragment, final String backStackId) {
		if (mActivity != null) {
			mActivity.getFragmentManager().beginTransaction()
					.replace(R.id.content, fragment, backStackId)
					.addToBackStack(backStackId)
					.commit();
		}
	}
}
