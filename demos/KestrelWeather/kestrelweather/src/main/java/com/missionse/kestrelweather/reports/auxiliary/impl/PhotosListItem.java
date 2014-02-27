package com.missionse.kestrelweather.reports.auxiliary.impl;

import android.app.FragmentManager;
import android.content.Context;

import com.missionse.kestrelweather.KestrelWeatherActivity;
import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.reports.auxiliary.AuxiliaryDataListItem;
import com.missionse.kestrelweather.reports.photos.PhotoOverviewFragment;

/**
 * An auxiliary data list item for Photos.
 */
public class PhotosListItem implements AuxiliaryDataListItem {
	private final Context mContext;
	private final FragmentManager mFragmentManager;
	private final DatabaseAccessor mDatabaseAccessor;
	private final int mReportId;

	/**
	 * Constructor.
	 * @param activity Instance of KestrelWeatherActivity.
	 * @param reportId The id of the report.
	 */
	public PhotosListItem(final KestrelWeatherActivity activity, final int reportId) {
		mContext = activity;
		mFragmentManager = activity.getFragmentManager();
		mDatabaseAccessor = activity.getDatabaseAccessor();
		mReportId = reportId;
	}

	@Override
	public int getIcon() {
		return R.drawable.ic_action_picture_black;
	}

	@Override
	public int getCount() {
		return mDatabaseAccessor.getPhotoSupplements(mReportId).size();
	}

	@Override
	public String getType() {
		return mContext.getString(R.string.photos);
	}

	@Override
	public void onClick() {
		mFragmentManager.beginTransaction()
				.setCustomAnimations(
						R.animator.slide_from_right, R.animator.slide_to_left,
						R.animator.slide_from_left, R.animator.slide_to_right)
				.replace(R.id.content, PhotoOverviewFragment.newInstance(mReportId), "photo_overview")
				.addToBackStack("photo_overview")
				.commit();
	}
}
