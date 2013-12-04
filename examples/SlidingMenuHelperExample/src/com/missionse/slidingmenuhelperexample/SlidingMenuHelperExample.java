package com.missionse.slidingmenuhelperexample;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.SimpleDrawerListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.missionse.slidingmenu.OnMenuClickListener;
import com.missionse.slidingmenu.SlidingMenuHelper;
import com.missionse.slidingmenu.SlidingMenuHelper.MenuType;

/**
 * Acts as the entry point to the SlidingMenuHelperExample. Uses the SlidingMenuHelper to create various types of
 * SlidingMenus.
 * 
 * In a general design sense, SlidingMenus should be used for option lists (such as a filter selection fragment), or for
 * navigation within a fragment (such as a list of headers to each page of a ViewPager). The NavigationDrawer should be
 * used to display overall application navigation (such as starting new activities or displaying new Fragments), or
 * administrative items (such as account information, overall application settings, or new item counts).
 */
public class SlidingMenuHelperExample extends Activity {

	private SlidingMenuHelper mSlidingMenuHelper;
	private ArrayAdapter<String> mTranscriptAdapter;
	private DrawerLayout mDrawerLayout;
	private ListView mAdminMenu;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sliding_menu_helper_example);

		final ListView transcriptView = (ListView) findViewById(R.id.transcript);
		mTranscriptAdapter = new ArrayAdapter<String>(this, R.layout.transcript_item);
		transcriptView.setAdapter(mTranscriptAdapter);

		mSlidingMenuHelper = new SlidingMenuHelper(this);

		createLeftMenu();

		createRightCustomMenu();

		createNavigationDrawer();
	}

	private void createLeftMenu() {
		/**
		 * Creating a generic navigation menu with a shadow and a title, built with a simple List of Strings. Also
		 * setting touch mode to FULLSCREEN, so that a gesture from anywhere will make it appear. We do this because we
		 * do not want it to collide with the NavigationDrawer provided by the Android OS. It also closes when an item
		 * is clicked.
		 */
		final List<String> leftMenuEntries = new ArrayList<String>();
		leftMenuEntries.add("One");
		leftMenuEntries.add("Two");
		mSlidingMenuHelper.createSimpleMenu(MenuType.LEFT, leftMenuEntries, new OnMenuClickListener() {
			@Override
			public void onMenuClick(final int position) {
				mTranscriptAdapter.add("Left menu entry [" + position + ", " + leftMenuEntries.get(position)
						+ "] selected");
				mSlidingMenuHelper.getLeftMenuBase().showContent();
			}
		});
		mSlidingMenuHelper.getLeftMenu().setTitle("Navigation Menu");
		mSlidingMenuHelper.getLeftMenu().enableShadow();

		/**
		 * We want the left menu to appear with a gesture anywhere on the screen, but since we also have a
		 * NavigationDrawer, we will disallow touches on the margin.
		 */
		mSlidingMenuHelper.getLeftMenu().setTouchMode(SlidingMenu.TOUCHMODE_FULLSCREEN, true);
		mSlidingMenuHelper.commit();
	}

	private void createRightCustomMenu() {
		// Creating a menu without a shadow or a title, built with an array adapter for custom line items.
		List<String> rightMenuEntries = new ArrayList<String>();
		rightMenuEntries.add("Three");
		rightMenuEntries.add("Four");
		CustomAdapter rightMenuAdapter = new CustomAdapter(this, R.layout.custom_menu_entry, rightMenuEntries);
		mSlidingMenuHelper.createCustomMenu(MenuType.RIGHT, rightMenuAdapter, null).commit();
	}

	private void createNavigationDrawer() {
		// Creating a generic "admin" menu.
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerLayout.setDrawerListener(new SimpleDrawerListener() {
			@Override
			public void onDrawerSlide(final View arg0, final float arg1) {
				mSlidingMenuHelper.getLeftMenuBase().setSlidingEnabled(false);
				mSlidingMenuHelper.getRightMenuBase().setSlidingEnabled(false);
			}

			@Override
			public void onDrawerStateChanged(final int state) {
				if (state == DrawerLayout.STATE_IDLE && !mDrawerLayout.isDrawerVisible(mAdminMenu)) {
					mSlidingMenuHelper.getLeftMenuBase().setSlidingEnabled(true);
					mSlidingMenuHelper.getRightMenuBase().setSlidingEnabled(true);
				}
			}
		});

		final List<String> adminMenuEntries = new ArrayList<String>();
		adminMenuEntries.add("Add To Right Menu");
		adminMenuEntries.add("Switch to Notification Menu");
		adminMenuEntries.add("Switch to Custom Menu");
		mAdminMenu = (ListView) findViewById(R.id.admin_drawer);
		mAdminMenu.setAdapter(new ArrayAdapter<String>(this, R.layout.nav_drawer_item, adminMenuEntries));
		mAdminMenu.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
				mTranscriptAdapter.add("Drawer entry [" + position + ", " + adminMenuEntries.get(position)
						+ "] selected");
				if (adminMenuEntries.get(position).equals("Add To Right Menu")) {
					mSlidingMenuHelper.getRightMenu().getAdapter().add("New entry");
				} else if (adminMenuEntries.get(position).equals("Switch to Notification Menu")) {
					createRightNotificationMenu();
				} else if (adminMenuEntries.get(position).equals("Switch to Custom Menu")) {
					createRightCustomMenu();
				}
			}
		});
	}

	private void createRightNotificationMenu() {

	}

	/**
	 * Custom adapter for the right SlidingMenu.
	 */
	private class CustomAdapter extends ArrayAdapter<String> {
		private Context mContext;
		private int mResource;
		private List<String> mItems;

		public CustomAdapter(final Context context, final int resource, final List<String> items) {
			super(context, resource, items);
			mContext = context;
			mResource = resource;
			mItems = items;
		}

		@Override
		public View getView(final int position, View convertView, final ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(mResource, null);
			}

			String itemLabel = mItems.get(position);
			if (itemLabel != null) {
				ImageView icon = (ImageView) convertView.findViewById(R.id.menu_icon);
				CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.menu_entry);

				if (icon != null && checkBox != null) {
					icon.setImageResource(R.drawable.ic_launcher);
					checkBox.setText(itemLabel);
					checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
							mTranscriptAdapter.add("Right menu entry [" + position + ", " + mItems.get(position)
									+ "] selected, checkbox is now [" + isChecked + "]");
						}
					});
				}
			}
			return convertView;
		}
	}
}
