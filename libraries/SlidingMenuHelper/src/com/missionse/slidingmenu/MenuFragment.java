package com.missionse.slidingmenu;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Base fragment that provides handling of the mTitle and menu click listener when the parent view is created.
 */
public class MenuFragment extends Fragment {

	private ListView mMenuList;

	private String mTitle;

	private final List<MenuClickListener> mListeners = new ArrayList<MenuClickListener>();

	/**
	 * Registers a listener to be called back when a menu item is clicked.
	 * @param listener a listener to be called back
	 */
	public void registerListener(final MenuClickListener listener) {
		mListeners.add(listener);
	}

	/**
	 * Sets the title of this menu.
	 * @param newTitle the title to be used
	 */
	public void setTitle(final String newTitle) {
		mTitle = newTitle;
	}

	/**
	 * The menu list created when the view is inflated.
	 * @return the ListView of this fragment
	 */
	public ListView getMenuList() {
		return mMenuList;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.default_menu_list, null);

		TextView titleView = (TextView) contentView.findViewById(R.id.menu_header);
		if (mTitle != null) {
			titleView.setText(mTitle);
		} else {
			titleView.setVisibility(View.GONE);
		}

		mMenuList = (ListView) contentView.findViewById(R.id.menu_list);
		mMenuList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
				for (MenuClickListener listener : mListeners) {
					listener.onMenuClick(position);
				}
			}
		});

		return contentView;
	}
}
