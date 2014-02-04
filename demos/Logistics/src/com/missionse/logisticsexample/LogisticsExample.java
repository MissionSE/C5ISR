package com.missionse.logisticsexample;

import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.missionse.logisticsexample.database.DatabaseFactory;
import com.missionse.logisticsexample.database.DatabaseUpdateCompleteListener;
import com.missionse.logisticsexample.database.LocalDatabaseHelper;
import com.missionse.logisticsexample.databaseview.order.OrderViewerContainerFragment;
import com.missionse.logisticsexample.databaseview.site.SiteViewerContainerFragment;
import com.missionse.logisticsexample.drawer.LogisticsDrawerFactory;
import com.missionse.logisticsexample.map.LogisticsMap;
import com.missionse.logisticsexample.map.MapViewerFragment;
import com.missionse.uiextensions.navigationdrawer.DrawerActivity;
import com.missionse.uiextensions.navigationdrawer.configuration.DrawerConfigurationContainer;
import com.missionse.uiextensions.navigationdrawer.entry.DrawerComplexItem;
import com.missionse.uiextensions.touchlistener.SwipeToDismissListener;

/**
 * Main entry point to the Logistics application. Instantiates the two drawers, and loads the initial fragment into the
 * content space.
 */
public class LogisticsExample extends DrawerActivity implements DatabaseUpdateCompleteListener {
	private static final String TAG = LogisticsExample.class.getName();

	private LogisticsDrawerFactory mDrawerFactory;
	private LogisticsMap mLogisticsMap;
	private LocalDatabaseHelper mDatabaseHelper;

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
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDatabaseHelper = DatabaseFactory.initializeDatabase(this, this);
	}

	private void displayMap() {
		FragmentManager fragmentManager = getFragmentManager();
		MapViewerFragment mapViewerFragment = (MapViewerFragment) fragmentManager.findFragmentByTag("map");
		if (mapViewerFragment == null) {
			mapViewerFragment = new MapViewerFragment();
			mapViewerFragment.setMapLoadedListener(mLogisticsMap);
		}
		fragmentManager.beginTransaction().replace(R.id.content, mapViewerFragment, "map").addToBackStack("map")
				.commit();
	}

	private void displaySiteDatabase() {
		FragmentManager fragmentManager = getFragmentManager();
		SiteViewerContainerFragment containerFragment = (SiteViewerContainerFragment) fragmentManager
				.findFragmentByTag("sitecontainer");
		if (containerFragment == null) {
			containerFragment = new SiteViewerContainerFragment();
		}
		fragmentManager.beginTransaction().replace(R.id.content, containerFragment, "sitecontainer")
				.addToBackStack("sitecontainer").commit();
	}

	private void displayOrderDatabase() {
		FragmentManager fragmentManager = getFragmentManager();
		OrderViewerContainerFragment containerFragment = (OrderViewerContainerFragment) fragmentManager
				.findFragmentByTag("ordercontainer");
		if (containerFragment == null) {
			containerFragment = new OrderViewerContainerFragment();
		}
		fragmentManager.beginTransaction().replace(R.id.content, containerFragment, "ordercontainer")
				.addToBackStack("ordercontainer").commit();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		syncNavigationDrawerWithContent();
	}

	private void syncNavigationDrawerWithContent() {
		FragmentManager fragmentManager = getFragmentManager();
		MapViewerFragment mapViewerFragment = (MapViewerFragment) fragmentManager.findFragmentByTag("map");
		if ((mapViewerFragment != null) && mapViewerFragment.isVisible()) {
			selectItem(getLeftDrawerAdapter().getPosition(LogisticsDrawerFactory.MAP), getLeftDrawerList());
		}
		SiteViewerContainerFragment siteContainerFragment = (SiteViewerContainerFragment) fragmentManager
				.findFragmentByTag("container");
		if ((siteContainerFragment != null) && siteContainerFragment.isVisible()) {
			selectItem(getLeftDrawerAdapter().getPosition(LogisticsDrawerFactory.LOCATION_LIST), getLeftDrawerList());
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(final Menu menu) {
		MenuItem dismissAll = menu.findItem(R.id.action_dismiss_all);
		dismissAll.setVisible(getDrawerLayout().isDrawerOpen(Gravity.END));

		return super.onPrepareOptionsMenu(menu);
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
		if (!super.onOptionsItemSelected(item)) {
			switch (item.getItemId()) {
				case R.id.action_dismiss_all:
					getRightDrawerAdapter().clear();
					getRightDrawerAdapter().notifyDataSetChanged();
					adjustNotificationActionBar();
					break;
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
				for (int position : positions) {
					listView.setItemChecked(position, false);
				}
			}
		});

		mDrawerFactory.addNavigationMenuItems(getLeftDrawerAdapter());

		selectItem(getLeftDrawerAdapter().getPosition(LogisticsDrawerFactory.MAP), getLeftDrawerList());
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
			displaySiteDatabase();
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

	/**
	 * Returns the database helper.
	 * @return The database helper.
	 */
	public LocalDatabaseHelper getDatabaseHelper() {
		return mDatabaseHelper;
	}

	@Override
	public void onDatabaseUpdateComplete() {
		Log.v(TAG, "Database update complete.");
		mLogisticsMap.requestAllLocations();
	}


    @Override
    public void onPause() {
        super.onPause();
        DatabaseFactory.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        DatabaseFactory.resume();
    }



}
