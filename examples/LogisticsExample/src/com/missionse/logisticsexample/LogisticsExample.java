package com.missionse.logisticsexample;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;

import com.missionse.logisticsexample.drawer.LogisticsDrawerFactory;
import com.missionse.logisticsexample.map.LogisticsMap;
import com.missionse.logisticsexample.map.MapViewerFragment;
import com.missionse.uiextensions.navigationdrawer.DrawerActivity;
import com.missionse.uiextensions.navigationdrawer.compatibility.DrawerSwipeToDismissTouchListener;
import com.missionse.uiextensions.navigationdrawer.configuration.DrawerConfigurationContainer;
import com.missionse.uiextensions.touchlistener.SwipeToDismissListener;

/**
 * Main entry point to the Logistics application. Instantiates the two drawers, and loads the initial fragment into the
 * content space.
 */
public class LogisticsExample extends DrawerActivity {
	private LogisticsDrawerFactory mDrawerFactory;
	private LogisticsMap mLogisticsMap;

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

		FragmentManager fragmentManager = getFragmentManager();
		MapViewerFragment mapViewerFragment = (MapViewerFragment) fragmentManager.findFragmentByTag("map");
		if (mapViewerFragment == null) {
			mapViewerFragment = new MapViewerFragment();
			mapViewerFragment.setMapLoadedListener(mLogisticsMap);
			fragmentManager.beginTransaction().add(R.id.content, mapViewerFragment, "map").commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.logistics_example, menu);
		return true;
	}

	@Override
	protected DrawerConfigurationContainer getDrawerConfigurations() {
		mDrawerFactory.setUserAccountActionsListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {

			}

			@Override
			public void onNothingSelected(final AdapterView<?> parent) {
			}
		});

		return mDrawerFactory.createDrawers();
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
							mDrawerFactory.getRightDrawerAdapter().remove(
									mDrawerFactory.getRightDrawerAdapter().getItem(position));
						}
						mDrawerFactory.getRightDrawerAdapter().notifyDataSetChanged();
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
