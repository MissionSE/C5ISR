package com.missionse.kestrelweather.reports;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.missionse.kestrelweather.KestrelWeatherActivity;
import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.database.model.tables.manipulators.ReportTable;
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
	private static final int SAVE_TRANSITION_DURATION = 500;

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

						ImageView reportSyncStatus = (ImageView) mView.findViewById(R.id.report_detail_sync_status_icon);
						if (reportSyncStatus != null) {
							if (report.isDirty()) {
								reportSyncStatus.setImageResource(R.drawable.report_status_not_synced);
							} else {
								reportSyncStatus.setImageResource(R.drawable.report_status_synced);
							}
						}

						if (report.isDraft()) {
							View reportDraftButtons = mView.findViewById(R.id.report_detail_draft_buttons);
							if (reportDraftButtons != null) {
								reportDraftButtons.setVisibility(View.VISIBLE);
							}

							Button discardReportButton = (Button) mView.findViewById(R.id.report_detail_discard_btn);
							if (discardReportButton != null) {
								discardReportButton.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(final View view) {
										handleDiscardReportButton();
									}
								});
							}

							Button saveReportButton = (Button) mView.findViewById(R.id.report_detail_save_btn);
							if (saveReportButton != null) {
								saveReportButton.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(final View view) {
										handleSaveReportButton();
									}
								});
							}

							PagerTitleStrip pagerTitleStrip = (PagerTitleStrip) mView.findViewById(R.id.report_detail_pager_title_strip);
							if (pagerTitleStrip != null) {
								pagerTitleStrip.setBackgroundColor(mActivity.getResources().getColor(R.color.holo_green_light));
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

	private void handleDiscardReportButton() {
		if (mActivity != null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
			builder.setMessage(R.string.discard_message)
					.setPositiveButton(R.string.discard, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							discardReport();
						}
					})
					.setNegativeButton(R.string.cancel, null);
			builder.create().show();
		}
	}

	private void discardReport() {
		KestrelWeatherActivity activity = (KestrelWeatherActivity) mActivity;
		if (activity != null) {
			DatabaseAccessor databaseAccessor = activity.getDatabaseAccessor();
			if (databaseAccessor != null) {
				Report report = databaseAccessor.getReportById(mReportId);
				if (report != null) {
					ReportRemover.removeReport(databaseAccessor, report.getId());
					activity.displayHome();
				}
			}
		}
	}

	private void handleSaveReportButton() {
		KestrelWeatherActivity activity = (KestrelWeatherActivity) mActivity;
		if (activity != null) {
			DatabaseAccessor databaseAccessor = activity.getDatabaseAccessor();
			Report report = databaseAccessor.getReportById(mReportId);
			if (report != null) {
				report.setDraft(false);

				ReportTable reportTable = databaseAccessor.getReportTable();
				reportTable.update(report);

				final View reportDraftButtons = mView.findViewById(R.id.report_detail_draft_buttons);
				if (reportDraftButtons != null) {
					Animation draftButtonAnimation = AnimationUtils.loadAnimation(activity,
							R.anim.fragment_report_detail_draft_buttons_transition);
					if (draftButtonAnimation != null) {
						draftButtonAnimation.setAnimationListener(new Animation.AnimationListener() {
							@Override
							public void onAnimationStart(final Animation animation) {
							}

							@Override
							public void onAnimationEnd(final Animation animation) {
								reportDraftButtons.setVisibility(View.GONE);
							}

							@Override
							public void onAnimationRepeat(final Animation animation) {
							}
						});
						reportDraftButtons.startAnimation(draftButtonAnimation);
					}
				}

				PagerTitleStrip pagerTitleStrip = (PagerTitleStrip) mView.findViewById(R.id.report_detail_pager_title_strip);
				if (pagerTitleStrip != null) {
					int paddingLeft = pagerTitleStrip.getPaddingLeft();
					int paddingTop = pagerTitleStrip.getPaddingTop();
					int paddingRight = pagerTitleStrip.getPaddingRight();
					int paddingBottom = pagerTitleStrip.getPaddingBottom();

					pagerTitleStrip.setBackground(activity.getResources().getDrawable(R.drawable.fragment_report_detail_pager_transition));
					pagerTitleStrip.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

					TransitionDrawable pagerTitleStripBackground = (TransitionDrawable) pagerTitleStrip.getBackground();
					if (pagerTitleStripBackground != null) {
						pagerTitleStripBackground.startTransition(SAVE_TRANSITION_DURATION);
					}
				}
			}
		}
	}
}
