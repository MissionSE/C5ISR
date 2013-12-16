package com.missionse.logisticsexample;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListView;

import com.missionse.logisticsexample.database.DatabaseHelper;
import com.missionse.logisticsexample.drawer.LogisticsDrawerFactory;
import com.missionse.logisticsexample.map.LogisticsMap;
import com.missionse.logisticsexample.map.MapViewerFragment;
import com.missionse.uiextensions.navigationdrawer.DrawerActivity;
import com.missionse.uiextensions.navigationdrawer.DrawerAdapter;
import com.missionse.uiextensions.navigationdrawer.configuration.DrawerConfigurationContainer;
import com.missionse.uiextensions.navigationdrawer.entry.DrawerComplexItem;
import com.missionse.uiextensions.touchlistener.SwipeToDismissListener;

/**
 * Main entry point to the Logistics application. Instantiates the two drawers, and loads the initial fragment into the
 * content space.
 */
public class LogisticsExample extends DrawerActivity {
	private LogisticsDrawerFactory mDrawerFactory;
	private LogisticsMap mLogisticsMap;

	private DatabaseHelper mDbHelper;

	private static final int INITIAL_NOTIFICATION_ID = 300;
	private static int mCurrentNotificationId = INITIAL_NOTIFICATION_ID;
	private static int mNotificationCount = 0;
	private static int mNotificationBackground = R.drawable.notification_action_bar_zero;
	private static int mNotificationTextColor = R.color.default_very_dark_gray;

	/**
	 * Constructs a new LogisticsExample.
	 */
	public LogisticsExample() {
		mLogisticsMap = new LogisticsMap(this);
		mDrawerFactory = new LogisticsDrawerFactory(this);
		mDbHelper = new DatabaseHelper(this);
		mDbHelper.initialize();
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private void displayMap() {
		FragmentManager fragmentManager = getFragmentManager();
		MapViewerFragment mapViewerFragment = (MapViewerFragment) fragmentManager.findFragmentByTag("map");
		if (mapViewerFragment == null) {
			mapViewerFragment = new MapViewerFragment();
			mapViewerFragment.setMapLoadedListener(mLogisticsMap);
			fragmentManager.beginTransaction().add(R.id.content, mapViewerFragment, "map").commit();
		}
	}

	private void displayLocationDatabase() {

	}

	private void displayOrderDatabase() {

	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.menu_activity_logistics_example, menu);

		MenuItem notificationCountItem = menu.findItem(R.id.action_notification);
		View notificationView = notificationCountItem.getActionView();
		Button notificationButton = (Button) notificationView.findViewById(R.id.action_bar_notification_count);
		notificationButton.setBackground(getResources().getDrawable(mNotificationBackground));
		notificationButton.setText(String.valueOf(mNotificationCount));
		notificationButton.setTextColor(getResources().getColor(mNotificationTextColor));
		notificationButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View button) {
				if (getDrawerLayout().isDrawerOpen(Gravity.END)) {
					getDrawerLayout().closeDrawer(Gravity.END);
				} else {
					getDrawerLayout().openDrawer(Gravity.END);
				}
			}
		});

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_settings:
				break;
			case R.id.action_feedback:
				break;
			case R.id.action_help:
				break;
			case R.id.action_licenses:
				break;
			default:
				break;
		}
		return true;
	}

	@Override
	protected DrawerConfigurationContainer getDrawerConfigurations() {
		return mDrawerFactory.createDrawers();
	}

	@Override
	protected void onDrawerConfigurationComplete() {
		mDrawerFactory.onDrawerConfigurationComplete(this, new SwipeToDismissListener() {

			@Override
			public boolean canDismiss(final int position) {
				return true; //This is meaningless; does not get invoked.
			}

			@Override
			public void onDismiss(final ListView listView, final int[] positions) {
				adjustNotificationActionBar();
			}

		});

		mDrawerFactory.addNavigationMenuItems(getLeftDrawerAdapter(), new OnItemSelectedListener() {
			@Override
			public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {

			}

			@Override
			public void onNothingSelected(final AdapterView<?> parent) {
			}
		});

		selectItem(((DrawerAdapter) getLeftDrawerList().getAdapter()).getPosition(LogisticsDrawerFactory.MAP),
				getLeftDrawerList());
	}

	@Override
	protected void onNavigationItemSelected(final int id) {
		if (id == LogisticsDrawerFactory.MAP) {
			getRightDrawerAdapter().add(
					DrawerComplexItem.create(++mCurrentNotificationId, "[SEVERE] [" + mCurrentNotificationId + "]",
							"This is a severe notification. This is extra detail about this notification.",
							R.drawable.ic_action_error, false));
			getRightDrawerAdapter().add(
					DrawerComplexItem.create(++mCurrentNotificationId, "[WARNING] [" + mCurrentNotificationId + "]",
							"This is a warning. This is extra detail about this notification.",
							R.drawable.ic_action_warning, false));
			getRightDrawerAdapter().add(
					DrawerComplexItem.create(++mCurrentNotificationId, "[INFO] [" + mCurrentNotificationId + "]",
							"This is an information notification. This is extra detail about this notification.",
							R.drawable.ic_action_about, false));

			adjustNotificationActionBar();

			displayMap();
		} else if (id == LogisticsDrawerFactory.LOCATION_LIST) {
			displayLocationDatabase();
		} else if (id == LogisticsDrawerFactory.ORDER_LIST) {
			displayOrderDatabase();
		}
	}

	private void adjustNotificationActionBar() {
		mNotificationCount = getRightDrawerAdapter().getCount();
		if (mNotificationCount > 0) {
			mNotificationBackground = R.drawable.notification_action_bar;
			mNotificationTextColor = R.color.white;
		} else {
			mNotificationBackground = R.drawable.notification_action_bar_zero;
			mNotificationTextColor = R.color.default_very_dark_gray;
		}
		invalidateOptionsMenu();
	}
}
