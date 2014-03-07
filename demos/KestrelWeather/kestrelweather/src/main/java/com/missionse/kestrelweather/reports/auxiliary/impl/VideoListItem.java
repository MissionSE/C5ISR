package com.missionse.kestrelweather.reports.auxiliary.impl;

import android.app.FragmentManager;
import android.content.Context;

import com.missionse.kestrelweather.KestrelWeatherActivity;
import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.reports.auxiliary.AuxiliaryDataListItem;
import com.missionse.kestrelweather.reports.video.VideoOverviewFragment;

/**
 * An auxiliary data list item for Video.
 */
public class VideoListItem implements AuxiliaryDataListItem {
	private final Context mContext;
	private final FragmentManager mFragmentManager;
	private final DatabaseAccessor mDatabaseAccessor;
	private final int mReportId;

	/**
	 * Constructor.
	 * @param activity Instance of KestrelWeatherActivity.
	 * @param reportId The id of the report.
	 */
	public VideoListItem(final KestrelWeatherActivity activity, final int reportId) {
		mContext = activity;
		mFragmentManager = activity.getFragmentManager();
		mDatabaseAccessor = activity.getDatabaseAccessor();
		mReportId = reportId;
	}

	@Override
	public int getIcon() {
		return R.drawable.ic_action_video;
	}

	@Override
	public int getCount() {
		return mDatabaseAccessor.getVideoSupplements(mReportId).size();
	}

	@Override
	public String getType() {
		return mContext.getString(R.string.video_recording);
	}

	@Override
	public void onClick() {
		mFragmentManager.beginTransaction()
			.setCustomAnimations(
					R.animator.fade_in, R.animator.fade_out,
					R.animator.fade_in, R.animator.fade_out)
			.replace(R.id.content, VideoOverviewFragment.newInstance(mReportId), "video_overview")
			.addToBackStack("video_overview")
			.commit();
	}
}
