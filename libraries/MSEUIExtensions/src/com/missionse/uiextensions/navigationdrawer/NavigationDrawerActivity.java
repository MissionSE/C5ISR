package com.missionse.uiextensions.navigationdrawer;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.missionse.uiextensions.R;

/**
 * A base activity to be extended, provided basic operations and management of a NavigationDrawer.
 */
public abstract class NavigationDrawerActivity extends Activity {

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;

	private ListView mDrawerList;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	private NavigationDrawerConfiguration mNavigationDrawerConfiguration;

	protected abstract NavigationDrawerConfiguration getNavigationDrawerConfiguration();

	protected abstract void onNavigationItemSelected(int id);

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mNavigationDrawerConfiguration = getNavigationDrawerConfiguration();

		setContentView(mNavigationDrawerConfiguration.getMainLayout());

		mDrawerTitle = getTitle();
		mTitle = mDrawerTitle;

		mDrawerLayout = (DrawerLayout) findViewById(mNavigationDrawerConfiguration.getDrawerLayoutId());
		mDrawerList = (ListView) findViewById(mNavigationDrawerConfiguration.getLeftDrawerId());
		mDrawerList.setAdapter(mNavigationDrawerConfiguration.getBaseAdapter());
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		initDrawerShadow();

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, getDrawerIcon(),
				mNavigationDrawerConfiguration.getDrawerOpenDesc(), mNavigationDrawerConfiguration.getDrawerCloseDesc()) {
			@Override
			public void onDrawerClosed(final View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(final View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	protected void initDrawerShadow() {
		mDrawerLayout.setDrawerShadow(mNavigationDrawerConfiguration.getDrawerShadow(), GravityCompat.START);
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
		if (mNavigationDrawerConfiguration.getActionMenuItemsToHideWhenDrawerOpen() != null) {
			boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
			for (int iItem : mNavigationDrawerConfiguration.getActionMenuItemsToHideWhenDrawerOpen()) {
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
			if (this.mDrawerLayout.isDrawerOpen(this.mDrawerList)) {
				this.mDrawerLayout.closeDrawer(this.mDrawerList);
			} else {
				this.mDrawerLayout.openDrawer(this.mDrawerList);
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
	 * Listener that selects and item and closes the drawer if necessary.
	 */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
			selectItem(position);
		}
	}

	/**
	 * Selects an item based on a position.
	 * @param position the position
	 */
	public void selectItem(final int position) {
		NavigationDrawerItem selectedItem = mNavigationDrawerConfiguration.getNavItems()[position];

		onNavigationItemSelected(selectedItem.getId());
		mDrawerList.setItemChecked(position, true);

		if (selectedItem.willChangeActionBarTitle()) {
			setTitle(selectedItem.getActionBarTitle());
		}

		if (this.mDrawerLayout.isDrawerOpen(this.mDrawerList)) {
			mDrawerLayout.closeDrawer(mDrawerList);
		}
	}

	@Override
	public void setTitle(final CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}
}
