package com.missionse.kestrelweather.reports;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.missionse.kestrelweather.KestrelWeatherActivity;
import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.sync.DatabaseSync;
import com.missionse.kestrelweather.database.sync.SyncStatusListener;
import com.missionse.kestrelweather.reports.filter.SyncStatusFilter;
import com.missionse.kestrelweather.reports.utils.ReportGroup;
import com.missionse.kestrelweather.reports.utils.ReportGroupAdapter;
import com.missionse.kestrelweather.reports.utils.ReportGroupLoaderTask;
import com.missionse.kestrelweather.reports.utils.ReportListLoadedListener;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.Options;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import uk.co.senab.actionbarpulltorefresh.library.viewdelegates.ViewDelegate;

/**
 * Provides a fragment to show a list of reports.
 */
public class ReportDatabaseFragment extends Fragment implements SyncStatusListener, ViewDelegate {
	private static final String TAG = ReportDatabaseFragment.class.getSimpleName();
	private static final float PULL_TO_REFRESH_RATIO = 0.5f;

	private Activity mActivity;
	private DatabaseAccessor mDatabaseAccessor;
	private ReportGroupAdapter mReportGroupAdapter;
	private TextView mReportCountView;
	private ProgressBar mProgressBar;
	private EditText mSearchField;
	private MenuItem mShowSynced;
	private MenuItem mShowUnsynced;
	private ReportGroupLoaderTask mReportGroupLoaderTask;
	private PullToRefreshLayout mPullToRefreshLayout;

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
			mReportGroupAdapter = new ReportGroupAdapter(mActivity, R.layout.fragment_report_detail_header);
			mReportGroupAdapter.addOnFilterRunnable(new Runnable() {
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
		constraint = constraint.trim();
		constraint = constraint.replace(" ", ",");
		mReportGroupAdapter.setSyncStatusConstraint(constraint);
		mReportGroupAdapter.filter();
		return super.onOptionsItemSelected(item);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_report_database, container, false);
		if (contentView != null) {
			mReportCountView = (TextView) contentView.findViewById(R.id.fragment_report_database_count);
			updateReportCount();

			mSearchField = (EditText) contentView.findViewById(R.id.fragment_report_database_search);
			mSearchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

			mSearchField.addTextChangedListener(new TextWatcher() {
				@Override
				public void afterTextChanged(final Editable s) {
				}

				@Override
				public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
				}

				@Override
				public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
					String titleFilter = "";
					if (mSearchField.getText() != null) {
						if (mSearchField.getText().length() > 0) {
							titleFilter = mSearchField.getText().toString().trim();
						}
					}
					mReportGroupAdapter.setReportTitleConstraint(titleFilter);
					mReportGroupAdapter.filter();
				}
			});

			mPullToRefreshLayout =
				(PullToRefreshLayout) contentView.findViewById(R.id.fragment_report_database_pull_to_refresh);
			ActionBarPullToRefresh.from(getActivity())
				.options(Options.create()
					.scrollDistance(PULL_TO_REFRESH_RATIO)
					.build())
				.allChildrenArePullable()
				.listener(new OnRefreshListener() {
					@Override
					public void onRefreshStarted(final View view) {
						try {
							KestrelWeatherActivity activity = (KestrelWeatherActivity) mActivity;
							if (activity != null) {
								DatabaseSync sync = new DatabaseSync(activity.getDatabaseAccessor(), activity);
								sync.setSyncCompleteListener(ReportDatabaseFragment.this);
								sync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, true, true, true);
							}
						} catch (ClassCastException e) {
							Log.e(TAG, "Unable to cast activity.", e);
						}
					}
				})
				.useViewDelegate(StickyListHeadersListView.class, this)
				.setup(mPullToRefreshLayout);

			StickyListHeadersListView reportList = (StickyListHeadersListView) contentView.findViewById(R.id.fragment_report_database_list);
			if (reportList != null) {
				reportList.setAdapter(mReportGroupAdapter);
				reportList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {
						FragmentManager fragmentManager = getFragmentManager();
						if (fragmentManager != null) {
							ReportGroup reportGroup = mReportGroupAdapter.getItem(position);
							if (reportGroup.getCount() > 1) {
								Fragment reportDetailFragment = ReportGroupDetailFragment.newInstance(reportGroup);
								fragmentManager.beginTransaction()
										.setCustomAnimations(
												R.animator.fade_in, R.animator.fade_out,
												R.animator.fade_in, R.animator.fade_out)
										.replace(R.id.content, reportDetailFragment, "report_detail")
										.addToBackStack("report_detail").commit();
							} else {
								Fragment reportDetailFragment = ReportDetailFragment.newInstance(reportGroup.getLatestReport().getId());
								fragmentManager.beginTransaction()
										.setCustomAnimations(
												R.animator.fade_in, R.animator.fade_out,
												R.animator.fade_in, R.animator.fade_out)
										.replace(R.id.content, reportDetailFragment, "report_detail")
										.addToBackStack("report_detail").commit();
							}
						}
						if (mActivity != null) {
							InputMethodManager inputMethodManager = (InputMethodManager) mActivity.
									getSystemService(Context.INPUT_METHOD_SERVICE);
							View currentFocus = mActivity.getCurrentFocus();
							if (currentFocus != null) {
								inputMethodManager.hideSoftInputFromWindow(
										currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
							}
						}
					}
				});

				TextView emptyView = (TextView) contentView.findViewById(R.id.fragment_report_database_list_empty);
				if (emptyView != null) {
					reportList.setEmptyView(emptyView);
				}

				mProgressBar = (SmoothProgressBar) contentView.findViewById(R.id.fragment_report_database_progress_bar);
				updateReportList(true);
			}
		}
		return contentView;
	}

	@Override
	public void onConfigurationChanged(final Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (mReportGroupLoaderTask != null) {
			mReportGroupLoaderTask.cancel(true);
		}
	}

	private void updateReportList(final boolean showProgressBar) {
		ProgressBar progressBar = null;
		if (showProgressBar) {
			progressBar = mProgressBar;
		}
		mReportGroupLoaderTask = new ReportGroupLoaderTask(mDatabaseAccessor, mReportGroupAdapter, progressBar,
				new ReportListLoadedListener() {
					@Override
					public void reportListLoaded() {
						mReportGroupAdapter.filter();
						mPullToRefreshLayout.setRefreshComplete();
					}
				}
		);
		mReportGroupLoaderTask.execute();
	}

	private void updateReportCount() {
		if (mReportCountView != null && mDatabaseAccessor != null) {
			int totalReports = mDatabaseAccessor.getSyncedCount() + mDatabaseAccessor.getUnSynedCount();
			if (mReportGroupAdapter.isReportTitleBeingFiltered() || mReportGroupAdapter.isSyncStatusBeingFiltered()) {
				mReportCountView.setText(mReportGroupAdapter.getCount() + "/" + totalReports);
			} else {
				mReportCountView.setText("" + totalReports);
			}
		}
	}

	@Override
	public void onSyncComplete() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				updateReportList(false);
			}
		});
		clearFilters();
	}

	@Override
	public void onSyncStarted() {
		//Nothing to do.
	}

	private void clearFilters() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mSearchField.setText("");
				mReportGroupAdapter.setReportTitleConstraint("");
				mShowSynced.setChecked(true);
				mShowUnsynced.setChecked(true);
				mReportGroupAdapter.setSyncStatusConstraint(SyncStatusFilter.DEFAULT);
				mReportGroupAdapter.filter();
			}
		});
	}

	@Override
	public void onSyncedReport(int reportId) {
	}

	private void runOnUiThread(final Runnable runnable) {
		if (mActivity != null) {
			mActivity.runOnUiThread(runnable);
		}
	}

	@Override
	public boolean isReadyForPull(final View view, final float v, final float v2) {
		StickyListHeadersListView stickyListHeadersListView = (StickyListHeadersListView) view;
		int firstVisiblePosition = stickyListHeadersListView.getFirstVisiblePosition();
		return (firstVisiblePosition == 0);
	}
}
