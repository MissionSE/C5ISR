package com.missionse.componentexample.viewpager;

import java.util.ArrayList;
import java.util.List;

import com.missionse.componentexample.R;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DrawerFragment extends ListFragment {
	
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
			final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.drawer_list, null);
	}
	
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		final List<String> menuItems = new ArrayList<String>();
		menuItems.add("Section 1");
		menuItems.add("Section 2");
		menuItems.add("Section 3");
		
		setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.drawer_list_item, 
				R.id.drawer_entry, menuItems));
	}
	
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		ViewPagerActivity activity = (ViewPagerActivity) getActivity();
		activity.switchContent(position);
	}
}
