package com.missionse.logisticsexample.drawer;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.missionse.logisticsexample.R;
import com.missionse.uiextensions.navigationdrawer.DrawerActivity;
import com.missionse.uiextensions.navigationdrawer.DrawerAdapter;
import com.missionse.uiextensions.navigationdrawer.DrawerItem;
import com.missionse.uiextensions.navigationdrawer.compatibility.DrawerSwipeToDismissTouchListener;
import com.missionse.uiextensions.navigationdrawer.configuration.DrawerConfiguration;
import com.missionse.uiextensions.navigationdrawer.configuration.DrawerConfigurationContainer;
import com.missionse.uiextensions.navigationdrawer.configuration.DrawerConfigurationContainer.DrawerType;
import com.missionse.uiextensions.navigationdrawer.entry.DrawerDivider;
import com.missionse.uiextensions.navigationdrawer.entry.DrawerHeader;
import com.missionse.uiextensions.navigationdrawer.entry.DrawerPaddedDivider;
import com.missionse.uiextensions.navigationdrawer.entry.DrawerSimpleItem;
import com.missionse.uiextensions.navigationdrawer.entry.DrawerSpinner;
import com.missionse.uiextensions.touchlistener.SwipeToDismissListener;

/**
 * Creates the two requisite Drawers, the left NavigationDrawer, and the right NotificationDrawer.
 */
public class LogisticsDrawerFactory {

	public static final int USER_ACCOUNT_ACTIONS = 001;
	public static final int DIVIDER = 002;
	public static final int NAVIGATION_HEADER = 100;
	public static final int MAP = 101;
	public static final int SUPPLY_LIST = 102;
	public static final int UPDATE_HISTORY = 103;

	private Context mContext;

	/**
	 * Creates a new LogisticsDrawerFactory.
	 * @param context the context to pass to created adapters
	 */
	public LogisticsDrawerFactory(final Context context) {
		mContext = context;
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
		DrawerConfiguration configuration = new DrawerConfiguration(DrawerType.LEFT, mContext);
		configuration.setDrawer(R.id.nav_drawer);
		container.addConfiguration(configuration);
	}

	private void createNotificationDrawer(final DrawerConfigurationContainer container) {
		DrawerConfiguration configuration = new DrawerConfiguration(DrawerType.RIGHT, mContext);
		configuration.setDrawer(R.id.notif_drawer);
		configuration.setShouldCloseOnSelect(false);
		configuration.setScrimColor(mContext.getResources().getColor(R.color.transparent_scrim));
		container.addConfiguration(configuration);
	}

	/**
	 * Sets up the Navigation menu items, which remain unchanged.
	 * @param adapter the adapter to add items to
	 * @param listener provides callbacks when spinner entries for the user account are clicked
	 */
	public void addNavigationMenuItems(final DrawerAdapter adapter, final OnItemSelectedListener listener) {
		List<String> userAccountSpinnerEntries = new ArrayList<String>();
		userAccountSpinnerEntries.add(mContext.getResources().getString(R.string.user_loggedout));
		userAccountSpinnerEntries.add(mContext.getResources().getString(R.string.user_login));

		List<DrawerItem> menu = new ArrayList<DrawerItem>();
		menu.add(DrawerSpinner.create(USER_ACCOUNT_ACTIONS, new ArrayAdapter<String>(mContext,
				R.layout.nav_drawer_spinner_entry, R.id.navheader_label, userAccountSpinnerEntries), listener));
		menu.add(DrawerPaddedDivider.create(DIVIDER));
		menu.add(DrawerHeader.create(NAVIGATION_HEADER, mContext.getResources().getString(R.string.drawer_navigation)));
		menu.add(DrawerDivider.create(DIVIDER));
		menu.add(DrawerSimpleItem.create(MAP, mContext.getResources().getString(R.string.drawer_map),
				R.drawable.places, true));
		menu.add(DrawerDivider.create(DIVIDER));
		menu.add(DrawerSimpleItem.create(SUPPLY_LIST, mContext.getResources().getString(R.string.drawer_supplylist),
				R.drawable.ebooks, true));
		menu.add(DrawerDivider.create(DIVIDER));
		menu.add(DrawerSimpleItem.create(UPDATE_HISTORY, mContext.getResources().getString(R.string.drawer_orders),
				R.drawable.analytics, true));
		menu.add(DrawerDivider.create(DIVIDER));

		for (DrawerItem item : menu) {
			adapter.add(item);
		}
	}

	/**
	 * Called when the DrawerActivity has finished configuring the drawers, and is now ready for post-actions.
	 * @param activity the parent activity
	 * @param activityListener listener, defined by the parent activity, that should be called back when we are finished
	 */
	public void onDrawerConfigurationComplete(final DrawerActivity activity,
			final SwipeToDismissListener activityListener) {

		DrawerSwipeToDismissTouchListener touchListener = new DrawerSwipeToDismissTouchListener(
				activity.getDrawerLayout(), activity.getRightDrawerList(), new SwipeToDismissListener() {
					@Override
					public boolean canDismiss(final int position) {
						return true;
					}

					@Override
					public void onDismiss(final ListView listView, final int[] positions) {
						for (int position : positions) {
							if (activity.getRightDrawerAdapter().getCount() > position) {
								activity.getRightDrawerAdapter().remove(
										activity.getRightDrawerAdapter().getItem(position));
							}
						}
						activity.getRightDrawerAdapter().notifyDataSetChanged();
						activityListener.onDismiss(listView, positions);
					}
				});

		activity.getRightDrawerList().setOnTouchListener(touchListener);
		activity.getRightDrawerList().setOnScrollListener(touchListener.makeScrollListener());

		TextView emptyView = (TextView) activity.findViewById(R.id.empty_notif_drawer);
		activity.getRightDrawerList().setEmptyView(emptyView);
	}
}
