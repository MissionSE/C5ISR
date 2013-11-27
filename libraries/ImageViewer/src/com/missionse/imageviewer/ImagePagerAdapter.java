package com.missionse.imageviewer;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * Provides a pager adapter used for image resources.
 */
public class ImagePagerAdapter extends FragmentStatePagerAdapter {

	private static final String TAG = ImagePagerAdapter.class.getName();

	private ArrayList<Integer> mImageResources;

	/**
	 * Constructor.
	 * @param fragmentManager The fragment manager.
	 */
	public ImagePagerAdapter(final FragmentManager fragmentManager) {
		super(fragmentManager);
		mImageResources = new ArrayList<Integer>();
	}

	/**
	 * Adds an image resource to the adapter.
	 * @param resourceId The resource id of the image to be added.
	 */
	public void addImage(final int resourceId) {
		mImageResources.add(resourceId);
	}

	@Override
	public Fragment getItem(final int position) {
		Fragment fragment = null;
		try {
			fragment = ImageFragmentFactory.createImageFragment(mImageResources.get(position));
		} catch (IndexOutOfBoundsException e) {
			Log.e(TAG, "Invalid item: " + position + ", max = " + getCount());
		}

		return fragment;
	}

	@Override
	public int getCount() {
		return mImageResources.size();
	}
}
