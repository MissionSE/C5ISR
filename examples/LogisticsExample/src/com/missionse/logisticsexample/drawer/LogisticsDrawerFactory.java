package com.missionse.logisticsexample.drawer;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;

import com.missionse.logisticsexample.R;
import com.missionse.uiextensions.navigationdrawer.DrawerAdapter;
import com.missionse.uiextensions.navigationdrawer.DrawerItem;
import com.missionse.uiextensions.navigationdrawer.configuration.DrawerConfiguration;
import com.missionse.uiextensions.navigationdrawer.configuration.DrawerConfigurationContainer;
import com.missionse.uiextensions.navigationdrawer.configuration.DrawerConfigurationContainer.DrawerType;
import com.missionse.uiextensions.navigationdrawer.entry.DrawerDivider;
import com.missionse.uiextensions.navigationdrawer.entry.DrawerHeader;
import com.missionse.uiextensions.navigationdrawer.entry.DrawerSimpleItem;
import com.missionse.uiextensions.navigationdrawer.entry.DrawerSpinner;

/**
 * Creates the two requisite Drawers, the left NavigationDrawer, and the right NotificationDrawer.
 */
public class LogisticsDrawerFactory {

	private static final int USER_ACCOUNT_ACTIONS = 001;
	private static final int DIVIDER = 002;
	private static final int NAVIGATION_HEADER = 100;
	private static final int MAP = 101;
	private static final int SUPPLY_LIST = 102;
	private static final int UPDATE_HISTORY = 103;

	private static final int NOTIF_ID1 = 901;
	private static final int NOTIF_ID2 = 902;
	private static final int NOTIF_ID3 = 903;

	private Context mContext;
	private ArrayAdapter<String> mUserAccountActionsAdapter;
	private DrawerAdapter mRightDrawerAdapter;
	private OnItemSelectedListener mListener;

	/**
	 * Creates a new LogisticsDrawerFactory.
	 * @param context the context to pass to created adapters
	 */
	public LogisticsDrawerFactory(final Context context) {
		mContext = context;
	}

	/**
	 * Sets the listener to be called back when an item is selected in the UserAccountActions spinner.
	 * @param listener the listener
	 */
	public void setUserAccountActionsListener(final OnItemSelectedListener listener) {
		mListener = listener;
	}

	/**
	 * Creates the two drawers.
	 * @return the configuration container containing both configurations for the drawers
	 */
	public DrawerConfigurationContainer createDrawers() {
		DrawerConfigurationContainer container = new DrawerConfigurationContainer(R.layout.activity_logistics_example,
				R.id.drawer_layout);

		createNavigationDrawer(container);
		createNotificationDrawer(container);

		return container;
	}

	private void createNavigationDrawer(final DrawerConfigurationContainer container) {
		List<String> userAccountSpinnerEntries = new ArrayList<String>();
		userAccountSpinnerEntries.add("Logged Out");
		userAccountSpinnerEntries.add("Log In");
		mUserAccountActionsAdapter = new ArrayAdapter<String>(mContext, R.layout.nav_drawer_header,
				R.id.navheader_label, userAccountSpinnerEntries);

		List<DrawerItem> menu = new ArrayList<DrawerItem>();
		menu.add(DrawerSpinner.create(USER_ACCOUNT_ACTIONS, mUserAccountActionsAdapter, mListener));
		menu.add(DrawerDivider.create(DIVIDER));
		menu.add(DrawerHeader.create(NAVIGATION_HEADER, "Navigation"));
		menu.add(DrawerSimpleItem.create(MAP, "Map", R.drawable.places, true));
		menu.add(DrawerSimpleItem.create(SUPPLY_LIST, "Supply List", R.drawable.ebooks, true));
		menu.add(DrawerSimpleItem.create(UPDATE_HISTORY, "History", R.drawable.analytics, true));

		DrawerConfiguration configuration = new DrawerConfiguration();
		configuration.setDrawer(R.id.nav_drawer);
		configuration.setNavigationItems(menu);
		configuration.setDrawerShadow(R.drawable.drawer_shadow_left);
		configuration.setDrawerOpenDesc(R.string.app_name);
		configuration.setDrawerCloseDesc(R.string.app_name);
		configuration.setBaseAdapter(new DrawerAdapter(mContext, 0, menu));

		container.setConfiguration(DrawerType.LEFT, configuration);
	}

	private void createNotificationDrawer(final DrawerConfigurationContainer container) {
		List<DrawerItem> notifications = new ArrayList<DrawerItem>();
		notifications.add(DrawerSimpleItem.create(NOTIF_ID1, "NOTIF 1", 0, false));
		notifications.add(DrawerSimpleItem.create(NOTIF_ID2, "NOTIF 2", 0, false));
		notifications.add(DrawerSimpleItem.create(NOTIF_ID3, "NOTIF 3", 0, false));

		DrawerConfiguration configuration = new DrawerConfiguration();
		configuration.setDrawer(R.id.notif_drawer);
		configuration.setNavigationItems(notifications);
		configuration.setDrawerShadow(R.drawable.drawer_shadow_right);
		configuration.setDrawerOpenDesc(R.string.app_name);
		configuration.setDrawerCloseDesc(R.string.app_name);
		mRightDrawerAdapter = new DrawerAdapter(mContext, 0, notifications);
		configuration.setBaseAdapter(mRightDrawerAdapter);

		container.setConfiguration(DrawerType.RIGHT, configuration);
	}

	/**
	 * Retrieves the DrawerAdapter created for the NotificationDrawer.
	 * @return the right DrawerAdapter
	 */
	public DrawerAdapter getRightDrawerAdapter() {
		return mRightDrawerAdapter;
	}
}
