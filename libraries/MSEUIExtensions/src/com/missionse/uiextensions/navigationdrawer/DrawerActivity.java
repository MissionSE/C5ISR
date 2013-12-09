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
			mLeftDrawerList = (ListView) findViewById(leftDrawerConfiguration.getDrawer());
			mLeftDrawerList.setAdapter(leftDrawerConfiguration.getBaseAdapter());
			mLeftDrawerList.setOnItemClickListener(new LeftDrawerItemClickListener());
			mDrawerLayout.setDrawerShadow(leftDrawerConfiguration.getDrawerShadow(), Gravity.START);

			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setHomeButtonEnabled(true);

			mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, getDrawerIcon(),
					leftDrawerConfiguration.getDrawerOpenDesc(), leftDrawerConfiguration.getDrawerCloseDesc()) {
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

		DrawerConfiguration rightDrawerConfiguration = mDrawerConfigurations.getRightConfiguration();
		if (rightDrawerConfiguration != null) {
			mRightDrawerList = (ListView) findViewById(rightDrawerConfiguration.getDrawer());
			mRightDrawerList.setAdapter(rightDrawerConfiguration.getBaseAdapter());
			mRightDrawerList.setOnItemClickListener(new RightDrawerItemClickListener());
			mDrawerLayout.setDrawerShadow(rightDrawerConfiguration.getDrawerShadow(), Gravity.END);
		}
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
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	protected DrawerLayout getDrawerLayout() {
		return mDrawerLayout;
	}

	protected ActionBarDrawerToggle getDrawerToggle() {
		return mDrawerToggle;
	}

	/**
	 * Listener that selects an item and closes the left drawer if necessary.
	 */
	private class LeftDrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
			selectLeftItem(position);
		}
	}

	/**
	 * Listener that selects an item and closes the right drawer if necessary.
	 */
	private class RightDrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
			selectRightItem(position);
		}
	}

	/**
	 * Selects an item based on a position.
	 * @param position the position
	 */
	public void selectLeftItem(final int position) {
		DrawerItem selectedItem = mDrawerConfigurations.getLeftConfiguration().getNavItems()[position];

		onNavigationItemSelected(selectedItem.getId());
		mLeftDrawerList.setItemChecked(position, true);

		if (selectedItem.willChangeActionBarTitle()) {
			setTitle(selectedItem.getActionBarTitle());
		}

		if (this.mDrawerLayout.isDrawerOpen(mLeftDrawerList)) {
			mDrawerLayout.closeDrawer(mLeftDrawerList);
		}
	}

	/**
	 * Selects an item based on a position.
	 * @param position the position
	 */
	public void selectRightItem(final int position) {
		DrawerItem selectedItem = mDrawerConfigurations.getRightConfiguration().getNavItems()[position];

		onNavigationItemSelected(selectedItem.getId());
		mRightDrawerList.setItemChecked(position, true);

		if (selectedItem.willChangeActionBarTitle()) {
			setTitle(selectedItem.getActionBarTitle());
		}

		if (this.mDrawerLayout.isDrawerOpen(mRightDrawerList)) {
			mDrawerLayout.closeDrawer(mRightDrawerList);
		}
	}

	@Override
	public void setTitle(final CharSequence title) {
		mCurrentTitle = title;
		getActionBar().setTitle(mCurrentTitle);
	}
}
