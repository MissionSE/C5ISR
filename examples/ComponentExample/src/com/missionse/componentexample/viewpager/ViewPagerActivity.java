package com.missionse.componentexample.viewpager;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.missionse.componentexample.R;
import com.missionse.drawersafeviewpager.DrawerSafeViewPager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class ViewPagerActivity extends FragmentActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter pagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private DrawerSafeViewPager viewPager;
    
    private SlidingMenu navigationDrawer;
    
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        
        pagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        viewPager = (DrawerSafeViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);
        
        navigationDrawer = new SlidingMenu(this);
        navigationDrawer.setMode(SlidingMenu.LEFT);
        navigationDrawer.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        navigationDrawer.setShadowWidthRes(R.dimen.drawer_shadow_width);
        navigationDrawer.setShadowDrawable(R.drawable.shadow);
        navigationDrawer.setBehindOffsetRes(R.dimen.drawer_offset);
        navigationDrawer.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        navigationDrawer.setMenu(R.layout.drawer);
        
        Fragment drawerFragment;
        if (savedInstanceState == null) {
			FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
			drawerFragment = new DrawerFragment();
			transaction.replace(R.id.menu_frame, drawerFragment);
			transaction.commit();
		} else {
			drawerFragment = (ListFragment)this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
		}
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main_content, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.action_settings:
    		//TODO: handle settings
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
    
    @Override
    public void onBackPressed() {
    	if (navigationDrawer.isMenuShowing()) {
    		navigationDrawer.showContent(true);
    	}
    	else {
    		super.onBackPressed();
    	}
    }
    
    public void switchContent(int position) {
    	navigationDrawer.showContent();
    	viewPager.setCurrentItem(position);
    }
    
}
