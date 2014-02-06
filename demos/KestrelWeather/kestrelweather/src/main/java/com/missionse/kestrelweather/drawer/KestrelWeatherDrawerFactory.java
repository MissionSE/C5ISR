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

public class KestrelWeatherDrawerFactory {

    public static final int MAP_OVERVIEW = 101;
    public static final int CREATE_REPORT = 102;
    public static final int REPORT_SYNC = 103;
    public static final int REPORT_DATABASE = 104;

	private Context mContext;

	public KestrelWeatherDrawerFactory(final Context context) {
		mContext = context;
	}

	public DrawerConfigurationContainer createDrawers() {
		DrawerConfigurationContainer container = new DrawerConfigurationContainer(R.layout.activity_kestrel_weather,
				R.id.drawer_layout);

		createNavigationDrawer(container);
		createSyncDrawer(container);

		return container;
	}

	private void createNavigationDrawer(final DrawerConfigurationContainer container) {
		DrawerConfiguration configuration = new DrawerConfiguration(DrawerType.LEFT, mContext);
		configuration.setDrawer(R.id.navigation_drawer);
		container.addConfiguration(configuration);
	}

	private void createSyncDrawer(final DrawerConfigurationContainer container) {
		DrawerConfiguration configuration = new DrawerConfiguration(DrawerType.RIGHT, mContext);
		configuration.setDrawer(R.id.sync_drawer);
		//configuration.setShouldCloseOnSelect(false);
		container.addConfiguration(configuration);
	}

	public void addNavigationMenuItems(final DrawerAdapter adapter) {
		List<DrawerItem> menu = new ArrayList<DrawerItem>();

        menu.add(DrawerSimpleItem.create(MAP_OVERVIEW, mContext.getResources().getString(R.string.drawer_overview),
            0, true));
        menu.add(DrawerSimpleItem.create(CREATE_REPORT, mContext.getResources().getString(R.string.drawer_create_report),
            0, true));
        menu.add(DrawerSimpleItem.create(REPORT_SYNC, mContext.getResources().getString(R.string.drawer_sync_reports),
            0, true));
        menu.add(DrawerSimpleItem.create(REPORT_DATABASE, mContext.getResources().getString(R.string.drawer_report_database),
            0, true));

		for (DrawerItem item : menu) {
			adapter.add(item);
		}
	}
}
