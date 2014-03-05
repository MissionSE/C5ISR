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

import com.missionse.kestrelweather.KestrelWeatherActivity;
import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.reports.auxiliary.AuxiliaryDataFragment;
import com.missionse.kestrelweather.reports.readings.ReadingsFragment;
import com.missionse.kestrelweather.reports.weather.WeatherOverviewFragment;
import com.missionse.kestrelweather.util.ReportRemover;
import com.missionse.uiextensions.viewpager.SectionFragmentPagerAdapter;

import org.joda.time.format.DateTimeFormat;

/**
 * Provides a fragment to show the details of a report.
 */
public class ReportDetailFragment extends Fragment {
	private static final String REPORT_ID = "report_id";
	private static final int INVALID_REPORT_ID = -1;

	private Activity mActivity;
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
		View view = inflater.inflate(R.layout.fragment_report_detail, container, false);
		if (view != null) {
			if (mActivity != null) {
				final DatabaseAccessor databaseAccessor = ((KestrelWeatherActivity) mActivity).getDatabaseAccessor();
				if (databaseAccessor != null) {
					final Report report = databaseAccessor.getReportById(mReportId);
					if (report != null) {
						TextView reportTitle = (TextView) view.findViewById(R.id.report_detail_title);
						if (reportTitle != null) {
							reportTitle.setText(report.getTitle());
						}

						TextView reportTimestamp = (TextView) view.findViewById(R.id.report_detail_timestamp);
						if (reportTimestamp != null) {
							reportTimestamp.setText(DateTimeFormat.forPattern("yyyy-MM-dd [HH:mm:ss]").print(report.getCreatedAt()));
						}

						ImageView reportSyncStatus = (ImageView) view.findViewById(R.id.report_detail_sync_status_icon);
						if (reportSyncStatus != null) {
							if (report.isDirty()) {
								reportSyncStatus.setImageResource(R.drawable.report_status_not_synced);
							} else {
								reportSyncStatus.setImageResource(R.drawable.report_status_synced);
							}
						}

						if (report.isDirty()) {
							View reportSyncButtons = view.findViewById(R.id.report_detail_sync_buttons);
							if (reportSyncButtons != null) {
								reportSyncButtons.setVisibility(View.VISIBLE);
							}

							Button cancelReportButton = (Button) view.findViewById(R.id.report_detail_cancel_btn);
							if (cancelReportButton != null) {
								cancelReportButton.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(final View view) {
										cancelReport();
									}
								});
							}

							Button saveReportButton = (Button) view.findViewById(R.id.report_detail_save_btn);
							if (saveReportButton != null) {
								saveReportButton.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(final View view) {
										saveReport();
									}
								});
							}
						}
					}
				}
			}

			ViewPager viewPager = (ViewPager) view.findViewById(R.id.report_detail_view_pager);
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

		return view;
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

	private void saveReport() {
		KestrelWeatherActivity activity = (KestrelWeatherActivity) mActivity;
		if (activity != null) {
			activity.updateDrawerFooterCountInformation();
			activity.displayHome();
		}
	}
}
