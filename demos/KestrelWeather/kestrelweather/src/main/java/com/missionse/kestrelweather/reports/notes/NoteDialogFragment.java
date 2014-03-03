package com.missionse.kestrelweather.reports.notes;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.missionse.kestrelweather.KestrelWeatherActivity;
import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.model.tables.Note;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.database.model.tables.manipulators.NoteTable;
import com.missionse.kestrelweather.database.model.tables.manipulators.ReportTable;

/**
 * Provides a dialog fragment used to view and edit a note.
 */
public class NoteDialogFragment extends DialogFragment {
	private static final String TAG = NoteDialogFragment.class.getSimpleName();
	private static final String NOTE_ID = "note_id";
	private static final String REPORT_ID = "report_id";
	private static final int INVALID_NOTE_ID = -1;
	private static final int INVALID_REPORT_ID = -1;

	private Activity mActivity;
	private boolean mEditableNote = true;
	private int mNoteId = INVALID_NOTE_ID;
	private int mReportId = INVALID_REPORT_ID;

	private Note mNote;
	private Report mReport;
	private EditText mTitleField;
	private EditText mContentField;

	/**
	 * Constructor.
	 */
	public NoteDialogFragment() {
	}

	/**
	 * A factory method used to create a new instance of the fragment with the provided parameters.
	 * @param reportId The database report id that is associated with the report.
	 * @param noteId The database note id that is associated with the note.
	 * @return A new instance of a NoteDialogFragment.
	 */
	public static NoteDialogFragment newInstance(final int noteId, final int reportId) {
		NoteDialogFragment fragment = new NoteDialogFragment();

		Bundle args = new Bundle();
		args.putInt(NOTE_ID, noteId);
		args.putInt(REPORT_ID, reportId);
		fragment.setArguments(args);

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
			mNote = getNoteFromId(getArguments().getInt(NOTE_ID));
			mReport = getReportFromId(getArguments().getInt(REPORT_ID));
		}
	}

	private void setDialogTitle() {
		Dialog dialog = getDialog();
		if (dialog != null) {
			if (mEditableNote) {
				if (mNoteId == INVALID_NOTE_ID) {
					dialog.setTitle("Create Note");
				} else {
					dialog.setTitle("Modify Note");
				}
			} else {
				dialog.setTitle("View Note");
			}
		}
	}

	private Report getReportFromId(final int id) {
		mReportId = id;
		if (id == INVALID_NOTE_ID) {
			return null;
		} else {
			return getReportTable().queryForId(id);
		}
	}

	private Note getNoteFromId(final int id) {
		mNoteId = id;
		if (id == INVALID_NOTE_ID) {
			return new Note();
		} else {
			return getNoteTable().queryForId(id);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View contentView = inflater.inflate(R.layout.fragment_report_notes_editor, container, false);
		if (contentView != null) {
			setupTitle(contentView);
			setupContent(contentView);
			setupButtons(contentView);
			setDialogTitle();
		}
		return contentView;
	}

	private void setupTitle(View root) {
		mTitleField = (EditText) root.findViewById(R.id.note_editor_title);
		if (mTitleField != null) {
			if (!mEditableNote) {
				mTitleField.setEnabled(false);
			}
			if (mNote.getTitle().length() > 0) {
				mTitleField.setText(mNote.getTitle());
			}
		}
	}

	private void setupContent(View root) {
		mContentField = (EditText) root.findViewById(R.id.note_editor_content);
		if (mContentField != null) {
			if (!mEditableNote) {
				mContentField.setEnabled(false);
			}
			if (mNote.getContent().length() > 0) {
				mContentField.setText(mNote.getContent());
			}
		}
	}

	private void setupButtons(View root) {
		Button okButton = (Button) root.findViewById(R.id.ok_button);
		Button cancelButton = (Button) root.findViewById(R.id.cancel_button);

		if (okButton != null) {
			okButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onOkButtonPressed();
				}
			});
		}
		if (cancelButton != null) {
			cancelButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onCancelButtonPressed();
				}
			});
		}
	}

	private String getTitle() {
		String title = null;
		if (mTitleField != null && mTitleField.getText() != null) {
			title = mTitleField.getText().toString();
		}

		return title;
	}

	private String getContent() {
		String content = null;
		if (mContentField != null && mContentField.getText() != null) {
			content = mContentField.getText().toString();
		}

		return content;
	}

	private boolean fieldsValid() {
		boolean fieldsValid = false;
		String title = getTitle();
		String content = getContent();
		if (title != null && content != null) {
			if (title.trim().length() > 0 && content.trim().length() > 0) {
				fieldsValid = true;
			}
		}

		return fieldsValid;
	}

	private void onOkButtonPressed() {
		if (!mEditableNote) {
			onCancelButtonPressed();
		} else {
			if (fieldsValid()) {
				Log.d(TAG, "Saving note to reportId=" + mReportId);
				String title = getTitle();
				if (title != null) {
					mNote.setTitle(title.trim());
				}

				String content = getContent();
				if (content != null) {
					mNote.setContent(content.trim());
				}
				NoteTable noteTable = getNoteTable();
				if (mNoteId == INVALID_NOTE_ID) {
					mNote.setReport(mReport);
					noteTable.create(mNote);
				} else {
					noteTable.update(mNote);
				}
				if (getTargetFragment() != null) {
					getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
				}
				dismiss();
			} else {
				if (mActivity != null) {
					Toast.makeText(mActivity, "Invalid input. Fields cannot be blank.", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	private void onCancelButtonPressed() {
		dismiss();
	}


	private NoteTable getNoteTable() {
		return getRealActivity().getDatabaseAccessor().getNoteTable();
	}

	private ReportTable getReportTable() {
		return getRealActivity().getDatabaseAccessor().getReportTable();
	}

	private KestrelWeatherActivity getRealActivity() {
		try {
			return (KestrelWeatherActivity) getActivity();
		} catch (ClassCastException e) {
			Log.e(TAG, "Cannot cast activity.", e);
			return null;
		}
	}
}
