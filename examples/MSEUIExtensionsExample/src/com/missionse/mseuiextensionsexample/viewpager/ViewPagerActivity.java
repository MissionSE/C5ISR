package com.missionse.mseuiextensionsexample.viewpager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.missionse.mseuiextensions.R;
import com.missionse.slidingmenu.OnMenuClickListener;
import com.missionse.slidingmenu.SlidingMenuHelper;
import com.missionse.slidingmenu.SlidingMenuHelper.MenuType;
import com.missionse.uiextensions.viewpager.DepthPageTransformer;
import com.missionse.uiextensions.viewpager.DrawerSafeViewPager;
import com.missionse.uiextensions.viewpager.SectionFragmentPagerAdapter;

/**
 * Acts as the entry point to the ViewPager demonstration.
 */
public class ViewPagerActivity extends Activity {

	private SectionFragmentPagerAdapter mPagerAdapter;
	private DrawerSafeViewPager mViewPager;

	private SlidingMenu mNavigationDrawer;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_pager);

		final List<String> menuItems = new ArrayList<String>();
		mPagerAdapter = new SectionFragmentPagerAdapter(getFragmentManager());

		for (int pageCount = 0; pageCount <= 2; ++pageCount) {
			Bundle arguments = new Bundle();
			arguments.putInt(DummySectionFragment.ARG_SECTION_NUMBER, pageCount + 1);
			DummySectionFragment section = new DummySectionFragment();
			section.setArguments(arguments);

			String title = "Section " + (pageCount + 1);

			mPagerAdapter.setPage(pageCount, title, section);
			menuItems.add(title);
		}

		mViewPager = (DrawerSafeViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setPageTransformer(true, new DepthPageTransformer());

		SlidingMenuHelper menuHelper = new SlidingMenuHelper(this);
		menuHelper.createSimpleMenu(MenuType.LEFT, menuItems, new OnMenuClickListener() {
			@Override
			public void onMenuClick(final int clickedItem) {
				switchContent(clickedItem);
			}
		});
		menuHelper.getLeftMenu().setTitle("Sections");
		menuHelper.commit();
	}

	@Override
	public void onBackPressed() {
		if (mNavigationDrawer.isMenuShowing()) {
			mNavigationDrawer.showContent(true);
		} else {
			super.onBackPressed();
		}
	}

	/**
	 * Changes the displayed content.
	 * @param position the page to switch to
	 */
	public void switchContent(final int position) {
		mNavigationDrawer.showContent();
		mViewPager.setCurrentItem(position);
	}
}
