package com.missionse.httpdatabaseexample;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.missionse.uiextensions.viewpager.SectionFragmentPagerAdapter;
import com.missionse.uiextensions.viewpager.ZoomOutPageTransformer;

public class HttpDatabaseExample extends Activity {

	ViewPager viewPager;
	StudentListFragment studentListFragment;
	ClassroomListFragment classroomListFragment;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_http_database_example);

		studentListFragment = new StudentListFragment();
		classroomListFragment = new ClassroomListFragment();

		SectionFragmentPagerAdapter pagerAdapter = new SectionFragmentPagerAdapter(getFragmentManager());
		pagerAdapter.setPage(0, "Student List", studentListFragment);
		pagerAdapter.setPage(1, "Classroom List", classroomListFragment);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
	}
}
