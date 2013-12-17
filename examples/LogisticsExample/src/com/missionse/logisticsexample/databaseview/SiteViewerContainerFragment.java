package com.missionse.logisticsexample.databaseview;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.missionse.logisticsexample.R;

/**
 * Contains two side-by-side fragments, managing communication between the two.
 */
public class SiteViewerContainerFragment extends Fragment {

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup parent, final Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_database_container, parent, false);

		getChildFragmentManager().beginTransaction().replace(R.id.database_list, new SiteListFragment(), "sitelist")
				.commit();
		getChildFragmentManager().beginTransaction().replace(R.id.database_detail, new Fragment(), "empty").commit();

		return contentView;
	}
}
