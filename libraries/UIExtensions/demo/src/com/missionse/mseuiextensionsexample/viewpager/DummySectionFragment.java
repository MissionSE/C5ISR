package com.missionse.mseuiextensionsexample.viewpager;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.missionse.mseuiextensions.R;

/**
 * Acts as a dummy fragment, to display page changes.
 */
public class DummySectionFragment extends Fragment {

	private static final String CONTENT = "This is some content in section ";
	public static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Creates a new DummySectionFragment.
	 */
	public DummySectionFragment() {
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_view_pager_dummy, container, false);

		TextView text = (TextView) contentView.findViewById(R.id.section_label);
		text.setText(CONTENT + Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)) + ".");

		return contentView;
	}
}
