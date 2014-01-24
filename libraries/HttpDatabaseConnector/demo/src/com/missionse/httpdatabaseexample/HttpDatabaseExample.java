package com.missionse.httpdatabaseexample;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

/**
 * Provides an activity that is an example on how to use the HttpDatabaseConnector library.
 */
public class HttpDatabaseExample extends Activity {
	private ViewPager mViewPager;
	private StudentListFragment mStudentListFragment;
	private ClassroomListFragment mClassroomListFragment;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_http_database_example);

		mStudentListFragment = new StudentListFragment();
		mClassroomListFragment = new ClassroomListFragment();

//		SectionFragmentPagerAdapter pagerAdapter = new SectionFragmentPagerAdapter(getFragmentManager());
//		pagerAdapter.setPage(0, "Student List", mStudentListFragment);
//		pagerAdapter.setPage(1, "Classroom List", mClassroomListFragment);
//		mViewPager = (ViewPager) findViewById(R.id.viewpager);
//		mViewPager.setAdapter(pagerAdapter);
//		mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
	}
}
