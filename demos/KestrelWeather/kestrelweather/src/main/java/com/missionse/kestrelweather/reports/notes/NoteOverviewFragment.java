package com.missionse.kestrelweather.reports.notes;


import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Fragment;
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
import com.missionse.kestrelweather.reports.utils.MediaMultiChoiceModeListener;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link NoteOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteOverviewFragment extends Fragment {
	private static final String TAG = NoteOverviewFragment.class.getSimpleName();
	private static final String EDITABLE_REPORT = "is_report_editable";
	private static final String REPORT_ID = "report_id";
	private static final int INVALID_REPORT_ID = -1;
	private int mReportId = INVALID_REPORT_ID;
	private NoteAdapter mNoteAdapter;
	private Activity mActivity;
	private boolean mEditable = false;


	public NoteOverviewFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param editable Parameter 1.
	 * @return A new instance of fragment NoteOverviewFragment.
	 */
	public static NoteOverviewFragment newInstance(final boolean editable) {
		return newInstance(editable, INVALID_REPORT_ID);
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param editable Parameter 1.
	 * @param reportId Parameter 2.
	 * @return A new instance of fragment NoteOverviewFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static NoteOverviewFragment newInstance(final boolean editable, final int reportId) {
		NoteOverviewFragment fragment = new NoteOverviewFragment();
		Bundle args = new Bundle();
		args.putBoolean(EDITABLE_REPORT, editable);
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mEditable = getArguments().getBoolean(EDITABLE_REPORT);
			mReportId = getArguments().getInt(REPORT_ID);
		}

		if (mEditable) {
			setHasOptionsMenu(true);
		}

		mNoteAdapter = new NoteAdapter(mActivity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
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
	public void onDetach() {
		super.onDetach();
		mActivity = null;
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.report_notes, menu);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (item.getItemId() == R.id.action_add_note) {
			DialogFragment dFragment = NoteDialog.newInstance(true, -1);
			dFragment.show(getFragmentManager(), "note_dialog");
		}
		return super.onOptionsItemSelected(item);
	}



}
