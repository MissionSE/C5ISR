package com.missionse.kestrelweather.reports.notes;


import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
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

/**
 * A fragment used to manage the notes attached to a report.
 */
public class NoteOverviewFragment extends Fragment {
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
					//TODO: Implenment onClick for each item in the list
				}
			});

			if (mEditable) {
				mNoteList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
				//mNoteList.setMultiChoiceModeListener(new MediaMultiChoiceModeListener(mActivity, mNoteList, mNoteAdapter));
			}

			TextView emptyView = (TextView) contentView.findViewById(R.id.fragment_report_notes_empty);
			if (emptyView != null) {
				mNoteList.setEmptyView(emptyView);
			}
		}
		return contentView;
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.report_notes, menu);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (item.getItemId() == R.id.action_add_note) {
			FragmentManager fragmentManager = getFragmentManager();
			if (fragmentManager != null) {
				DialogFragment dialogFragment = NoteDialogFragment.newInstance(-1, -1);
				dialogFragment.show(fragmentManager, "note_dialog");
			}
		}
		return super.onOptionsItemSelected(item);
	}
}
