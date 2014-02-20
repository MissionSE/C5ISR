package com.missionse.imageviewer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Provides a fragment with a pager of images.
 */
public class MultiImageFragment extends Fragment {
	private FragmentStatePagerAdapter mAdapter;

	public static MultiImageFragment newInstance(final FragmentStatePagerAdapter adapter) {
		MultiImageFragment multiImageFragment = new MultiImageFragment();

		if (adapter != null) {
			multiImageFragment.setAdapter(adapter);
		}

		return multiImageFragment;
	}

	/**
	 * Sets the adapter to be used with the fragment.
	 * @param adapter The pager adapter used to contain the images.
	 */
	public void setAdapter(final FragmentStatePagerAdapter adapter) {
		mAdapter = adapter;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_pager, null);
		if (view != null) {
			ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
			viewPager.setAdapter(mAdapter);
		}

		return view;
	}
}
