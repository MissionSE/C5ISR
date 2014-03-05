package com.missionse.kestrelweather.drawer;

import android.content.Context;

import com.missionse.kestrelweather.R;
import com.missionse.uiextensions.navigationdrawer.DrawerAdapter;
import com.missionse.uiextensions.navigationdrawer.DrawerItem;
import com.missionse.uiextensions.navigationdrawer.configuration.DrawerConfiguration;
import com.missionse.uiextensions.navigationdrawer.configuration.DrawerConfigurationContainer;
import com.missionse.uiextensions.navigationdrawer.configuration.DrawerConfigurationContainer.DrawerType;
import com.missionse.uiextensions.navigationdrawer.entry.DrawerSimpleItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates the navigation drawer for the Kestrel Weather activity.
 */
public class KestrelWeatherDrawerFactory {

	public static final int MAP_OVERVIEW = 101;
	public static final int CREATE_REPORT = 102;
	public static final int REPORT_SYNC = 103;
	public static final int REPORT_DATABASE = 104;

	private Context mContext;

	/**
	 * Constructs a new KestrelWeatherDrawerFactory.
	 * @param context the parent activity's context
	 */
	public KestrelWeatherDrawerFactory(final Context context) {
		mContext = context;
	}

	/**
	 * Creates the navigation drawers.
	 * @return a configuration container, holding configuration information for both drawers to be instantiated
	 */
	public DrawerConfigurationContainer createDrawers() {
		DrawerConfigurationContainer container = new DrawerConfigurationContainer(R.layout.activity_kestrel_weather,
				R.id.drawer_layout);

		createNavigationDrawer(container);

		return container;
	}

	private void createNavigationDrawer(final DrawerConfigurationContainer container) {
		DrawerConfiguration configuration = new DrawerConfiguration(mContext, DrawerType.LEFT, R.id.navigation_drawer,
				R.id.navigation_drawer_list);
		container.addConfiguration(configuration);
	}

	/**
	 * Adds the navigation menu items to the supplied adapter.
	 * @param adapter the adapter to which to add menu items
	 */
	public void addNavigationMenuItems(final DrawerAdapter adapter) {
		List<DrawerItem> menu = new ArrayList<DrawerItem>();

		DrawerSimpleItem overviewItem = DrawerSimpleItem.create(MAP_OVERVIEW, mContext.getResources().getString(R.string.drawer_overview),
				0, true);
		overviewItem.setBackgroundDrawable(R.drawable.drawer_overview_background_selector, mContext);
		overviewItem.setTextColorStateList(R.drawable.drawer_overview_text_selector, mContext);
		menu.add(overviewItem);
		DrawerSimpleItem createItem = DrawerSimpleItem.create(CREATE_REPORT, mContext.getResources().
				getString(R.string.drawer_create_report), 0, true);
		createItem.setBackgroundDrawable(R.drawable.drawer_create_background_selector, mContext);
		createItem.setTextColorStateList(R.drawable.drawer_create_text_selector, mContext);
		menu.add(createItem);
		DrawerSimpleItem syncItem = DrawerSimpleItem.create(REPORT_SYNC, mContext.getResources().getString(R.string.drawer_sync_reports),
				0, true);
		syncItem.setBackgroundDrawable(R.drawable.drawer_sync_background_selector, mContext);
		syncItem.setTextColorStateList(R.drawable.drawer_sync_text_selector, mContext);
		menu.add(syncItem);
		DrawerSimpleItem databaseItem = DrawerSimpleItem.create(REPORT_DATABASE, mContext.getResources().
				getString(R.string.drawer_report_database), 0, true);
		databaseItem.setBackgroundDrawable(R.drawable.drawer_database_background_selector, mContext);
		databaseItem.setTextColorStateList(R.drawable.drawer_database_text_selector, mContext);
		menu.add(databaseItem);

		for (DrawerItem item : menu) {
			adapter.add(item);
		}
	}
}
