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

public class MenuFragment extends Fragment {

	protected ListView menuList;
	private String title;

	private final List<MenuClickListener> listeners = new ArrayList<MenuClickListener>();

	public void registerListener(final MenuClickListener listener) {
		listeners.add(listener);
	}

	public void deregisterListener(final MenuClickListener listener) {
		listeners.remove(listener);
	}

	public void setTitle(final String newTitle) {
		title = newTitle;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.default_menu_list, null);

		TextView titleView = (TextView) contentView.findViewById(R.id.menu_header);
		if (title != null) {
			titleView.setText(title);
		} else {
			titleView.setVisibility(View.GONE);
		}

		menuList = (ListView) contentView.findViewById(R.id.menu_list);
		menuList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
				for (MenuClickListener listener : listeners) {
					listener.onMenuClick(position);
				}
			}
		});

		return contentView;
	}
}
