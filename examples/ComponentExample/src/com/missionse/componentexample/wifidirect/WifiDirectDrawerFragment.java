package com.missionse.componentexample.wifidirect;

import java.util.ArrayList;
import java.util.List;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.missionse.componentexample.R;

public class WifiDirectDrawerFragment extends ListFragment {
	
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
			final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.drawer_list, null);
	}
	
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		final List<String> menuItems = new ArrayList<String>();
		menuItems.add("Enable P2P");
		menuItems.add("Discover Peers");
		
		setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.drawer_list_item, 
				R.id.drawer_entry, menuItems));
	}
	
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		WifiDirectActivity activity = (WifiDirectActivity) getActivity();
		
		String selectedItem = (String) listView.getAdapter().getItem(position);
		if (selectedItem.equals("Enable P2P")) {
			activity.enableP2P();
		}
		else if (selectedItem.equals("Discover Peers")) {
			activity.discoverPeers();
		}
	}
}
