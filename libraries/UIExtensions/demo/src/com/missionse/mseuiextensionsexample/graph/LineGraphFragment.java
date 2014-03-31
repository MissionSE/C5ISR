package com.missionse.mseuiextensionsexample.graph;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.missionse.mseuiextensions.R;
import com.missionse.uiextensions.graph.Line;
import com.missionse.uiextensions.graph.LineGraph;
import com.missionse.uiextensions.graph.LinePoint;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Displays a Pie Graph.
 */
public class LineGraphFragment extends Fragment {

	private static final float X_UPPER_BOUND = 10f;
	private static final float Y_UPPER_BOUND = 10f;
	private static final float BUFFER = 0.5f;

	private DecimalFormat mFormatter;

	public LineGraphFragment() {
		mFormatter = new DecimalFormat("##.#");
		mFormatter.setRoundingMode(RoundingMode.DOWN);
	}

	/**
	 * Creates a new instance of the PieGraphFragment.
	 * @return a new PieGraphFragment
	 */
	public static LineGraphFragment newInstance() {
		return new LineGraphFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_line, container, false);

		if (contentView != null) {
			LineGraph lineGraph = (LineGraph) contentView.findViewById(R.id.line_graph);
			lineGraph.setXRange(0, X_UPPER_BOUND);
			lineGraph.setXAxisBounds("0", "10");
			lineGraph.setYRange(0, Y_UPPER_BOUND);
			lineGraph.setYAxisBounds("low", "high");

			Line blueLine = new Line();
			blueLine.setFill(true);
			blueLine.setColor(getResources().getColor(R.color.holo_blue_light));
			addRandomTrendingPoints(blueLine);

			Line greenLine = new Line();
			greenLine.setFill(true);
			greenLine.setColor(getResources().getColor(R.color.holo_green_light));
			addRandomTrendingPoints(greenLine);

			Line redLine = new Line();
			redLine.setFill(true);
			redLine.setColor(getResources().getColor(R.color.holo_red_light));
			addRandomTrendingPoints(redLine);

			Line orangeLine = new Line();
			orangeLine.setFill(true);
			orangeLine.setColor(getResources().getColor(R.color.holo_orange_light));
			addRandomTrendingPoints(orangeLine);

			lineGraph.addLine(blueLine);
			lineGraph.addLine(greenLine);
			lineGraph.addLine(redLine);
			lineGraph.addLine(orangeLine);
		}

		return contentView;
	}

	private void addRandomTrendingPoints(final Line line) {
		float initialYValue = (float) (Math.random() * Y_UPPER_BOUND);
		LinePoint firstPoint = createLinePoint(0, initialYValue);
		line.addPoint(firstPoint);
		float lastYValue = initialYValue;
		for (int count = 1; count <= X_UPPER_BOUND; count++) {
			float yValueChange = (float) (Math.random() * 2f - 1f);
			LinePoint point = createLinePoint(count, lastYValue + yValueChange);
			line.addPoint(point);
			lastYValue = point.getY();
		}
	}

	private LinePoint createLinePoint(float xValue, float yValue) {
		LinePoint point = new LinePoint(xValue, yValue);
		if (point.getY() < BUFFER) {
			point.setY(BUFFER);
		} else if (point.getY() > Y_UPPER_BOUND - BUFFER) {
			point.setY(Y_UPPER_BOUND - BUFFER);
		}
		point.setTitle(mFormatter.format(point.getY()));
		return point;
	}
}
