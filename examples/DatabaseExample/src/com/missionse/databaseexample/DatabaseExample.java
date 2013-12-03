package com.missionse.databaseexample;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.missionse.uiextensions.viewpager.SectionFragmentPagerAdapter;
import com.missionse.uiextensions.viewpager.ZoomOutPageTransformer;

/**
 * Acts as the entry point to the DatabaseExample application. Sets up the fragments and ViewPager.
 */
public class DatabaseExample extends Activity {

	private ViewPager mViewPager;
	private StudentListFragment mStudentListFragment;
	private ClassroomListFragment mClassroomListFragment;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_databaseexample);

		mStudentListFragment = new StudentListFragment();
		mClassroomListFragment = new ClassroomListFragment();

		SectionFragmentPagerAdapter pagerAdapter = new SectionFragmentPagerAdapter(getFragmentManager());
		pagerAdapter.setPage(0, "Student List", mStudentListFragment);
		pagerAdapter.setPage(1, "Classroom List", mClassroomListFragment);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setAdapter(pagerAdapter);
		mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
	}
}
