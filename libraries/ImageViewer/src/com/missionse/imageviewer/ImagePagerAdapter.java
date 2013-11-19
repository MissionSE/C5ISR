package com.missionse.imageviewer;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.util.Log;

public class ImagePagerAdapter extends FragmentPagerAdapter {

	private static final String TAG = ImagePagerAdapter.class.getName();

	private ArrayList<Integer> imageResources;

	public ImagePagerAdapter(final FragmentManager fm) {
		super(fm);
		imageResources = new ArrayList<Integer>();
	}

	public void addImage(final int resourceId) {
		imageResources.add(resourceId);
	}

	@Override
	public Fragment getItem(final int position) {
		ImageFragment fragment = null;
		try {
			fragment = ImageFragmentFactory.createImageFragment(imageResources.get(position));
		} catch (IndexOutOfBoundsException e) {
			Log.e(TAG, "Invalid item: " + position + ", max = " + getCount() + ".");
		}

		return fragment;
	}

	@Override
	public int getCount() {
		return imageResources.size();
	}

}
