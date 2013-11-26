package com.missionse.slidingmenu;

import java.util.List;

import android.app.Activity;
import android.widget.ArrayAdapter;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * Provides an abstracted, easy-to-use API based on the third-party SlidingMenu library.
 */
public class SlidingMenuHelper {

	private Activity mActivity;

	private SlidingMenu mLeftMenu;
	private MenuFragment mLeftMenuFragment;
	private SlidingMenu mRightMenu;
	private MenuFragment mRightMenuFragment;

	/**
	 * Creates a SlidingMenuHelper.
	 * @param parent the parent activity creating this helper
	 */
	public SlidingMenuHelper(final Activity parent) {
		mActivity = parent;
	}

	/**
	 * Creates a menu given various options.
	 * @param type the type of menu (LEFT/RIGHT)
	 * @param adapter the array adapter to use when populating the list
	 * @param title an optional title to be displayed at the top of the menu, can be null
	 * @param listener a listener to be called back when a menu item is clicked
	 * @return a newly created SlidingMenu
	 */
	public SlidingMenu createMenu(final int type, final ArrayAdapter<?> adapter, final String title,
			final MenuClickListener listener) {

		if (SlidingMenu.LEFT == type) {
			mLeftMenu = new SlidingMenu(mActivity);

			mLeftMenu.setMode(SlidingMenu.LEFT);
			mLeftMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
			mLeftMenu.setShadowWidthRes(R.dimen.menu_shadow_width);
			mLeftMenu.setShadowDrawable(R.drawable.shadow_left);
			mLeftMenu.setBehindWidthRes(R.dimen.default_menu_width);
			mLeftMenu.attachToActivity(mActivity, SlidingMenu.SLIDING_WINDOW);
			mLeftMenu.setMenu(R.layout.left_menu);

			mLeftMenuFragment = new CustomMenuFragment();
			if (title != null) {
				mLeftMenuFragment.setTitle(title);
			}
			((CustomMenuFragment) mLeftMenuFragment).setCustomArrayAdapter(adapter);
			mLeftMenuFragment.registerListener(listener);

			return mLeftMenu;
		} else if (SlidingMenu.RIGHT == type) {
			mRightMenu = new SlidingMenu(mActivity);

			mRightMenu.setMode(SlidingMenu.RIGHT);
			mRightMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
			mRightMenu.setShadowWidthRes(R.dimen.menu_shadow_width);
			mRightMenu.setShadowDrawable(R.drawable.shadow_right);
			mRightMenu.setBehindWidthRes(R.dimen.default_menu_width);
			mRightMenu.attachToActivity(mActivity, SlidingMenu.SLIDING_WINDOW);
			mRightMenu.setMenu(R.layout.right_menu);

			mRightMenuFragment = new CustomMenuFragment();
			if (title != null) {
				mRightMenuFragment.setTitle(title);
			}
			((CustomMenuFragment) mRightMenuFragment).setCustomArrayAdapter(adapter);
			mRightMenuFragment.registerListener(listener);

			return mRightMenu;
		}
		return null;
	}

	/**
	 * Creates a SlidingMenu given various options.
	 * @param type the type of menu to create (LEFT/RIGHT)
	 * @param entries a list of entries with which to populate the menu list
	 * @param title an optional title to be displayed at the top of the menu, can be null
	 * @param listener a listener to be called back when a menu item is clicked
	 * @return a newly created SlidingMenu
	 */
	public SlidingMenu createMenu(final int type, final List<String> entries, final String title,
			final MenuClickListener listener) {

		if (SlidingMenu.LEFT == type) {
			mLeftMenu = new SlidingMenu(mActivity);

			mLeftMenu.setMode(SlidingMenu.LEFT);
			mLeftMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
			mLeftMenu.setShadowWidthRes(R.dimen.menu_shadow_width);
			mLeftMenu.setShadowDrawable(R.drawable.shadow_left);
			mLeftMenu.setBehindWidthRes(R.dimen.default_menu_width);
			mLeftMenu.attachToActivity(mActivity, SlidingMenu.SLIDING_WINDOW);
			mLeftMenu.setMenu(R.layout.left_menu);

			mLeftMenuFragment = new DefaultMenuFragment();
			if (title != null) {
				mLeftMenuFragment.setTitle(title);
			}
			((DefaultMenuFragment) mLeftMenuFragment).setMenuEntries(entries);
			mLeftMenuFragment.registerListener(listener);

			return mLeftMenu;
		} else if (SlidingMenu.RIGHT == type) {
			mRightMenu = new SlidingMenu(mActivity);

			mRightMenu.setMode(SlidingMenu.RIGHT);
			mRightMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
			mRightMenu.setShadowWidthRes(R.dimen.menu_shadow_width);
			mRightMenu.setShadowDrawable(R.drawable.shadow_right);
			mRightMenu.setBehindWidthRes(R.dimen.default_menu_width);
			mRightMenu.attachToActivity(mActivity, SlidingMenu.SLIDING_WINDOW);
			mRightMenu.setMenu(R.layout.right_menu);

			mRightMenuFragment = new DefaultMenuFragment();
			if (title != null) {
				mRightMenuFragment.setTitle(title);
			}
			((DefaultMenuFragment) mRightMenuFragment).setMenuEntries(entries);
			mRightMenuFragment.registerListener(listener);

			return mRightMenu;
		}
		return null;
	}

	/**
	 * Commits any SlidingMenu configuration by doing a fragment transaction to place the SlidingMenus (if created) in
	 * their appropriate containers.
	 */
	public void complete() {
		if (mLeftMenu != null) {
			mActivity.getFragmentManager().beginTransaction().replace(R.id.left_menu, mLeftMenuFragment).commit();
		}
		if (mRightMenu != null) {
			mActivity.getFragmentManager().beginTransaction().replace(R.id.right_menu, mRightMenuFragment).commit();
		}
	}
}
