package com.missionse.uiextensions.viewpager;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

/**
 * Extends the FragmentPagerAdapter to maintain page titles for a PagerTitleStrip.
 */
public class SectionFragmentPagerAdapter extends FragmentPagerAdapter {

	@SuppressLint("UseSparseArrays")
	private final Map<Integer, Fragment> mPages = new HashMap<Integer, Fragment>();

	@SuppressLint("UseSparseArrays")
	private final Map<Integer, String> mPageTitles = new HashMap<Integer, String>();

	/**
	 * Creates a new SectionFragmentPagerAdapter.
	 * @param fragmentManager the FragmentManager that owns this fragment
	 */
	public SectionFragmentPagerAdapter(final FragmentManager fragmentManager) {
		super(fragmentManager);
	}

	/**
	 * Sets a given Fragment to a given index.
	 * @param pageNumber the pager number to set to
	 * @param fragmentTitle the title to assign to this page, can be null.
	 * @param fragment the fragment to appear at the given page number
	 */
	public void setPage(final int pageNumber, final String fragmentTitle, final Fragment fragment) {
		mPages.put(Integer.valueOf(pageNumber), fragment);
		if (fragmentTitle != null) {
			mPageTitles.put(Integer.valueOf(pageNumber), fragmentTitle);
		} else {
			mPageTitles.put(Integer.valueOf(pageNumber), "");
		}

	}

	@Override
	public Fragment getItem(final int selectedItem) {
		return mPages.get(Integer.valueOf(selectedItem));
	}

	@Override
	public int getCount() {
		return mPages.size();
	}

	@Override
	public CharSequence getPageTitle(final int position) {
		if (mPageTitles.containsKey(Integer.valueOf(position))) {
			return mPageTitles.get(Integer.valueOf(position));
		}
		return null;
	}
}
