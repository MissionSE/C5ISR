package com.missionse.mseuiextensionsexample.graph;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.missionse.mseuiextensions.R;
import com.missionse.uiextensions.graph.Bar;
import com.missionse.uiextensions.graph.BarGraph;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays a Pie Graph.
 */
public class BarGraphFragment extends Fragment {

	private static final float BLUE = 25f;
	private static final float GREEN = 35f;
	private static final float RED = 45f;
	private static final float ORANGE = 15f;

	public BarGraphFragment() {
	}

	/**
	 * Creates a new instance of the PieGraphFragment.
	 * @return a new PieGraphFragment
	 */
	public static BarGraphFragment newInstance() {
		return new BarGraphFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_bar, container, false);

		if (contentView != null) {
			BarGraph barGraph = (BarGraph) contentView.findViewById(R.id.bar_graph);
			barGraph.setShowAxis(true);
			barGraph.setShowBarText(true);

			Bar blueBar = new Bar();
			blueBar.setColor(getResources().getColor(R.color.holo_blue_light));
			blueBar.setName("Blue");
			blueBar.setValue(BLUE);
			blueBar.setValueString("45");

			Bar greenBar = new Bar();
			greenBar.setColor(getResources().getColor(R.color.holo_green_light));
			greenBar.setName("Green");
			greenBar.setValue(GREEN);
			greenBar.setValueString("25");

			Bar redBar = new Bar();
			redBar.setColor(getResources().getColor(R.color.holo_red_light));
			redBar.setName("Red");
			redBar.setValue(RED);
			redBar.setValueString("35");

			Bar orangeBar = new Bar();
			orangeBar.setColor(getResources().getColor(R.color.holo_orange_light));
			orangeBar.setName("Orange");
			orangeBar.setValue(ORANGE);
			orangeBar.setValueString("15");

			List<Bar> bars = new ArrayList<Bar>();
			bars.add(blueBar);
			bars.add(greenBar);
			bars.add(redBar);
			bars.add(orangeBar);

			barGraph.setBars(bars);
		}

		return contentView;
	}
}
