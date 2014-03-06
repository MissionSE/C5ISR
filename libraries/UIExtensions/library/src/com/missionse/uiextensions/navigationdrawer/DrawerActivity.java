package com.missionse.uiextensions.navigationdrawer;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.missionse.uiextensions.R;
import com.missionse.uiextensions.navigationdrawer.configuration.DrawerConfiguration;
import com.missionse.uiextensions.navigationdrawer.configuration.DrawerConfigurationContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * A base activity to be extended, provided basic operations and management of a NavigationDrawer.
 */
public abstract class DrawerActivity extends Activity {

	private DrawerConfigurationContainer mDrawerConfigurations;

	private CharSequence mDefaultTitle;
	private CharSequence mCurrentTitle;

	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawerLayout;

	private View mLeftDrawer;
	private ListView mLeftDrawerList;
	private View mRightDrawer;
	private ListView mRightDrawerList;

	private List<DrawerLayout.DrawerListener> mDrawerEventListeners = new ArrayList<DrawerLayout.DrawerListener>();

	protected abstract DrawerConfigurationContainer getDrawerConfigurations();

	protected abstract void onNavigationItemSelected(int id);

	protected abstract void onDrawerConfigurationComplete();

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mDrawerConfigurations = getDrawerConfigurations();

		setContentView(mDrawerConfigurations.getMainLayout());
		mDefaultTitle = getTitle();
		mCurrentTitle = mDefaultTitle;

		mDrawerLayout = (DrawerLayout) findViewById(mDrawerConfigurations.getDrawerLayout());

		DrawerConfiguration leftDrawerConfiguration = mDrawerConfigurations.getLeftConfiguration();
		if (leftDrawerConfiguration != null) {
			createLeftDrawer(leftDrawerConfiguration);
		}

		DrawerConfiguration rightDrawerConfiguration = mDrawerConfigurations.getRightConfiguration();
		if (rightDrawerConfiguration != null) {
			createRightDrawer(rightDrawerConfiguration);
		}

		onDrawerConfigurationComplete();

		mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
			@Override
			public void onDrawerSlide(final View drawerView, final float slideOffset) {
				for (DrawerLayout.DrawerListener listener : mDrawerEventListeners) {
					listener.onDrawerSlide(drawerView, slideOffset);
				}
			}

			@Override
			public void onDrawerOpened(final View drawerView) {
				for (DrawerLayout.DrawerListener listener : mDrawerEventListeners) {
					listener.onDrawerOpened(drawerView);
				}
			}

			@Override
			public void onDrawerClosed(final View drawerView) {
				for (DrawerLayout.DrawerListener listener : mDrawerEventListeners) {
					listener.onDrawerClosed(drawerView);
				}
			}

			@Override
			public void onDrawerStateChanged(final int newState) {
				for (DrawerLayout.DrawerListener listener : mDrawerEventListeners) {
					listener.onDrawerStateChanged(newState);
				}
			}
		});
	}

	private void createLeftDrawer(final DrawerConfiguration drawerConfiguration) {
		mLeftDrawer = findViewById(drawerConfiguration.getDrawer());
		mLeftDrawerList = (ListView) findViewById(drawerConfiguration.getDrawerList());
		mLeftDrawerList.setAdapter(drawerConfiguration.getBaseAdapter());
		mLeftDrawerList.setOnItemClickListener(new DrawerItemClickListener(mLeftDrawerList));
		if (drawerConfiguration.getDivider() != null) {
			mLeftDrawerList.setDivider(drawerConfiguration.getDivider());
			mLeftDrawerList.setDividerHeight(1);
		}
		mDrawerLayout.setDrawerShadow(drawerConfiguration.getDrawerShadow(), Gravity.START);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, getDrawerIcon(),
				drawerConfiguration.getDrawerOpenDesc(), drawerConfiguration.getDrawerCloseDesc()) {
			@Override
			public void onDrawerSlide(final View drawerView, final float slideOffset) {
				super.onDrawerSlide(drawerView, slideOffset);
				if (drawerView == mDrawerLayout.getChildAt(1)) {
					mDrawerLayout.setScrimColor(mDrawerConfigurations.getLeftConfiguration().getScrimColor());
				} else if (drawerView == mDrawerLayout.getChildAt(2)) {
					mDrawerLayout.setScrimColor(mDrawerConfigurations.getRightConfiguration().getScrimColor());
				}
			}

			@Override
			public void onDrawerClosed(final View drawerView) {
				super.onDrawerClosed(drawerView);
				getActionBar().setTitle(mCurrentTitle);
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(final View drawerView) {
				super.onDrawerOpened(drawerView);
				getActionBar().setTitle(mDefaultTitle);
				invalidateOptionsMenu();
			}
		};

		mDrawerEventListeners.add(mDrawerToggle);
	}

	private void createRightDrawer(final DrawerConfiguration drawerConfiguration) {
		mRightDrawer = findViewById(drawerConfiguration.getDrawer());
		mRightDrawerList = (ListView) findViewById(drawerConfiguration.getDrawerList());
		mRightDrawerList.setAdapter(drawerConfiguration.getBaseAdapter());
		mRightDrawerList.setOnItemClickListener(new DrawerItemClickListener(mRightDrawerList));
		if (drawerConfiguration.getDivider() != null) {
			mRightDrawerList.setDivider(drawerConfiguration.getDivider());
			mRightDrawerList.setDividerHeight(1);
		}
		mDrawerLayout.setDrawerShadow(drawerConfiguration.getDrawerShadow(), Gravity.END);
	}

	/**
	 * Adds a DrawerListener to be notified of drawer events.
	 * @param listener the listener to be notified
	 */
	public void addDrawerEventListener(final DrawerLayout.DrawerListener listener) {
		mDrawerEventListeners.add(listener);
	}

	/**
	 * Removes a DrawerListener to be notified of drawer events.
	 * @param listener the listener to no longer be notified
	 */
	public void removeDrawerEventListener(final DrawerLayout.DrawerListener listener) {
		mDrawerEventListeners.remove(listener);
	}

	/**
	 * Retrieves the underlying main root layout, the DrawerLayout, used to create the NavigationDrawers.
	 * @return the DrawerLayout
	 */
	public DrawerLayout getDrawerLayout() {
		return mDrawerLayout;
	}

	/**
	 * Retrieves the left Drawer view.
	 * @return the left Drawer view
	 */
	public View getLeftDrawer() {
		return mLeftDrawer;
	}

	/**
	 * Retrieves the right Drawer view.
	 * @return the right Drawer view
	 */
	public View getRightDrawer() {
		return mRightDrawer;
	}

	/**
	 * Retrieves the ListView created for the left drawer.
	 * @return the ListView
	 */
	public ListView getLeftDrawerList() {
		return mLeftDrawerList;
	}

	/**
	 * Retrieves the adapter used to populate the ListView comprising the left drawer.
	 * @return the ArrayAdapter
	 */
	public DrawerAdapter getLeftDrawerAdapter() {
		return (DrawerAdapter) mDrawerConfigurations.getLeftConfiguration().getBaseAdapter();
	}

	/**
	 * Retrieves the ListView created for the right drawer.
	 * @return the ListView
	 */
	public ListView getRightDrawerList() {
		return mRightDrawerList;
	}

	/**
	 * Retrieves the adapter used to populate the ListView comprising the right drawer.
	 * @return the ArrayAdapter
	 */
	public DrawerAdapter getRightDrawerAdapter() {
		return (DrawerAdapter) mDrawerConfigurations.getRightConfiguration().getBaseAdapter();
	}

	protected int getDrawerIcon() {
		return R.drawable.ic_drawer;
	}

	@Override
	protected void onPostCreate(final Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(final Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onPrepareOptionsMenu(final Menu menu) {
		if (mDrawerConfigurations.getLeftConfiguration() != null
				&& mDrawerConfigurations.getLeftConfiguration().getActionMenuItemsToHideWhenDrawerOpen() != null) {
			boolean drawerOpen = mDrawerLayout.isDrawerOpen(mLeftDrawerList);
			for (int iItem : mDrawerConfigurations.getLeftConfiguration().getActionMenuItemsToHideWhenDrawerOpen()) {
				menu.findItem(iItem).setVisible(!drawerOpen);
			}
		} else if (mDrawerConfigurations.getRightConfiguration() != null
				&& mDrawerConfigurations.getRightConfiguration().getActionMenuItemsToHideWhenDrawerOpen() != null) {
			boolean drawerOpen = mDrawerLayout.isDrawerOpen(mRightDrawerList);
			for (int iItem : mDrawerConfigurations.getRightConfiguration().getActionMenuItemsToHideWhenDrawerOpen()) {
				menu.findItem(iItem).setVisible(!drawerOpen);
			}
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(final int keyCode, final KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (mDrawerLayout.isDrawerOpen(mLeftDrawer)) {
				mDrawerLayout.closeDrawer(mLeftDrawer);
			} else {
				mDrawerLayout.openDrawer(mLeftDrawer);
			}
			if (mDrawerLayout.isDrawerOpen(mRightDrawer)) {
				mDrawerLayout.closeDrawer(mRightDrawer);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Listener that selects an item and closes the left drawer if necessary.
	 */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		private ListView mDrawerList;

		public DrawerItemClickListener(final ListView drawerList) {
			mDrawerList = drawerList;
		}

		@Override
		public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
			selectItem(position, mDrawerList);
		}
	}

	/**
	 * Selects an item in a Drawer.
	 * @param position the position
	 * @param drawerList the ListView that on which the selected item exists
	 */
	public void selectItem(final int position, final ListView drawerList) {
		DrawerItem selectedItem = (DrawerItem) drawerList.getAdapter().getItem(position);

		onNavigationItemSelected(selectedItem.getId());
		drawerList.setItemChecked(position, true);

		if (selectedItem.willChangeActionBarTitle()) {
			setTitle(selectedItem.getActionBarTitle());
		}

		if ((drawerList == mLeftDrawerList && mDrawerConfigurations.getLeftConfiguration().shouldCloseOnSelect())) {
			if (mDrawerLayout.isDrawerOpen(mLeftDrawer)) {
				mDrawerLayout.closeDrawer(mLeftDrawer);
			}
		} else if (drawerList == mRightDrawerList && mDrawerConfigurations.getRightConfiguration()
						.shouldCloseOnSelect()) {
			if (mDrawerLayout.isDrawerOpen(mRightDrawer)) {
				mDrawerLayout.closeDrawer(mRightDrawer);
			}
		}

	}

	@Override
	public void setTitle(final CharSequence title) {
		mCurrentTitle = title;
		getActionBar().setTitle(mCurrentTitle);
	}
}
