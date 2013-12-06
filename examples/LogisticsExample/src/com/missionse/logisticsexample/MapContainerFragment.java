package com.missionse.logisticsexample;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapFragment;

public class MapContainerFragment extends Fragment {

	private static final int PAGES = 2;

	private ViewPager mPager;
	private FragmentPagerAdapter mPagerAdapter;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_map_container, container, false);
		mPager = (ViewPager) contentView.findViewById(R.id.pager);
		mPagerAdapter = new MapWithFilterPagerAdapter(getFragmentManager());
		mPager.setAdapter(mPagerAdapter);

		return contentView;
	}

	public int getCurrentPage() {
		return mPager.getCurrentItem();
	}

	public void setCurrentPage(final int page) {
		mPager.setCurrentItem(page);
	}

	private class MapWithFilterPagerAdapter extends FragmentPagerAdapter {

		public MapWithFilterPagerAdapter(final FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public Fragment getItem(final int position) {
			if (position == 0) {
				return new MapFragment();
			} else if (position == 1) {
				return new MapFragment();
			}
			return null;
		}

		@Override
		public int getCount() {
			return PAGES;
		}
	}
}
