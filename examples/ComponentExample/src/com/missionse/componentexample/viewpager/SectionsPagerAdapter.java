package com.missionse.componentexample.viewpager;

import java.util.Locale;

import com.missionse.componentexample.R;
import com.missionse.modelviewer.ModelViewerFragmentFactory;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;

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
    	Fragment fragment;
    	if (position == 2) {
    		fragment = (Fragment) ModelViewerFragmentFactory.createObjModelFragment(R.raw.multiobjects_obj);
    	}
    	else {
    		fragment = new DummySectionFragment();
            Bundle args = new Bundle();
            args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
            fragment.setArguments(args);
    	}
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
