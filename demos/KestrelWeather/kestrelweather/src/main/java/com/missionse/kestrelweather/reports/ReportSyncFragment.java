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
import android.widget.TextView;
import android.widget.Toast;

import com.missionse.kestrelweather.KestrelWeatherActivity;
import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.database.model.tables.manipulators.ReportTable;
import com.missionse.kestrelweather.database.sync.DatabaseSync;
import com.missionse.kestrelweather.database.sync.SyncStatusListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a fragment to show a list of reports.
 */
public class ReportSyncFragment extends Fragment implements SyncStatusListener {
	private static final String TAG = ReportSyncFragment.class.getSimpleName();
	private Activity mActivity;
	private ReportAdapter mReportAdapter;
	private TextView mUnsyncedCountView;

	/**
	 * Default constructor.
	 */
	public ReportSyncFragment() {
	}

	/**
	 * A factory method used to create a new instance of this fragment.
	 * @return A new instance of the fragment ReportSyncFragment.
	 */
	public static ReportSyncFragment newInstance() {
		return new ReportSyncFragment();
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

		if (mActivity != null) {
			mReportAdapter = new ReportAdapter(mActivity, R.layout.fragment_report_detail_header,
					new ArrayList<Report>());
		}
	}

	private List<Report> queryReports() {
		DatabaseAccessor databaseAccessor = ((KestrelWeatherActivity) mActivity).getDatabaseAccessor();
		ReportTable reportTable = databaseAccessor.getReportTable();
		List<Report> allReports = reportTable.queryForAll();
		List<Report> dirtyReports = new ArrayList<Report>();

		for (Report report : allReports) {
			if (report.isDirty()) {
				dirtyReports.add(report);
			}
		}

		mUnsyncedCountView.setText(getResources().getString(R.string.report_sync_count) + " " + dirtyReports.size());
		if (dirtyReports.isEmpty()) {
			mUnsyncedCountView.setTextColor(getResources().getColor(R.color.gray_medium));
			mUnsyncedCountView.setBackgroundColor(getResources().getColor(R.color.gray_light));
		} else {
			mUnsyncedCountView.setTextColor(getResources().getColor(R.color.white));
			mUnsyncedCountView.setBackgroundColor(getResources().getColor(R.color.holo_green_dark));
		}
		return dirtyReports;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_report_sync, container, false);
		if (contentView != null) {
			mUnsyncedCountView = (TextView) contentView.findViewById(R.id.fragment_report_sync_count);
			ListView reportList = (ListView) contentView.findViewById(R.id.fragment_report_sync_list);
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
											R.animator.slide_from_right, R.animator.slide_to_left,
											R.animator.slide_from_left, R.animator.slide_to_right)
									.replace(R.id.content, reportDetailFragment, "report_detail")
									.addToBackStack("report_detail")
									.commit();
						}
					}
				});

				TextView emptyView = (TextView) contentView.findViewById(R.id.fragment_report_sync_list_empty);
				if (emptyView != null) {
					reportList.setEmptyView(emptyView);
				}

				Button syncButton = (Button) contentView.findViewById(R.id.sync_btn);
				syncButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(final View view) {
						try {
							KestrelWeatherActivity activity = (KestrelWeatherActivity) getActivity();
							if (activity != null) {
								DatabaseSync sync = new DatabaseSync(activity.getDatabaseAccessor(), activity);
								sync.setSyncCompleteListener(ReportSyncFragment.this);
								sync.execute(true, true, true);
							}
						} catch (ClassCastException e) {
							Log.e(TAG, "Unable to cast activity.", e);
						}
					}
				});

				mReportAdapter.clear();
				mReportAdapter.addAll(queryReports());
				mReportAdapter.notifyDataSetChanged();
			}
		}

		return contentView;
	}

	@Override
	public void onSyncComplete() {
		mReportAdapter.clear();
		mReportAdapter.addAll(queryReports());
		mReportAdapter.notifyDataSetChanged();
		Toast.makeText(mActivity, getResources().getString(R.string.sync_ended), Toast.LENGTH_SHORT).show();

		if (getActivity() != null) {
			((KestrelWeatherActivity) getActivity()).updateDrawerFooterTimeInformation();
			((KestrelWeatherActivity) getActivity()).updateDrawerFooterCountInformation();
		}
	}

	@Override
	public void onSyncStarted() {
		Toast.makeText(mActivity, getResources().getString(R.string.sync_started), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onSyncedReport(int reportId) {

	}
}
