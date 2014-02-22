package com.missionse.kestrelweather.reports.notes;

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
 * Created by rvieras on 2/20/14.
 */
public class NoteDialog extends DialogFragment {
	private static final String TAG = NoteDialog.class.getSimpleName();
	private static final String EDITABLE_NOTE = "is_note_editable";
	private static final String NOTE_ID = "note_id";
	private static final String REPORT_ID = "report_id";
	private static final int INVALID_DB_ID = -1;

	private boolean mEditableNote;
	private int mNoteId;
	private int mReportId;
	private Note mNote;
	private Report mReport;
	private EditText mTitleField;
	private EditText mContentField;

	/**
	 * Default constructor needed by API.
	 */
	public NoteDialog() {
	}

	/**
	 * Use the parameters to determain who you would like the note to be.
	 * @param editable Makes the note editable or note.
	 * @param noteId The ID associated with the note
	 * @param reportId The ID associated with the report.
	 * @return Instance of NoteDialog.
	 */
	public static NoteDialog newInstance(final boolean editable, final int noteId, final int reportId) {
		NoteDialog fragment = new NoteDialog();
		Bundle args = new Bundle();
		args.putBoolean(EDITABLE_NOTE, editable);
		args.putInt(NOTE_ID, noteId);
		args.putInt(REPORT_ID, reportId);
		fragment.setArguments(args);
		return fragment;
	}

	/**
	 * Creates a NoteDialog that assumes the note is new and editable.
	 * @param reportId The ID to associate the report too.
	 * @return Instance of NoteDialog.
	 */
	public static NoteDialog newInsance(final int reportId) {
		return newInstance(true, INVALID_DB_ID, reportId);
	}

	/**
	 * Creates a NOteDialog that assumes the note already has a report associated with it.
	 * Use the given parameters to make that Note editable.
	 * @param editable Makes the note editable.
	 * @param mNoteId The ID of the note.
	 * @return Instance of NoteDialog.
	 */
	public static NoteDialog newInstance(final boolean editable, final int mNoteId) {
		return newInstance(editable, mNoteId, INVALID_DB_ID);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mEditableNote = getArguments().getBoolean(EDITABLE_NOTE);
			mNote = getNoteFromId(getArguments().getInt(NOTE_ID));
			mReport = getReportFromId(getArguments().getInt(REPORT_ID));
			//setDialogTitle();
		}
	}

	private void setDialogTitle() {
		if (mEditableNote) {
			if (mNoteId == INVALID_DB_ID) {
				getDialog().setTitle("Create Note");
			} else {
				getDialog().setTitle("Modify Note");
			}
		} else {
			getDialog().setTitle("View Note");
		}
	}

	private Report getReportFromId(final int id) {
		mReportId = id;
		if (id == INVALID_DB_ID) {
			return null;
		} else {
			return getReportTable().queryForId(id);
		}
	}

	private Note getNoteFromId(final int id) {
		mNoteId = id;
		if (id == INVALID_DB_ID) {
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

	private boolean validate() {
		return mTitleField.getText().toString().trim().length() > 0 &&
				mContentField.getText().toString().trim().length() > 0;
	}

	private void onOkButtonPressed() {
		if (!mEditableNote) {
			onCancelButtonPressed();
		} else {
			if (validate()) {
				mNote.setTitle(mTitleField.getText().toString().trim());
				mNote.setContent(mContentField.getText().toString().trim());
				if (mNoteId == INVALID_DB_ID) {
					//TODO: Create new note.
				} else {
					//TODO: Update existing note.
				}
			} else {
				Toast.makeText(getActivity(), "Invalid input. Fields cannot be blank.", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void onCancelButtonPressed() {
		dismiss();
	}

	private NoteTable getNoteTable() {
		return getRealActivity().getLocalDatabaseHelper().getNoteTable();
	}

	private ReportTable getReportTable() {
		return getRealActivity().getLocalDatabaseHelper().getReportTable();
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
