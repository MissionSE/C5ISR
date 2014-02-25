package com.missionse.kestrelweather.reports.notes;


import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.model.tables.Note;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.database.model.tables.manipulators.ReportTable;

/**
 * A fragment used to manage the notes attached to a report.
 */
public class NoteOverviewFragment extends Fragment {
	private static final String TAG = NoteOverviewFragment.class.getSimpleName();
	private static final int REQUEST_LIST_RELOAD = 1;
	private static final String REPORT_ID = "report_id";
	private static final int INVALID_REPORT_ID = -1;

	private NoteAdapter mNoteAdapter;
	private Activity mActivity;
	private boolean mEditable = true;
	private int mReportId = INVALID_REPORT_ID;

	/**
	 * Constructor.
	 */
	public NoteOverviewFragment() {
	}

	/**
	 * A factory method used to create a new instance of the fragment with the provided parameters.
	 * @param reportId The database report id that is associated with the report (if one exists).
	 * @return A new instance of a PhotoOverviewFragment.
	 */
	public static NoteOverviewFragment newInstance(final int reportId) {
		NoteOverviewFragment fragment = new NoteOverviewFragment();

		Bundle args = new Bundle();
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
			mReportId = getArguments().getInt(REPORT_ID);
		}

		if (mEditable) {
			setHasOptionsMenu(true);
		}

		mNoteAdapter = new NoteAdapter(mActivity);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_report_notes, container, false);
		if (contentView != null) {
			ListView mNoteList = (ListView) contentView.findViewById(R.id.fragment_report_notes_list);
			mNoteList.setAdapter(mNoteAdapter);
			mNoteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {
					Note note = mNoteAdapter.getItem(position);
					showNoteDialog(note.getId());
				}
			});

			if (mEditable) {
				mNoteList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
			}

			TextView emptyView = (TextView) contentView.findViewById(R.id.fragment_report_notes_empty);
			if (emptyView != null) {
				mNoteList.setEmptyView(emptyView);
			}

			populateAdapter();
		}
		return contentView;
	}

	private void populateAdapter() {
		ReportTable reportTable = ((DatabaseAccessor) getActivity()).getReportTable();
		Report report = reportTable.queryForId(mReportId);
		if (report != null) {
			if (mNoteAdapter.isEmpty()) {
				mNoteAdapter.addAll(report.getNotes());
			} else {
				mNoteAdapter.clear();
				;
				mNoteAdapter.addAll(report.getNotes());
			}
		} else {
			Log.e(TAG, "Unable to populate adapter report for id=" + mReportId + " is null.");
		}
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.report_notes, menu);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (item.getItemId() == R.id.action_add_note) {
			showNoteDialog(INVALID_REPORT_ID);
		}
		return super.onOptionsItemSelected(item);
	}

	private void showNoteDialog(int noteId) {
		FragmentManager fragmentManager = getFragmentManager();
		if (fragmentManager != null) {
			DialogFragment dialogFragment = NoteDialogFragment.newInstance(noteId, mReportId);
			dialogFragment.setTargetFragment(NoteOverviewFragment.this, REQUEST_LIST_RELOAD);
			dialogFragment.show(fragmentManager, "note_dialog");
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_LIST_RELOAD) {
			if (resultCode == Activity.RESULT_OK) {
				populateAdapter();
			}
		}
	}
}
