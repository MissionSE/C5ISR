package com.missionse.kestrelweather.reports;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.missionse.kestrelweather.KestrelWeatherActivity;
import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.database.sync.DatabaseSync;
import com.missionse.kestrelweather.database.sync.SyncStatusListener;

import java.util.ArrayList;

/**
 * Provides a fragment to show a list of reports.
 */
public class ReportDatabaseFragment extends Fragment implements SyncStatusListener {
	private static final String TAG = ReportDatabaseFragment.class.getSimpleName();
	private Activity mActivity;
	private DatabaseAccessor mDatabaseAccessor;
	private ReportAdapter mReportAdapter;
	private TextView mReportCountView;
	private ProgressBar mProgressBar;
	private MenuItem mShowSynced;
	private MenuItem mShowUnsynced;

	/**
	 * Default constructor.
	 */
	public ReportDatabaseFragment() {
	}

	/**
	 * A factory method used to create a new instance of this fragment.
	 * @return A new instance of the fragment ReportDraftFragment.
	 */
	public static ReportDatabaseFragment newInstance() {
		return new ReportDatabaseFragment();
	}

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
		mDatabaseAccessor = ((KestrelWeatherActivity) mActivity).getDatabaseAccessor();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mActivity = null;
		mDatabaseAccessor = null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (mActivity != null) {
			mReportAdapter = new ReportAdapter(mActivity, R.layout.fragment_report_detail_header,
					new ArrayList<Report>());
			mReportAdapter.addOnFilterRunnable(new Runnable() {
				@Override
				public void run() {
					updateReportCount();
				}
			});
		}

		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.report_database_action_bar, menu);
		mShowSynced = menu.findItem(R.id.action_filter_synced);
		mShowUnsynced = menu.findItem(R.id.action_filter_unsynced);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		item.setChecked(!item.isChecked());
		String constraint = "";
		if (mShowSynced.isChecked()) {
			constraint += "synced ";
		}
		if (mShowUnsynced.isChecked()) {
			constraint += "unsynced";
		}
		constraint = constraint.replace(" ", ",");
		mReportAdapter.setSyncStatusConstraint(constraint);
		mReportAdapter.filter();
		return super.onOptionsItemSelected(item);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_report_database, container, false);
		if (contentView != null) {
			mReportCountView = (TextView) contentView.findViewById(R.id.fragment_report_database_count);
			updateReportCount();

			EditText searchField = (EditText) contentView.findViewById(R.id.fragment_report_database_search);
			searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				@Override
				public boolean onEditorAction(final TextView v, final int actionId, final KeyEvent event) {
					if (actionId == EditorInfo.IME_ACTION_DONE) {
						InputMethodManager inputMethodManager = (InputMethodManager) mActivity.getSystemService(
							Context.INPUT_METHOD_SERVICE);
						inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
						return true;
					}
					return false;
				}
			});

			searchField.addTextChangedListener(new TextWatcher() {
				@Override
				public void afterTextChanged(final Editable s) { }
				@Override
				public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) { }

				@Override
				public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
					mReportAdapter.setReportTitleConstraint(s);
					mReportAdapter.filter();
				}
			});

			ListView reportList = (ListView) contentView.findViewById(R.id.fragment_report_database_list);
			if (reportList != null) {
				reportList.setAdapter(mReportAdapter);
				reportList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {
						FragmentManager fragmentManager = getFragmentManager();
						if (fragmentManager != null) {
							Fragment reportDetailFragment = ReportDetailFragment.newInstance(mReportAdapter.getItem(position).getId());
							fragmentManager.beginTransaction()
									.setCustomAnimations(R.animator.fade_in, R.animator.fade_out,
											R.animator.fade_in, R.animator.fade_out)
									.replace(R.id.content, reportDetailFragment, "report_detail")
									.addToBackStack("report_detail").commit();
						}
					}
				});

				TextView emptyView = (TextView) contentView.findViewById(R.id.fragment_report_database_list_empty);
				if (emptyView != null) {
					reportList.setEmptyView(emptyView);
				}

				mProgressBar = (ProgressBar) contentView.findViewById(R.id.fragment_report_database_progress_bar);

				Button syncButton = (Button) contentView.findViewById(R.id.sync_btn);
				syncButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(final View view) {
						try {
							KestrelWeatherActivity activity = (KestrelWeatherActivity) getActivity();
							if (activity != null) {
								DatabaseSync sync = new DatabaseSync(activity.getDatabaseAccessor(), activity);
								sync.setSyncCompleteListener(ReportDatabaseFragment.this);
								sync.execute(true, true, true);
							}
						} catch (ClassCastException e) {
							Log.e(TAG, "Unable to cast activity.", e);
						}
					}
				});

				updateReportList();
			}
		}

		return contentView;
	}

	private void updateReportList() {
		new ReportLoaderTask(mDatabaseAccessor, mReportAdapter, mProgressBar).execute(false);
	}

	private void updateReportCount() {
		if (mReportCountView != null && mDatabaseAccessor != null) {
			int totalReports = mDatabaseAccessor.getSyncedCount() + mDatabaseAccessor.getUnSynedCount();
			if (mReportAdapter.isReporTitleBeingFiltered() || mReportAdapter.isSyncStatusBeingFiltered()) {
				mReportCountView.setText(mReportAdapter.getCount() + "/" + totalReports);
			} else {
				mReportCountView.setText("" + totalReports);
			}
		}
	}

	@Override
	public void onSyncComplete() {
		runOnUi(new Runnable() {
			@Override
			public void run() {
				updateReportList();
				Toast.makeText(mActivity, getResources().getString(R.string.sync_ended), Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void onSyncStarted() {
		runOnUi(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(mActivity, getResources().getString(R.string.sync_started), Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void onSyncedReport(int reportId) {
	}

	private void runOnUi(Runnable runnable) {
		if (mActivity != null) {
			mActivity.runOnUiThread(runnable);
		}
	}
}
