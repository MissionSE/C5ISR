package com.missionse.kestrelweather.reports;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.missionse.kestrelweather.KestrelWeatherActivity;
import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.database.sync.DatabaseSync;
import com.missionse.kestrelweather.database.sync.SyncStatusListener;
import com.missionse.kestrelweather.reports.auxiliary.AuxiliaryDataFragment;
import com.missionse.kestrelweather.reports.readings.ReadingsFragment;
import com.missionse.kestrelweather.reports.weather.WeatherOverviewFragment;
import com.missionse.kestrelweather.util.ReportRemover;
import com.missionse.uiextensions.viewpager.SectionFragmentPagerAdapter;

import org.joda.time.format.DateTimeFormat;

/**
 * Provides a fragment to show the details of a report.
 */
public class ReportDetailFragment extends Fragment implements SyncStatusListener {
	private static final String REPORT_ID = "report_id";
	private static final int INVALID_REPORT_ID = -1;

	private Activity mActivity;
	private View mView;
	private int mReportId = INVALID_REPORT_ID;

	/**
	 * Default constructor.
	 */
	public ReportDetailFragment() {
	}

	/**
	 * A factory method used to create a new instance of this fragment.
	 * @param reportId The database ID associated with the report.
	 * @return A new instance of the fragment ReportDetailFragment.
	 */
	public static ReportDetailFragment newInstance(final int reportId) {
		ReportDetailFragment fragment = new ReportDetailFragment();

		Bundle bundle = new Bundle();
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
			mReportId = getArguments().getInt(REPORT_ID);
		}
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_report_detail, container, false);
		if (mView != null) {
			if (mActivity != null) {
				final DatabaseAccessor databaseAccessor = ((KestrelWeatherActivity) mActivity).getDatabaseAccessor();
				if (databaseAccessor != null) {
					final Report report = databaseAccessor.getReportById(mReportId);
					if (report != null) {
						TextView reportTitle = (TextView) mView.findViewById(R.id.report_detail_title);
						if (reportTitle != null) {
							reportTitle.setText(report.getTitle());
						}

						TextView reportTimestamp = (TextView) mView.findViewById(R.id.report_detail_timestamp);
						if (reportTimestamp != null) {
							reportTimestamp.setText(DateTimeFormat.forPattern("yyyy-MM-dd [HH:mm:ss]").print(report.getCreatedAt()));
						}

						setSyncStatusIcon(report);

						if (report.isDirty()) {
							View reportSyncButtons = mView.findViewById(R.id.report_detail_sync_buttons);
							if (reportSyncButtons != null) {
								reportSyncButtons.setVisibility(View.VISIBLE);
							}

							Button cancelReportButton = (Button) mView.findViewById(R.id.report_detail_cancel_btn);
							if (cancelReportButton != null) {
								cancelReportButton.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(final View view) {
										cancelReport();
									}
								});
							}

							Button saveReportButton = (Button) mView.findViewById(R.id.report_detail_sync_btn);
							if (saveReportButton != null) {
								saveReportButton.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(final View view) {
										syncReport();
									}
								});
							}
						}
					}
				}
			}

			ViewPager viewPager = (ViewPager) mView.findViewById(R.id.report_detail_view_pager);
			if (viewPager != null) {
				FragmentManager fragmentManager = getChildFragmentManager();
				if (fragmentManager != null) {
					SectionFragmentPagerAdapter pagerAdapter = new SectionFragmentPagerAdapter(fragmentManager);
					pagerAdapter.setPage(0, getString(R.string.weather), WeatherOverviewFragment.newInstance(mReportId));
					pagerAdapter.setPage(1, getString(R.string.kestrel_readings), ReadingsFragment.newInstance(mReportId));
					pagerAdapter.setPage(2, getString(R.string.auxiliary_data), AuxiliaryDataFragment.newInstance(mReportId));

					viewPager.setAdapter(pagerAdapter);
				}
			}
		}

		return mView;
	}

	private void cancelReport() {
		KestrelWeatherActivity activity = (KestrelWeatherActivity) mActivity;
		if (activity != null) {
			DatabaseAccessor databaseAccessor = activity.getDatabaseAccessor();
			if (databaseAccessor != null) {
				Report report = databaseAccessor.getReportById(mReportId);
				if (report != null) {
					if (report.getRemoteId() == 0) {
						ReportRemover.removeReport(databaseAccessor, report.getId());
					}

					activity.updateDrawerFooterCountInformation();
					activity.displayHome();
				}
			}
		}
	}

	private void syncReport() {
		KestrelWeatherActivity activity = (KestrelWeatherActivity) mActivity;
		if (activity != null) {
			DatabaseSync databaseSync = new DatabaseSync(activity);
			databaseSync.setSyncCompleteListener(this);
			databaseSync.execute(true, true, true);
		}
	}

	@Override
	public void onSyncComplete() {
		Toast.makeText(mActivity, getString(R.string.sync_ended), Toast.LENGTH_SHORT).show();

		KestrelWeatherActivity activity = (KestrelWeatherActivity) mActivity;
		if (activity != null) {
			activity.updateDrawerFooterTimeInformation();
			activity.updateDrawerFooterCountInformation();

			DatabaseAccessor databaseAccessor = activity.getDatabaseAccessor();
			if (databaseAccessor != null) {
				Report report = databaseAccessor.getReportById(mReportId);
				if (report != null) {
					setSyncStatusIcon(report);
				}
			}

			View reportSyncButtons = mView.findViewById(R.id.report_detail_sync_buttons);
			if (reportSyncButtons != null) {
				reportSyncButtons.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onSyncStarted() {
		Toast.makeText(mActivity, getString(R.string.sync_started), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onSyncedReport(final int reportId) {
	}

	private void setSyncStatusIcon(final Report report) {
		ImageView reportSyncStatus = (ImageView) mView.findViewById(R.id.report_detail_sync_status_icon);
		if (reportSyncStatus != null) {
			if (report.isDirty()) {
				reportSyncStatus.setImageResource(R.drawable.report_status_not_synced);
			} else {
				reportSyncStatus.setImageResource(R.drawable.report_status_synced);
			}
		}
	}
}
