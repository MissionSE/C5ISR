package com.missionse.kestrelweather.reports;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_report_database, container, false);
		if (contentView != null) {
			mReportCountView = (TextView) contentView.findViewById(R.id.fragment_report_database_count);
			updateReportCount();

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
									.setCustomAnimations(
											R.animator.fade_in, R.animator.fade_out,
											R.animator.fade_in, R.animator.fade_out)
									.replace(R.id.content, reportDetailFragment, "report_detail")
									.addToBackStack("report_detail")
									.commit();
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
			mReportCountView.setText("Reports: " + totalReports);
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
