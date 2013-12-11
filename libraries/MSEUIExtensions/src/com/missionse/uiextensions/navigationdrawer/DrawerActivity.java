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

/**
 * A base activity to be extended, provided basic operations and management of a NavigationDrawer.
 */
public abstract class DrawerActivity extends Activity {

	private DrawerConfigurationContainer mDrawerConfigurations;

	private CharSequence mDefaultTitle;
	private CharSequence mCurrentTitle;

	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawerLayout;

	private ListView mLeftDrawerList;
	private ListView mRightDrawerList;

	protected abstract DrawerConfigurationContainer getDrawerConfigurations();

	protected abstract void onNavigationItemSelected(int id);

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
	}

	private void createLeftDrawer(final DrawerConfiguration drawerConfiguration) {
		mLeftDrawerList = (ListView) findViewById(drawerConfiguration.getDrawer());
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
			public void onDrawerClosed(final View view) {
				getActionBar().setTitle(mCurrentTitle);
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(final View drawerView) {
				getActionBar().setTitle(mDefaultTitle);
				invalidateOptionsMenu();
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	private void createRightDrawer(final DrawerConfiguration drawerConfiguration) {
		mRightDrawerList = (ListView) findViewById(drawerConfiguration.getDrawer());
		mRightDrawerList.setAdapter(drawerConfiguration.getBaseAdapter());
		mRightDrawerList.setOnItemClickListener(new DrawerItemClickListener(mRightDrawerList));
		if (drawerConfiguration.getDivider() != null) {
			mRightDrawerList.setDivider(drawerConfiguration.getDivider());
			mRightDrawerList.setDividerHeight(1);
		}
		mDrawerLayout.setDrawerShadow(drawerConfiguration.getDrawerShadow(), Gravity.END);
	}

	protected void onDrawerConfigurationComplete() {

	}

	protected DrawerLayout getDrawerLayout() {
		return mDrawerLayout;
	}

	protected ListView getLeftDrawerList() {
		return mLeftDrawerList;
	}

	protected DrawerAdapter getLeftDrawerAdapter() {
		return (DrawerAdapter) mDrawerConfigurations.getLeftConfiguration().getBaseAdapter();
	}

	protected ListView getRightDrawerList() {
		return mRightDrawerList;
	}

	protected DrawerAdapter getRightDrawerAdapter() {
		return (DrawerAdapter) mDrawerConfigurations.getRightConfiguration().getBaseAdapter();
	}

	protected ActionBarDrawerToggle getDrawerToggle() {
		return mDrawerToggle;
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
		return mDrawerToggle.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(final int keyCode, final KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (mDrawerLayout.isDrawerOpen(mLeftDrawerList)) {
				mDrawerLayout.closeDrawer(mLeftDrawerList);
			} else {
				mDrawerLayout.openDrawer(mLeftDrawerList);
			}
			if (mDrawerLayout.isDrawerOpen(mRightDrawerList)) {
				mDrawerLayout.closeDrawer(mRightDrawerList);
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

		if (mDrawerLayout.isDrawerOpen(drawerList)) {
			mDrawerLayout.closeDrawer(drawerList);
		}
	}

	@Override
	public void setTitle(final CharSequence title) {
		mCurrentTitle = title;
		getActionBar().setTitle(mCurrentTitle);
	}
}
