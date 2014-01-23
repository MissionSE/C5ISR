package com.missionse.slidingmenu;

import java.util.List;

import android.app.Activity;
import android.widget.ArrayAdapter;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * Provides an abstracted, easy-to-use API based on the third-party SlidingMenu library.
 */
public class SlidingMenuHelper {

	/**
	 * Enumeration denoting the side on which the menu should appear.
	 */
	public enum MenuType {
		RIGHT, LEFT
	}

	private Activity mActivity;

	private SlidingMenu mLeftMenu;
	private MenuFragment mLeftMenuFragment;
	private boolean mLeftMenuSet;

	private SlidingMenu mRightMenu;
	private MenuFragment mRightMenuFragment;
	private boolean mRightMenuSet;

	/**
	 * Creates a SlidingMenuHelper.
	 * @param parent the parent activity creating this helper
	 */
	public SlidingMenuHelper(final Activity parent) {
		mActivity = parent;
	}

	/**
	 * Retrieves the menu fragment set to populate the LEFT side.
	 * @return the left menu fragment
	 */
	public MenuFragment getLeftMenu() {
		return mLeftMenuFragment;
	}

	/**
	 * Retrieves the SlidingMenu that makes up the core of the LEFT menu.
	 * @return the SlidingMenu used on the left
	 */
	public SlidingMenu getLeftMenuBase() {
		return mLeftMenu;
	}

	/**
	 * Retrieves the menu fragment set to populate the RIGHT side.
	 * @return the right menu fragment
	 */
	public MenuFragment getRightMenu() {
		return mRightMenuFragment;
	}

	/**
	 * Retrieves the SlidingMenu that makes up the core of the RIGHT menu.
	 * @return the SlidingMenu used on the right
	 */
	public SlidingMenu getRightMenuBase() {
		return mRightMenu;
	}

	/**
	 * Creates an untitled, simple menu, built from a List of Strings.
	 * @param type the type of menu to create (LEFT/RIGHT)
	 * @param entries a list of entries with which to populate the menu list
	 * @param listener a listener to be called back when a menu item is clicked, can be null
	 * @return the SlidingMenuHelper
	 */
	public SlidingMenuHelper createSimpleMenu(final MenuType type, final List<String> entries,
			final OnMenuClickListener listener) {

		if (MenuType.LEFT == type) {
			createLeftMenu(DefaultMenuFragment.class);
			((DefaultMenuFragment) mLeftMenuFragment).setMenuEntries(entries);
			if (listener != null) {
				mLeftMenuFragment.registerListener(listener);
			}
		} else if (MenuType.RIGHT == type) {
			createRightMenu(DefaultMenuFragment.class);
			((DefaultMenuFragment) mRightMenuFragment).setMenuEntries(entries);
			if (listener != null) {
				mRightMenuFragment.registerListener(listener);
			}
		}
		return this;
	}

	/**
	 * Creates an untitled menu, built from a provided adapter. NOTE: If the Views you plan to place in the menu via
	 * your adapter are focusable (such as a Checkbox), then the listener provided via this method will NOT be invoked
	 * (by design of the Android OS). In this case, you must provide your own mechanism for listening for item selection
	 * (likely in the adapter itself).
	 * @param type the type of menu (LEFT/RIGHT)
	 * @param adapter the array adapter to use when populating the list
	 * @param listener a listener to be called back when a menu item is clicked, can be null
	 * @return this helper
	 */
	public SlidingMenuHelper createCustomMenu(final MenuType type, final ArrayAdapter<?> adapter,
			final OnMenuClickListener listener) {

		if (MenuType.LEFT == type) {
			createLeftMenu(CustomMenuFragment.class);
			((CustomMenuFragment) mLeftMenuFragment).setAdapter(adapter);

			if (listener != null) {
				mLeftMenuFragment.registerListener(listener);
			}
		} else if (MenuType.RIGHT == type) {
			createRightMenu(CustomMenuFragment.class);
			((CustomMenuFragment) mRightMenuFragment).setAdapter(adapter);

			if (listener != null) {
				mRightMenuFragment.registerListener(listener);
			}
		}
		return this;
	}

	/**
	 * Creates an untitled notification menu, built from the provided adapter. Notification menu items can be swiped
	 * away to the left or right to be dismissed, in addition to normal selection. NOTE: If the Views you plan to place
	 * in the menu via your adapter are focusable (such as a Checkbox), then the listener provided via this method will
	 * NOT be invoked (by design of the Android OS). In this case, you must provide your own mechanism for listening for
	 * item selection (likely in the adapter itself).
	 * @param type the type of menu (LEFT/RIGHT)
	 * @param entries list of String entries to initially populate the menu with
	 * @param listener a listener to be called back when a menu item is clicked or dismissed, can be null
	 * @return this helper
	 */
	public SlidingMenuHelper createNotificationMenu(final MenuType type, final List<String> entries,
			final OnNotificationActionListener listener) {

		if (MenuType.LEFT == type) {
			createLeftMenu(NotificationMenuFragment.class);
			((NotificationMenuFragment) mLeftMenuFragment).setMenuEntries(entries);

			//			if (listener != null) {
			//				mLeftMenuFragment.registerListener(listener);
			//			}
		} else if (MenuType.RIGHT == type) {
			createRightMenu(NotificationMenuFragment.class);
			((NotificationMenuFragment) mRightMenuFragment).setMenuEntries(entries);

			//			if (listener != null) {
			//				((NotificationMenuFragment) mRightMenuFragment).registerListener(listener);
			//			}
		}
		return this;
	}

	private void createLeftMenu(final Class<?> fragmentClass) {
		mLeftMenuSet = false;

		mLeftMenu = new SlidingMenu(mActivity);

		mLeftMenu.setMode(SlidingMenu.LEFT);
		setCommonDefaultSettings(mLeftMenu);
		mLeftMenu.setShadowDrawable(R.drawable.shadow_left);
		mLeftMenu.setMenu(R.layout.left_menu);

		try {
			mLeftMenuFragment = (MenuFragment) fragmentClass.newInstance();
			mLeftMenuFragment.setSlidingMenu(mLeftMenu);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private void createRightMenu(final Class<?> fragmentClass) {
		mRightMenuSet = false;

		mRightMenu = new SlidingMenu(mActivity);

		mRightMenu.setMode(SlidingMenu.RIGHT);
		setCommonDefaultSettings(mRightMenu);
		mRightMenu.setShadowDrawable(R.drawable.shadow_right);
		mRightMenu.setMenu(R.layout.right_menu);

		try {
			mRightMenuFragment = (MenuFragment) fragmentClass.newInstance();
			mRightMenuFragment.setSlidingMenu(mRightMenu);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private void setCommonDefaultSettings(final SlidingMenu menu) {
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.setBehindWidthRes(R.dimen.default_menu_width);
		menu.setShadowWidth(0);
	}

	/**
	 * Commits any menu configuration via fragment transactions. Can be run more than once, and should always be run at
	 * least once after after a create* call is made.
	 */
	public void commit() {
		if (mLeftMenu != null && !mLeftMenuSet) {
			mLeftMenu.attachToActivity(mActivity, SlidingMenu.SLIDING_CONTENT);
			mActivity.getFragmentManager().beginTransaction().replace(R.id.left_menu, mLeftMenuFragment).commit();
			mLeftMenuSet = true;
		}
		if (mRightMenu != null && !mRightMenuSet) {
			mRightMenu.attachToActivity(mActivity, SlidingMenu.SLIDING_CONTENT);
			mActivity.getFragmentManager().beginTransaction().replace(R.id.right_menu, mRightMenuFragment).commit();
			mRightMenuSet = true;
		}
	}
}
