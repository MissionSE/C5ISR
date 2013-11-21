package com.missionse.slidingmenu;

import java.util.List;

import android.app.Activity;
import android.widget.ArrayAdapter;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class SlidingMenuHelper {

	private Activity activity;

	private SlidingMenu leftMenu;
	private MenuFragment leftMenuFragment;
	private SlidingMenu rightMenu;
	private MenuFragment rightMenuFragment;

	public SlidingMenuHelper(final Activity parent) {
		activity = parent;
	}

	public SlidingMenu createMenu(final int type, final ArrayAdapter<?> adapter, final String title,
			final MenuClickListener listener) {

		if (SlidingMenu.LEFT == type) {
			leftMenu = new SlidingMenu(activity);

			leftMenu.setMode(SlidingMenu.LEFT);
			leftMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
			leftMenu.setShadowWidthRes(R.dimen.menu_shadow_width);
			leftMenu.setShadowDrawable(R.drawable.shadow_left);
			leftMenu.setBehindWidthRes(R.dimen.default_menu_width);
			leftMenu.attachToActivity(activity, SlidingMenu.SLIDING_WINDOW);
			leftMenu.setMenu(R.layout.left_menu);

			leftMenuFragment = new CustomMenuFragment();
			if (title != null) {
				leftMenuFragment.setTitle(title);
			}
			((CustomMenuFragment) leftMenuFragment).setCustomArrayAdapter(adapter);
			leftMenuFragment.registerListener(listener);

			return leftMenu;
		} else if (SlidingMenu.RIGHT == type) {
			rightMenu = new SlidingMenu(activity);

			rightMenu.setMode(SlidingMenu.RIGHT);
			rightMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
			rightMenu.setShadowWidthRes(R.dimen.menu_shadow_width);
			rightMenu.setShadowDrawable(R.drawable.shadow_right);
			rightMenu.setBehindWidthRes(R.dimen.default_menu_width);
			rightMenu.attachToActivity(activity, SlidingMenu.SLIDING_WINDOW);
			rightMenu.setMenu(R.layout.right_menu);

			rightMenuFragment = new CustomMenuFragment();
			if (title != null) {
				rightMenuFragment.setTitle(title);
			}
			((CustomMenuFragment) rightMenuFragment).setCustomArrayAdapter(adapter);
			rightMenuFragment.registerListener(listener);

			return rightMenu;
		}
		return null;
	}

	public SlidingMenu createMenu(final int type, final List<String> entries, final String title,
			final MenuClickListener listener) {

		if (SlidingMenu.LEFT == type) {
			leftMenu = new SlidingMenu(activity);

			leftMenu.setMode(SlidingMenu.LEFT);
			leftMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
			leftMenu.setShadowWidthRes(R.dimen.menu_shadow_width);
			leftMenu.setShadowDrawable(R.drawable.shadow_left);
			leftMenu.setBehindWidthRes(R.dimen.default_menu_width);
			leftMenu.attachToActivity(activity, SlidingMenu.SLIDING_WINDOW);
			leftMenu.setMenu(R.layout.left_menu);

			leftMenuFragment = new DefaultMenuFragment();
			if (title != null) {
				leftMenuFragment.setTitle(title);
			}
			((DefaultMenuFragment) leftMenuFragment).setMenuEntries(entries);
			leftMenuFragment.registerListener(listener);

			return leftMenu;
		} else if (SlidingMenu.RIGHT == type) {
			rightMenu = new SlidingMenu(activity);

			rightMenu.setMode(SlidingMenu.RIGHT);
			rightMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
			rightMenu.setShadowWidthRes(R.dimen.menu_shadow_width);
			rightMenu.setShadowDrawable(R.drawable.shadow_right);
			rightMenu.setBehindWidthRes(R.dimen.default_menu_width);
			rightMenu.attachToActivity(activity, SlidingMenu.SLIDING_WINDOW);
			rightMenu.setMenu(R.layout.right_menu);

			rightMenuFragment = new DefaultMenuFragment();
			if (title != null) {
				rightMenuFragment.setTitle(title);
			}
			((DefaultMenuFragment) rightMenuFragment).setMenuEntries(entries);
			rightMenuFragment.registerListener(listener);

			return rightMenu;
		}
		return null;
	}

	public void complete() {
		if (leftMenu != null) {
			activity.getFragmentManager().beginTransaction().replace(R.id.left_menu, leftMenuFragment).commit();
		}
		if (rightMenu != null) {
			activity.getFragmentManager().beginTransaction().replace(R.id.right_menu, rightMenuFragment).commit();
		}
	}

}
