package com.missionse.mseuiextensionsexample.viewpager;

import android.app.Activity;
import android.os.Bundle;

import com.missionse.mseuiextensions.R;
import com.missionse.uiextensions.viewpager.DepthPageTransformer;
import com.missionse.uiextensions.viewpager.DrawerSafeViewPager;
import com.missionse.uiextensions.viewpager.SectionFragmentPagerAdapter;

/**
 * Demonstrates the usage of the SectionFragmentPagerAdapter and the DrawerSafeViewPager.
 */
public class ViewPagerActivity extends Activity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_pager);

		SectionFragmentPagerAdapter pagerAdapter = new SectionFragmentPagerAdapter(getFragmentManager());

		for (int pageCount = 0; pageCount <= 2; ++pageCount) {
			Bundle arguments = new Bundle();
			arguments.putInt(DummySectionFragment.ARG_SECTION_NUMBER, pageCount + 1);
			DummySectionFragment section = new DummySectionFragment();
			section.setArguments(arguments);

			String title = "Section " + (pageCount + 1);

			pagerAdapter.setPage(pageCount, title, section);
		}

		DrawerSafeViewPager drawerSafeViewPager = (DrawerSafeViewPager) findViewById(R.id.pager);
		drawerSafeViewPager.setAdapter(pagerAdapter);
		drawerSafeViewPager.setPageTransformer(true, new DepthPageTransformer());
	}
}
