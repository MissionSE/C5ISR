package com.missionse.logisticsexample;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.MapFragment;
import com.missionse.uiextensions.navigationdrawer.DrawerActivity;
import com.missionse.uiextensions.navigationdrawer.DrawerAdapter;
import com.missionse.uiextensions.navigationdrawer.DrawerItem;
import com.missionse.uiextensions.navigationdrawer.compatibility.DrawerSwipeToDismissTouchListener;
import com.missionse.uiextensions.navigationdrawer.configuration.DrawerConfiguration;
import com.missionse.uiextensions.navigationdrawer.configuration.DrawerConfigurationContainer;
import com.missionse.uiextensions.navigationdrawer.configuration.DrawerConfigurationContainer.DrawerType;
import com.missionse.uiextensions.navigationdrawer.entry.DrawerDivider;
import com.missionse.uiextensions.navigationdrawer.entry.DrawerHeader;
import com.missionse.uiextensions.navigationdrawer.entry.DrawerSimpleItem;
import com.missionse.uiextensions.navigationdrawer.entry.DrawerSpinner;
import com.missionse.uiextensions.touchlistener.SwipeToDismissListener;

public class LogisticsExample extends DrawerActivity {

	private ArrayAdapter<String> userAccountActionsAdapter;
	private DrawerAdapter rightDrawerAdapter;

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

		createNavigationDrawer(container);
		createNotificationDrawer(container);

		return container;
	}

	private void createNavigationDrawer(final DrawerConfigurationContainer container) {
		//Create spinner entries
		List<String> userAccountSpinnerEntries = new ArrayList<String>();
		userAccountSpinnerEntries.add("Logged Out");
		userAccountSpinnerEntries.add("Log In");
		userAccountActionsAdapter = new ArrayAdapter<String>(this, R.layout.nav_drawer_header, R.id.navheader_label,
				userAccountSpinnerEntries);

		List<DrawerItem> menu = new ArrayList<DrawerItem>();
		menu.add(DrawerSpinner.create(001, userAccountActionsAdapter, userAccountActionsListener));
		menu.add(DrawerDivider.create(002));
		menu.add(DrawerHeader.create(100, "Navigation"));
		menu.add(DrawerSimpleItem.create(101, "Map", R.drawable.places, true));
		menu.add(DrawerSimpleItem.create(102, "Supply List", R.drawable.ebooks, true));
		menu.add(DrawerSimpleItem.create(103, "History", R.drawable.analytics, true));

		DrawerConfiguration configuration = new DrawerConfiguration();
		configuration.setDrawer(R.id.nav_drawer);
		configuration.setNavigationItems(menu);
		configuration.setDrawerShadow(R.drawable.drawer_shadow);
		configuration.setDrawerOpenDesc(R.string.app_name);
		configuration.setDrawerCloseDesc(R.string.app_name);
		configuration.setBaseAdapter(new DrawerAdapter(this, 0, menu));

		container.setConfiguration(DrawerType.LEFT, configuration);
	}

	private void createNotificationDrawer(final DrawerConfigurationContainer container) {
		List<DrawerItem> notifications = new ArrayList<DrawerItem>();
		notifications.add(DrawerSimpleItem.create(101, "Map", R.drawable.places, false));
		notifications.add(DrawerSimpleItem.create(102, "Supply List", R.drawable.ebooks, false));
		notifications.add(DrawerSimpleItem.create(103, "History", R.drawable.analytics, false));

		DrawerConfiguration configuration = new DrawerConfiguration();
		configuration.setDrawer(R.id.notif_drawer);
		configuration.setNavigationItems(notifications);
		configuration.setDrawerShadow(R.drawable.drawer_shadow);
		configuration.setDrawerOpenDesc(R.string.app_name);
		configuration.setDrawerCloseDesc(R.string.app_name);
		rightDrawerAdapter = new DrawerAdapter(this, 0, notifications);
		configuration.setBaseAdapter(rightDrawerAdapter);

		container.setConfiguration(DrawerType.RIGHT, configuration);
	}

	@Override
	protected void onConfigurationComplete() {
		DrawerSwipeToDismissTouchListener touchListener = new DrawerSwipeToDismissTouchListener(getDrawerLayout(),
				getRightDrawerList(), new SwipeToDismissListener() {
					@Override
					public boolean canDismiss(final int position) {
						return true;
					}

					@Override
					public void onDismiss(final ListView listView, final int[] reverseSortedPositions) {
						for (int position : reverseSortedPositions) {
							rightDrawerAdapter.remove(rightDrawerAdapter.getItem(position));
						}
						rightDrawerAdapter.notifyDataSetChanged();
					}
				});

		getRightDrawerList().setOnTouchListener(touchListener);
		getRightDrawerList().setOnScrollListener(touchListener.makeScrollListener());
	}

	@Override
	protected void onNavigationItemSelected(final int id) {

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
