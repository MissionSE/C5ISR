package com.missionse.imageviewer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Provides a fragment with a pager of images.
 */
public class MultiImageFragment extends Fragment {

	private ViewPager mPager;
	private ImagePagerAdapter mAdapter;

	/**
	 * Sets the adapter to be used with the fragment.
	 * @param adapter The pager adapter used to contain the images.
	 */
	public void setAdapter(final ImagePagerAdapter adapter) {
		mAdapter = adapter;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_pager, null);

		mPager = (ViewPager) view.findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		return view;
	}
}
