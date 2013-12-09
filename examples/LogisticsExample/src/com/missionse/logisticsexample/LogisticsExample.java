package com.missionse.logisticsexample;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.MapFragment;
import com.missionse.uiextensions.navigationdrawer.DrawerActivity;
import com.missionse.uiextensions.navigationdrawer.NavigationDrawerAdapter;
import com.missionse.uiextensions.navigationdrawer.NavigationDrawerItem;
import com.missionse.uiextensions.navigationdrawer.configuration.DrawerConfiguration;
import com.missionse.uiextensions.navigationdrawer.configuration.DrawerConfigurationContainer;
import com.missionse.uiextensions.navigationdrawer.configuration.DrawerConfigurationContainer.DrawerType;
import com.missionse.uiextensions.navigationdrawer.entry.NavigationDrawerDivider;
import com.missionse.uiextensions.navigationdrawer.entry.NavigationDrawerHeader;
import com.missionse.uiextensions.navigationdrawer.entry.NavigationDrawerSimpleItem;
import com.missionse.uiextensions.navigationdrawer.entry.NavigationDrawerSpinner;

public class LogisticsExample extends DrawerActivity {

	private ArrayAdapter<String> userAccountActionsAdapter;

	private OnItemSelectedListener userAccountActionsListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {

		}

		@Override
		public void onNothingSelected(final AdapterView<?> parent) {
			//Nothing to do.
		}
	};

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getFragmentManager().beginTransaction().replace(R.id.content, new MapFragment()).commit();
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.logistics_example, menu);
		return true;
	}

	@Override
	protected DrawerConfigurationContainer getDrawerConfigurations() {
		DrawerConfigurationContainer container = new DrawerConfigurationContainer(R.layout.activity_logistics_example,
				R.id.drawer_layout);

		//Create spinner entries
		List<String> userAccountSpinnerEntries = new ArrayList<String>();
		userAccountSpinnerEntries.add("Logged Out");
		userAccountSpinnerEntries.add("Log In");
		userAccountActionsAdapter = new ArrayAdapter<String>(this, R.layout.nav_drawer_header, R.id.navheader_label,
				userAccountSpinnerEntries);

		NavigationDrawerItem[] menu = new NavigationDrawerItem[] {
				NavigationDrawerSpinner.create(001, userAccountActionsAdapter, userAccountActionsListener),
				NavigationDrawerDivider.create(002), NavigationDrawerHeader.create(100, "Navigation"),
				NavigationDrawerSimpleItem.create(101, "Map", R.drawable.places, true),
				NavigationDrawerSimpleItem.create(102, "Supply List", R.drawable.ebooks, true),
				NavigationDrawerSimpleItem.create(103, "History", R.drawable.analytics, true) };

		DrawerConfiguration navDrawerConfiguration = new DrawerConfiguration();
		navDrawerConfiguration.setDrawer(R.id.nav_drawer);
		navDrawerConfiguration.setNavItems(menu);
		navDrawerConfiguration.setDrawerShadow(R.drawable.drawer_shadow);
		navDrawerConfiguration.setDrawerOpenDesc(R.string.app_name);
		navDrawerConfiguration.setDrawerCloseDesc(R.string.app_name);
		navDrawerConfiguration.setBaseAdapter(new NavigationDrawerAdapter(this, 0, menu));

		NavigationDrawerItem[] notifications = new NavigationDrawerItem[] {
				NavigationDrawerSimpleItem.create(101, "Map", R.drawable.places, true),
				NavigationDrawerSimpleItem.create(102, "Supply List", R.drawable.ebooks, true),
				NavigationDrawerSimpleItem.create(103, "History", R.drawable.analytics, true) };

		DrawerConfiguration notifDrawerConfiguration = new DrawerConfiguration();
		notifDrawerConfiguration.setDrawer(R.id.notif_drawer);
		notifDrawerConfiguration.setNavItems(notifications);
		notifDrawerConfiguration.setDrawerShadow(R.drawable.drawer_shadow);
		notifDrawerConfiguration.setDrawerOpenDesc(R.string.app_name);
		notifDrawerConfiguration.setDrawerCloseDesc(R.string.app_name);
		notifDrawerConfiguration.setBaseAdapter(new NavigationDrawerAdapter(this, 0, notifications));

		container.setConfiguration(DrawerType.LEFT, navDrawerConfiguration);
		container.setConfiguration(DrawerType.RIGHT, notifDrawerConfiguration);
		return container;
	}

	@Override
	protected void onNavigationItemSelected(final int id) {

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
