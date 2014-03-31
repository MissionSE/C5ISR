package com.missionse.mseuiextensionsexample.graph;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.missionse.mseuiextensions.R;
import com.missionse.uiextensions.graph.PieChart;
import com.missionse.uiextensions.graph.PieSlice;

/**
 * Displays a Pie Graph.
 */
public class PieGraphFragment extends Fragment {

	private static final float BLUE = 45f;
	private static final float GREEN = 35f;
	private static final float RED = 25f;
	private static final float ORANGE = 15f;

	private static final int THICKNESS = 100;

	public PieGraphFragment() {
	}

	/**
	 * Creates a new instance of the PieGraphFragment.
	 * @return a new PieGraphFragment
	 */
	public static PieGraphFragment newInstance() {
		return new PieGraphFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_pie, container, false);

		if (contentView != null) {
			PieChart pieChart = (PieChart) contentView.findViewById(R.id.pie_chart);
			pieChart.setThickness(THICKNESS);

			PieSlice blueSlice = new PieSlice();
			blueSlice.setColor(getResources().getColor(R.color.holo_blue_light));
			blueSlice.setTitle("Blue");
			blueSlice.setValue(BLUE);

			PieSlice greenSlice = new PieSlice();
			greenSlice.setColor(getResources().getColor(R.color.holo_green_light));
			greenSlice.setTitle("Green");
			greenSlice.setValue(GREEN);

			PieSlice redSlice = new PieSlice();
			redSlice.setColor(getResources().getColor(R.color.holo_red_light));
			redSlice.setTitle("Red");
			redSlice.setValue(RED);

			PieSlice orangeSlice = new PieSlice();
			orangeSlice.setColor(getResources().getColor(R.color.holo_orange_light));
			orangeSlice.setTitle("Orange");
			orangeSlice.setValue(ORANGE);

			pieChart.addSlice(blueSlice);
			pieChart.addSlice(greenSlice);
			pieChart.addSlice(redSlice);
			pieChart.addSlice(orangeSlice);
		}

		return contentView;
	}
}
