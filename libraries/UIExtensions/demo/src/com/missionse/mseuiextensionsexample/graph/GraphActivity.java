package com.missionse.mseuiextensionsexample.graph;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.missionse.mseuiextensions.R;

/**
 * Demonstrates the usage of various graphs in the Graphing feature of the UI Extensions library.
 */
public class GraphActivity extends Activity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graph);

		final SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.action_list,
			android.R.layout.simple_spinner_dropdown_item);

		if (getActionBar() != null) {
			getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
			getActionBar().setListNavigationCallbacks(spinnerAdapter, new ActionBar.OnNavigationListener() {
				@Override
				public boolean onNavigationItemSelected(final int position, final long itemId) {
					String itemSelected = (String) spinnerAdapter.getItem(position);
					if (itemSelected.equals("Pie")) {
						showPieGraph();
					} else if (itemSelected.equals("Line")) {
						showLineGraph();
					} else if (itemSelected.equals("Bar")) {
						showBarGraph();
					}
					return true;
				}
			});
		}
	}

	private void showPieGraph() {
		PieGraphFragment pieGraphFragment = PieGraphFragment.newInstance();
		getFragmentManager().beginTransaction().replace(R.id.content_frame, pieGraphFragment).commit();
	}

	private void showLineGraph() {
		LineGraphFragment pieGraphFragment = LineGraphFragment.newInstance();
		getFragmentManager().beginTransaction().replace(R.id.content_frame, pieGraphFragment).commit();
	}

	private void showBarGraph() {
		BarGraphFragment pieGraphFragment = BarGraphFragment.newInstance();
		getFragmentManager().beginTransaction().replace(R.id.content_frame, pieGraphFragment).commit();
	}
}
