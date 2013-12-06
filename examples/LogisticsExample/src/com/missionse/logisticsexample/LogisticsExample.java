package com.missionse.logisticsexample;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;

import com.missionse.uiextensions.navigationdrawer.NavigationDrawerActivity;
import com.missionse.uiextensions.navigationdrawer.NavigationDrawerAdapter;
import com.missionse.uiextensions.navigationdrawer.NavigationDrawerConfiguration;
import com.missionse.uiextensions.navigationdrawer.NavigationDrawerHeader;
import com.missionse.uiextensions.navigationdrawer.NavigationDrawerItem;
import com.missionse.uiextensions.navigationdrawer.NavigationDrawerSimpleItem;
import com.missionse.uiextensions.navigationdrawer.NavigationDrawerSpinner;

public class LogisticsExample extends NavigationDrawerActivity {

	private ArrayAdapter<String> userAccountActionsAdapter;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.logistics_example, menu);
		return true;
	}

	@Override
	protected NavigationDrawerConfiguration getNavigationDrawerConfiguration() {
		//Create spinner entries
		List<String> userAccountSpinnerEntries = new ArrayList<String>();
		userAccountSpinnerEntries.add("Log In");
		userAccountSpinnerEntries.add("Add New Account");
		userAccountActionsAdapter = new ArrayAdapter<String>(this, R.layout.nav_drawer_header, R.id.navheader_label,
				userAccountSpinnerEntries);

		NavigationDrawerItem[] menu = new NavigationDrawerItem[] {
				NavigationDrawerSpinner.create(001, userAccountActionsAdapter),
				NavigationDrawerHeader.create(100, "Navigation"),
				NavigationDrawerSimpleItem.create(101, "Map", R.drawable.ic_launcher, true),
				NavigationDrawerSimpleItem.create(102, "Supply List", R.drawable.ic_launcher, true),
				NavigationDrawerSimpleItem.create(103, "History", R.drawable.ic_launcher, true),
				NavigationDrawerHeader.create(200, "HEADER"),
				NavigationDrawerSimpleItem.create(201, "FEATURE", R.drawable.ic_launcher, true) };

		NavigationDrawerConfiguration navDrawerActivityConfiguration = new NavigationDrawerConfiguration();
		navDrawerActivityConfiguration.setMainLayout(R.layout.activity_logistics_example);
		navDrawerActivityConfiguration.setDrawerLayoutId(R.id.drawer_layout);
		navDrawerActivityConfiguration.setLeftDrawerId(R.id.drawer);
		navDrawerActivityConfiguration.setNavItems(menu);
		navDrawerActivityConfiguration.setDrawerShadow(R.drawable.drawer_shadow);
		navDrawerActivityConfiguration.setDrawerOpenDesc(R.string.app_name);
		navDrawerActivityConfiguration.setDrawerCloseDesc(R.string.app_name);
		navDrawerActivityConfiguration.setBaseAdapter(new NavigationDrawerAdapter(this, 0, menu));
		return navDrawerActivityConfiguration;
	}

	@Override
	protected void onNavigationItemSelected(final int id) {

	}

}
