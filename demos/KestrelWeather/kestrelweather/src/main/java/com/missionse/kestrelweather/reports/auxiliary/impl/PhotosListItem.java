package com.missionse.kestrelweather.reports.auxiliary.impl;

import android.app.FragmentManager;
import android.content.Context;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.reports.auxiliary.AuxiliaryDataListItem;
import com.missionse.kestrelweather.reports.photos.PhotoOverviewFragment;

/**
 * An auxiliary data list item for Photos.
 */
public class PhotosListItem implements AuxiliaryDataListItem {
	private final Context mContext;
	private final FragmentManager mFragmentManager;
	private final int mReportId;

	/**
	 * Constructor.
	 * @param context The current Context.
	 * @param fragmentManager The fragment manager.
	 * @param reportId The id of the report.
	 */
	public PhotosListItem(final Context context, final FragmentManager fragmentManager, final int reportId) {
		mContext = context;
		mFragmentManager = fragmentManager;
		mReportId = reportId;
	}

	@Override
	public int getIcon() {
		return R.drawable.ic_action_picture_black;
	}

	@Override
	public int getCount() {
		return 0;
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
