package com.missionse.componentexample.viewpager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.missionse.componentexample.R;
import com.missionse.slidingmenu.MenuClickListener;
import com.missionse.slidingmenu.SlidingMenuHelper;
import com.missionse.uiextensions.viewpager.DrawerSafeViewPager;
import com.missionse.uiextensions.viewpager.SectionFragmentPagerAdapter;

public class ViewPagerActivity extends Activity {

	private SectionFragmentPagerAdapter pagerAdapter;
	private DrawerSafeViewPager viewPager;

	private SlidingMenu navigationDrawer;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_pager);

		final List<String> menuItems = new ArrayList<String>();
		pagerAdapter = new SectionFragmentPagerAdapter(getFragmentManager());

		for (int pageCount = 0; pageCount <= 2; ++pageCount) {
			Bundle arguments = new Bundle();
			arguments.putInt(DummySectionFragment.ARG_SECTION_NUMBER, pageCount + 1);
			DummySectionFragment section = new DummySectionFragment();
			section.setArguments(arguments);

			String title = "Section " + (pageCount + 1);

			pagerAdapter.setPage(pageCount, title, section);
			menuItems.add(title);
		}

		viewPager = (DrawerSafeViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(pagerAdapter);

		SlidingMenuHelper menuHelper = new SlidingMenuHelper(this);
		navigationDrawer = menuHelper.createMenu(SlidingMenu.LEFT, menuItems, "Sections", new MenuClickListener() {
			@Override
			public void onMenuClick(final int clickedItem) {
				switchContent(clickedItem);
			}
		});
		menuHelper.complete();
	}

	@Override
	public void onBackPressed() {
		if (navigationDrawer.isMenuShowing()) {
			navigationDrawer.showContent(true);
		} else {
			super.onBackPressed();
		}
	}

	public void switchContent(final int position) {
		navigationDrawer.showContent();
		viewPager.setCurrentItem(position);
	}
}
