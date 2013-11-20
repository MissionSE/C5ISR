package com.missionse.uiextensions.viewpager;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

public class SectionFragmentPagerAdapter extends FragmentPagerAdapter {

	@SuppressLint("UseSparseArrays")
	private final Map<Integer, Fragment> pages = new HashMap<Integer, Fragment>();

	public SectionFragmentPagerAdapter(final FragmentManager fragmentManager) {
		super(fragmentManager);
	}

	public void setPage(final int pageNumber, final Fragment fragment) {
		pages.put(Integer.valueOf(pageNumber), fragment);
	}

	@Override
	public Fragment getItem(final int selectedItem) {
		return pages.get(Integer.valueOf(selectedItem));
	}

	@Override
	public int getCount() {
		return pages.size();
	}
}
