package com.missionse.imageviewer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;

/**
 * Provides a pager adapter used for image uris.
 */
public class UriImagePagerAdapter extends FragmentStatePagerAdapter {
	private static final String TAG = UriImagePagerAdapter.class.getName();

	private ArrayList<Uri> mImageUris;

	/**
	 * Constructor.
	 * @param fragmentManager The fragment manager.
	 */
	public UriImagePagerAdapter(final FragmentManager fragmentManager) {
		super(fragmentManager);
		mImageUris = new ArrayList<Uri>();
	}

	/**
	 * Adds an image uri to the adapter.
	 * @param imageUri The uri of the image to be added.
	 */
	public void addImage(final Uri imageUri) {
		mImageUris.add(imageUri);
	}

	@Override
	public Fragment getItem(final int position) {
		Fragment fragment = null;
		try {
			fragment = ImageFragmentFactory.createImageFragment(mImageUris.get(position));
		} catch (IndexOutOfBoundsException e) {
			Log.e(TAG, "Invalid item: " + position + ", max = " + getCount());
		}

		return fragment;
	}

	@Override
	public int getCount() {
		return mImageUris.size();
	}
}
