package com.missionse.componentexample.viewpager;

import java.util.Locale;

import com.missionse.componentexample.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

	private Context context;

	public SectionsPagerAdapter(final Context context, final FragmentManager fragmentManager) {
        super(fragmentManager);
		this.context = context;
    }

    @Override
    public Fragment getItem(final int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a DummySectionFragment (defined as a static inner class
        // below) with the page number as its lone argument.
        Fragment fragment = new DummySectionFragment();
        Bundle args = new Bundle();
        args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(final int position) {
        Locale locale = Locale.getDefault();
        switch (position) {
            case 0:
                return context.getString(R.string.title_section1).toUpperCase(locale);
            case 1:
                return context.getString(R.string.title_section2).toUpperCase(locale);
            case 2:
                return context.getString(R.string.title_section3).toUpperCase(locale);
        }
        return null;
    }
}
