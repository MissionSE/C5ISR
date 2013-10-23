package com.missionse.componentexample.viewpager;

import com.missionse.componentexample.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class DummySectionFragment extends Fragment {
    
	private static final String CONTENT = "This is some content in section ";
	
	/**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String ARG_SECTION_NUMBER = "section_number";

    public DummySectionFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
    		final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_pager_dummy, container, false);
        TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
        dummyTextView.setText(CONTENT + Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)) + ".");
        return rootView;
    }
}
