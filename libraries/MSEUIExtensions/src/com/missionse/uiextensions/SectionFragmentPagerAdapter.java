package com.missionse.uiextensions;

import java.util.HashMap;
import java.util.Map;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

public class SectionFragmentPagerAdapter extends FragmentPagerAdapter {
	
	private final Map<Integer, Fragment> pages = new HashMap<Integer, Fragment>();
	
	public SectionFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public void setPage(final int pageNumber, final Fragment fragment) {
		pages.put(Integer.valueOf(pageNumber), fragment);
	}
	
	@Override
	public Fragment getItem(int selectedItem) {
		return pages.get(Integer.valueOf(selectedItem));
	}

	@Override
	public int getCount() {
		return pages.size();
	}
}
