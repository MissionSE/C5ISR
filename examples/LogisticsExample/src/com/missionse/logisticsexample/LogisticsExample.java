package com.missionse.logisticsexample;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;

import com.missionse.logisticsexample.navdrawer.NavigationDrawerActivity;
import com.missionse.logisticsexample.navdrawer.NavigationDrawerAdapter;
import com.missionse.logisticsexample.navdrawer.NavigationDrawerConfiguration;
import com.missionse.logisticsexample.navdrawer.NavigationDrawerItem;
import com.missionse.logisticsexample.navdrawer.NavigationDrawerMenuBasic;
import com.missionse.logisticsexample.navdrawer.NavigationDrawerMenuDropdown;
import com.missionse.logisticsexample.navdrawer.NavigationDrawerMenuHeader;

public class LogisticsExample extends NavigationDrawerActivity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.logistics_example, menu);
		return true;
	}

	@Override
	protected NavigationDrawerConfiguration getNavDrawerConfiguration() {
		List<String> userEntries = new ArrayList<String>();
		userEntries.add("Log In");
		userEntries.add("Add New Account");
		ArrayAdapter<String> user = new ArrayAdapter<String>(this, R.layout.nav_drawer_header, R.id.navheader_label,
				userEntries);

		NavigationDrawerItem[] menu = new NavigationDrawerItem[] {
				NavigationDrawerMenuDropdown.create(001, "User Admin", user),
				NavigationDrawerMenuHeader.create(100, "Navigation"),
				NavigationDrawerMenuBasic.create(101, "Map", "ic_launcher", true, this),
				NavigationDrawerMenuBasic.create(102, "Supply List", "ic_launcher", true, this),
				NavigationDrawerMenuBasic.create(103, "History", "ic_launcher", true, this),
				NavigationDrawerMenuHeader.create(200, "HEADER"),
				NavigationDrawerMenuBasic.create(201, "FEATURE", "ic_launcher", true, this) };

		NavigationDrawerConfiguration navDrawerActivityConfiguration = new NavigationDrawerConfiguration();
		navDrawerActivityConfiguration.setMainLayout(R.layout.activity_logistics_example);
		navDrawerActivityConfiguration.setDrawerLayoutId(R.id.drawer_layout);
		navDrawerActivityConfiguration.setLeftDrawerId(R.id.drawer);
		navDrawerActivityConfiguration.setNavItems(menu);
		navDrawerActivityConfiguration.setDrawerShadow(R.drawable.drawer_shadow);
		navDrawerActivityConfiguration.setDrawerOpenDesc(R.string.app_name);
		navDrawerActivityConfiguration.setDrawerCloseDesc(R.string.app_name);
		navDrawerActivityConfiguration
				.setBaseAdapter(new NavigationDrawerAdapter(this, R.layout.nav_drawer_item, menu));
		return navDrawerActivityConfiguration;
	}

	@Override
	protected void onNavItemSelected(final int id) {
		// TODO Auto-generated method stub

	}

}
