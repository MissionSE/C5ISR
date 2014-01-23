package com.missionse.gsonexample;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.missionse.gsonexample.fragments.ShopBoardFragment;
import com.missionse.gsonexample.fragments.WeatherFragment;
import com.missionse.uiextensions.viewpager.SectionFragmentPagerAdapter;
import com.missionse.uiextensions.viewpager.ZoomOutPageTransformer;

/**
 * Acts as the entry point to the GsonExample application. Sets up the fragments and ViewPager.
 */
public class GsonExample extends Activity {

	private ViewPager mViewPager;
	private ShopBoardFragment mShopBoardFragment;
	private WeatherFragment mWeatherFragment;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gsonexample_main);

		mShopBoardFragment = new ShopBoardFragment();
		mWeatherFragment = new WeatherFragment();
		
		SectionFragmentPagerAdapter pagerAdapter = new SectionFragmentPagerAdapter(getFragmentManager());
		pagerAdapter.setPage(0, "Shop Board", mShopBoardFragment);
		pagerAdapter.setPage(1, "Weather", mWeatherFragment);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setAdapter(pagerAdapter);
		mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
	}
}
